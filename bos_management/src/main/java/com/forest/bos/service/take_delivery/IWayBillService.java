package com.forest.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.forest.bos.domain.take_delivery.WayBill;

public interface IWayBillService {
	public void save(WayBill wayBill);

	public Page<WayBill> pageQuery(Pageable pageable);

	public WayBill findByWayBillNum(String wayBillNum);

	public Page<WayBill> pageQuery(WayBill model, Pageable pageable);
	
	public void syncIndex();
}
