package com.forest.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.forest.bos.domain.system.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer>{
	
	@Query("from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id=? order by m.priority")
	public List<Menu> findByUser(Integer id);

}
