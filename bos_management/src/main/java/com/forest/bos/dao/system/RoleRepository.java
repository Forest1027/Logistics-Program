package com.forest.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.forest.bos.domain.system.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	
	/*@Query("from Role r inner join r.users u where u.id=?") 
	 public List<Role> findByUserId(Integer id);*/
	 

	@Query("from Role r inner join fetch r.users u where u.id = ?")
	public List<Role> findByUser(Integer id);

}
