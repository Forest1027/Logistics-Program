package com.forest.bos.service.system.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.system.UserRepository;
import com.forest.bos.domain.system.User;
import com.forest.bos.service.system.IUserService;

@Service
@Transactional
public class UserServiceImp implements IUserService{
	@Autowired
	private UserRepository repository;

	@Override
	public User findByUsername(String username) {
		return repository.findByUsername(username);
	}

}
