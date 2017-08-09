package com.forest.bos.service.base.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.base.TakeTimeRepository;
import com.forest.bos.domain.base.TakeTime;
import com.forest.bos.service.base.ITakeTimeService;

@Service
@Transactional
public class TakeTimeServiceImp implements ITakeTimeService{
	@Autowired
	private TakeTimeRepository repository;

	@Override
	public List<TakeTime> finAll() {
		return repository.findAll();
	}

}
