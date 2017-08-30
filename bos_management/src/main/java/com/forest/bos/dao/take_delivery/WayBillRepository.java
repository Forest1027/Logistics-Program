package com.forest.bos.dao.take_delivery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.forest.bos.domain.take_delivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill, Integer>,JpaSpecificationExecutor<WayBill>{

	public WayBill findByWayBillNum(String wayBillNum);

}
