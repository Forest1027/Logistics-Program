package com.forest.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.forest.bos.domain.system.Role;
import com.forest.bos.domain.system.Permission;
import com.forest.bos.domain.system.User;
import com.forest.bos.service.system.IPermissionService;
import com.forest.bos.service.system.IRoleService;
import com.forest.bos.service.system.IUserService;

@Service("bosRealm")
public class BosRealm extends AuthorizingRealm{
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IPermissionService permissionService;

	@Override
	//授权
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		//取出用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		//查询角色，赋予角色
		List<Role> roles = roleService.findByUser(user);
		for (Role role : roles) {
			simpleAuthorizationInfo.addRole(role.getKeyword());
		}
		//查询权限，赋予权限
		List<Permission> permissions = permissionService.findByUser(user);
		for (Permission permission : permissions) {
			simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
		}
		return simpleAuthorizationInfo;
	}

	@Override
	//认证
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken userToken = (UsernamePasswordToken)token;
		//查询subject中保存的用户是否存在
		User user = userService.findByUsername(userToken.getUsername());
		if (user==null) {
			//不存在
			return null;
		}else {
			//存在
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		}
	}

}
