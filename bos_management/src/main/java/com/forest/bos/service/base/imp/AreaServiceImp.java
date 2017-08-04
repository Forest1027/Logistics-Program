package com.forest.bos.service.base.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.base.AreaRepository;
import com.forest.bos.domain.base.Area;
import com.forest.bos.service.base.IAreaService;

@Service
@Transactional
public class AreaServiceImp implements IAreaService{
	@Autowired
	private AreaRepository repository;

	@Override
	public void batchImport(List<Area> areas) {
		repository.save(areas);
	}

	@Override
	public Page<Area> findAll(Specification<Area> specification, Pageable pageable) {
		Page<Area> page = repository.findAll(specification, pageable);
		return page;
	}

}
