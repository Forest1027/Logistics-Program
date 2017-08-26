package com.forest.bos.service.system;

import java.util.List;

import com.forest.bos.domain.system.Role;
import com.forest.bos.domain.system.User;

public interface IRoleService {

	public List<Role> findByUser(User user);

	public List<Role> findAll();

	public void save(Role model, String[] permissionIds, String menuIds);

}
