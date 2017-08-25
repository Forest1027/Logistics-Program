package com.forest.bos.service.system.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.system.PermissionRepository;
import com.forest.bos.domain.system.Permission;
import com.forest.bos.domain.system.User;
import com.forest.bos.service.system.IPermissionService;

@Service
@Transactional
public class PermissionServiceImp implements IPermissionService{
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public List<Permission> findByUser(User user) {
		//判断用户名
		if ("admin".equals(user.getUsername())) {
			return permissionRepository.findAll();
		}else {
			return permissionRepository.findByUser(user.getId());
		}
	}

	@Override
	public List<Permission> finaAll() {
		return permissionRepository.findAll();
	}

	@Override
	public void add(Permission model) {
		permissionRepository.save(model);
	}
}
