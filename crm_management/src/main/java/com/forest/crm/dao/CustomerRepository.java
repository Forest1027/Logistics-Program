package com.forest.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.forest.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	public List<Customer> findByFixedAreaIdIsNull();

	public List<Customer> findByFixedAreaId(String fixedAreaId);

	@Query(value = "update Customer set fixedAreaId=?2 where id=?1")
	@Modifying
	public void updateCIdWithFAId(Integer id, String fixedAreaId);

	@Query(value = "update Customer set fixedAreaId=null where fixedAreaId=?")
	@Modifying
	public void clearFixedAreaId(String fixedAreaId);

	public Customer findByTelephone(String telephone);
	
	@Query(value="update Customer set type=1 where telephone=?")
	@Modifying
	public void updateType(String telephone);

}
