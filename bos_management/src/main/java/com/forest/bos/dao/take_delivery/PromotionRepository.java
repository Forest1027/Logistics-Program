package com.forest.bos.dao.take_delivery;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.forest.bos.domain.take_delivery.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer>,JpaSpecificationExecutor<Promotion>{
	
	@Query("update Promotion set status='2' where endDate<? and status='1'")
	@Modifying
	public void updateStatus(Date date);

}
