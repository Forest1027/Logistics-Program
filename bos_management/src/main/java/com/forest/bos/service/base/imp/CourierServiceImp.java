package com.forest.bos.service.base.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.base.CourierRepository;
import com.forest.bos.domain.base.Courier;
import com.forest.bos.service.base.ICourierService;

@Service
@Transactional
public class CourierServiceImp implements ICourierService{
	@Autowired
	private CourierRepository repository;

	@Override
	public void save(Courier courier) {
		repository.save(courier);
	}
}
