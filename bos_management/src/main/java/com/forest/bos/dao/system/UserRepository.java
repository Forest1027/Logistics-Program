package com.forest.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forest.bos.domain.system.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public User findByUsername(String username);

}
