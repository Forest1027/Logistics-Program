package com.forest.bos.service.system.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.system.RoleRepository;
import com.forest.bos.dao.system.UserRepository;
import com.forest.bos.domain.system.Role;
import com.forest.bos.domain.system.User;
import com.forest.bos.service.system.IUserService;

@Service
@Transactional
public class UserServiceImp implements IUserService{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void save(User model, String[] roleIds) {
		if (roleIds!=null) {
			for (String roleId : roleIds) {
				Role role = roleRepository.findOne(Integer.parseInt(roleId));
				model.getRoles().add(role);
				userRepository.save(model);
			}
		}
		
	}

}
