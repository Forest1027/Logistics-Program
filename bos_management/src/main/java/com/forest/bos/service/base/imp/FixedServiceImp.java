package com.forest.bos.service.base.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.base.FixedRepository;
import com.forest.bos.domain.base.FixedArea;
import com.forest.bos.service.base.IFixedService;

@Service
@Transactional
public class FixedServiceImp implements IFixedService{
	@Autowired
	private FixedRepository repository;

	@Override
	public void save(FixedArea model) {
		repository.save(model);
	}

	@Override
	public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable) {
		Page<FixedArea> page = repository.findAll(specification,pageable);
		return page;
	}

}
