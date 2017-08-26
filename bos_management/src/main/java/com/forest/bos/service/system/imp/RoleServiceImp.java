package com.forest.bos.service.system.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.system.MenuRepository;
import com.forest.bos.dao.system.PermissionRepository;
import com.forest.bos.dao.system.RoleRepository;
import com.forest.bos.domain.system.Menu;
import com.forest.bos.domain.system.Permission;
import com.forest.bos.domain.system.Role;
import com.forest.bos.domain.system.User;
import com.forest.bos.service.system.IRoleService;

@Service
@Transactional
public class RoleServiceImp implements IRoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;
	
	@Autowired
	private MenuRepository menuRepository;


	@Override
	public List<Role> findByUser(User user) {
		// 判断username
		if ("admin".equals(user.getUsername())) {
			// admin具有所有角色
			return roleRepository.findAll();
		} else {
			// 根据user id查询
			return roleRepository.findByUser(user.getId());
		}
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public void save(Role model, String[] permissionIds, String menuIds) {
		// TODO Auto-generated method stub
		// 保存角色信息
		roleRepository.save(model);
		// 保存permission
		if (permissionIds != null) {
			for (String permissionId : permissionIds) {
				Permission permission = permissionRepository.findOne(Integer.parseInt(permissionId));
				model.getPermissions().add(permission);
			}
		}
		// 保存menu
		if (menuIds != null) {
			String[] menuIdes = menuIds.split(",");
			for (String menuId : menuIdes) {
				Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
				model.getMenus().add(menu);
			}
		}
	}

}
