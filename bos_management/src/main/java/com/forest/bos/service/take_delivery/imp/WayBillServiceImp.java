package com.forest.bos.service.take_delivery.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.forest.bos.dao.take_delivery.WayBillRepository;
import com.forest.bos.domain.take_delivery.WayBill;
import com.forest.bos.service.take_delivery.IWayBillService;

@Service
@Transactional
public class WayBillServiceImp implements IWayBillService{
	@Autowired
	private WayBillRepository repository;

	@Override
	public void save(WayBill wayBill) {
		repository.save(wayBill);
	}

	@Override
	public Page<WayBill> pageQuery(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return repository.findByWayBillNum(wayBillNum);
	}

}
