package com.forest.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.forest.bos.domain.base.Courier;

public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {
	
	@Query(value="update Courier set deltag='1' where id=?")
	@Modifying
	public void updateDeltag(Integer id);

}
