package com.forest.bos.web.action.system;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.system.User;
import com.forest.bos.service.system.IUserService;
import com.forest.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {
	@Action(value = "user_login", results = { @Result(name = "success", type = "redirect", location = "/index.html"),
			@Result(name = "login", type = "redirect", location = "/login.html") })
	public String login() {
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		try {
			subject.login(token);
			// 登录成功
			return SUCCESS;
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 登录失败
			return LOGIN;
		}
	}

	@Action(value = "user_logout", results = { @Result(name = "success", type = "redirect", location = "/login.html") })
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		System.out.println("用戶退出登录啦---------------");
		return SUCCESS;
	}

	@Autowired
	private IUserService userService;

	@Action(value = "user_list", results = { @Result(name = "success", type = "json") })
	public String list() {
		List<User> users = userService.findAll();
		ActionContext.getContext().getValueStack().push(users);
		return SUCCESS;
	}

	private String[] roleIds;

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	@Action(value = "user_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/system/userlist.html") })
	public String save() {
		userService.save(model, roleIds);
		return SUCCESS;
	}
}
