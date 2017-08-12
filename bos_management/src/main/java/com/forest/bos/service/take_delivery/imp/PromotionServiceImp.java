package com.forest.bos.service.take_delivery.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.take_delivery.PromotionRepository;
import com.forest.bos.domain.take_delivery.Promotion;
import com.forest.bos.service.take_delivery.IPromotionService;

@Service
@Transactional
public class PromotionServiceImp implements IPromotionService{
	@Autowired
	private PromotionRepository repository;

	@Override
	public void save(Promotion model) {
		repository.save(model);
	}

	@Override
	public Page<Promotion> findPageData(Pageable pageable) {
		return repository.findAll(pageable);
	}

}
