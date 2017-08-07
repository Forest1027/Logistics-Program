package com.forest.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.crm.dao.CustomerRepository;
import com.forest.crm.domain.Customer;
import com.forest.crm.service.ICustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService{
	@Autowired
	private CustomerRepository repository;

	@Override
	public List<Customer> findNoAssosnCustomer() {
		//fixedAreaId is null
		return repository.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findAssosnCustomer(String fixedAreaId) {
		// find by fixedAreaId
		return repository.findByFixedAreaId(fixedAreaId);
	}

	@Override
	public void assosnCustomerToFixedArea(String customerIdStr, String fixedAreaId) {
		//本质是将客户的fixedareaid的值进行更新
		//获取客户id
		String[] ids = customerIdStr.split(",");
		for (String idStr : ids) {
			Integer id = Integer.parseInt(idStr);
			repository.updateCIdWithFAId(id,fixedAreaId);
		}
	}

}
