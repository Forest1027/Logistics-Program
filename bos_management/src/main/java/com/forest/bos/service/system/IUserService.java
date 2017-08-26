package com.forest.bos.service.system;

import java.util.List;

import com.forest.bos.domain.system.User;

public interface IUserService {

	public User findByUsername(String username);

	public List<User> findAll();

	public void save(User model, String[] roleIds);

}
