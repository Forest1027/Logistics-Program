package com.forest.bos.service.base.imp;

import org.springframework.beans.factory.annotation.Autowired;
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
}
