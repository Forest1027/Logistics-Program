package com.forest.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
		//首先将已关联的客户都解除关联
		repository.clearFixedAreaId(fixedAreaId);
		//判断传过来的id是否为空
		System.out.println(customerIdStr);
		if("null".equals(customerIdStr)) {
			System.out.println("结束");
			return;
		}
		//获取客户id
		String[] ids = customerIdStr.split(",");
		System.out.println(ids+"------------");
		System.out.println(fixedAreaId+"--------------");
		for (String idStr : ids) {
			Integer id = Integer.parseInt(idStr);
			repository.updateCIdWithFAId(id,fixedAreaId);
		}
	}

	@Override
	public void regist(Customer customer) {
		System.out.println(customer);
		repository.save(customer);
	}

	@Override
	public Customer findByTelephone(String telephone) {
		return repository.findByTelephone(telephone);
	}

	@Override
	public void updateByTelephone(String telephone) {
		repository.updateType(telephone);
	}

}
