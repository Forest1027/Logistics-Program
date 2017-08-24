package com.forest.bos.dao.system;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forest.bos.domain.system.User;

public interface UserRepository extends JpaRepository<User, Serializable>{

	public User findByUsername(String username);

}
