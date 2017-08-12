package com.forest.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.forest.bos.domain.take_delivery.Promotion;

public interface IPromotionService {

	public void save(Promotion model);

	public Page<Promotion> findPageData(Pageable pageable);

}
