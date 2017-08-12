package com.forest.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.forest.bos.domain.take_delivery.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer>,JpaSpecificationExecutor<Promotion>{

}
