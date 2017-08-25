package com.forest.bos.service.system;

import java.util.List;

import com.forest.bos.domain.system.Permission;
import com.forest.bos.domain.system.User;

public interface IPermissionService {

	public List<Permission> findByUser(User user);

	public List<Permission> finaAll();

	public void add(Permission model);

}
