package com.forest.bos.service.take_delivery.imp;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.take_delivery.PromotionRepository;
import com.forest.bos.domain.take_delivery.PageBean;
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

	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		Pageable pageable = new PageRequest(page-1,rows);
		Page<Promotion> pageData = repository.findAll(pageable);
		
		PageBean<Promotion> pageBean = new PageBean<>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setRows(pageData.getContent());
		return pageBean;
	}

	@Override
	public Promotion findById(Integer id) {
		return repository.findOne(id);
	}

	@Override
	public void updateStatus(Date date) {
		repository.updateStatus(date);
	}

}
