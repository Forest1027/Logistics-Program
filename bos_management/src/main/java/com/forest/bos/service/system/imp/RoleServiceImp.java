package com.forest.bos.service.system.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.system.RoleRepository;
import com.forest.bos.domain.system.Role;
import com.forest.bos.domain.system.User;
import com.forest.bos.service.system.IRoleService;

@Service
@Transactional
public class RoleServiceImp implements IRoleService{
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<Role> findByUser(User user) {
		//判断username
		if ("admin".equals(user.getUsername())) {
			//admin具有所有角色
			return roleRepository.findAll();
		}else {
			//根据user id查询
			return roleRepository.findByUser(user.getId());
		}
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

}
