package com.forest.bos.service.system;

import com.forest.bos.domain.system.User;

public interface IUserService {

	public User findByUsername(String username);

}
