package com.forest.bos.service.base.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.base.StandardRepository;
import com.forest.bos.domain.base.Standard;
import com.forest.bos.service.base.IStandardService;

@Service
@Transactional
public class StandardServiceImp implements IStandardService{
	@Autowired
	private StandardRepository repository;
	
	@Override
	public void save(Standard standard){
		repository.save(standard);
	}

	@Override
	public Page<Standard> findPageData(Pageable pageable) {
		Page<Standard> pageData = repository.findAll(pageable);
		return pageData;
	}

	@Override
	public List<Standard> findAll() {
		List<Standard> standards = repository.findAll();
		return standards;
	}
}
