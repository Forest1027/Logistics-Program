package com.forest.bos.dao.base;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.forest.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard,Integer>{
	//查询的三种方法
	public List<Standard> findByName(String name);
	
	@Query(value="from Standard where name=?",nativeQuery=false)
	public List<Standard> queryName(String name);
	
	@Query
	public List<Standard> queryName2(String name);
	
	//
}
