package com.forest.bos.service.transit.imp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.forest.bos.dao.take_delivery.WayBillRepository;
import com.forest.bos.dao.transit.TransitinfoRepository;
import com.forest.bos.domain.take_delivery.WayBill;
import com.forest.bos.domain.transit.TransitInfo;
import com.forest.bos.service.transit.ITransitinfoService;

public class TransitinfoServiceImp implements ITransitinfoService{
	@Autowired
	private WayBillRepository waybillRepository;
	
	@Autowired
	private TransitinfoRepository transitinfoRepository;

	@Override
	public void create(String wayBillIds) {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(wayBillIds)) {
			for (String wayBillId : wayBillIds.split(",")) {
				//将对应的wayBill查询出来
				WayBill wayBill = waybillRepository.findOne(Integer.parseInt(wayBillId));
				//判断是否处于待发货状态
				if (wayBill.getWayBillType()=="1") {
					TransitInfo transitInfo = new TransitInfo();
					transitInfo.setWayBill(wayBill);
					transitInfo.setStatus("出入库中转");
					transitinfoRepository.save(transitInfo);
					
					//将waybill的状态改变
					wayBill.setWayBillType("2");
				}
			}
		}
	}

}
