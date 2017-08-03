package com.forest.bos.service.base.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

	@Override
	public Page<Courier> pageQuery(Specification<Courier> specification, Pageable pageable) {
		Page<Courier> page = repository.findAll(specification,pageable);
		return page;
	}
}
