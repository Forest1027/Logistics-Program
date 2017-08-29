package com.forest.bos.service.transit.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.transit.DeliveryInfoRepository;
import com.forest.bos.dao.transit.TransitinfoRepository;
import com.forest.bos.domain.transit.DeliveryInfo;
import com.forest.bos.domain.transit.TransitInfo;
import com.forest.bos.service.transit.IDeliveryInfoService;

@Service
@Transactional
public class DeliveryInfoServiceImp implements IDeliveryInfoService {
	@Autowired
	private TransitinfoRepository transitInfoRepository;
	
	@Autowired
	private DeliveryInfoRepository deliveryInfoRepository;

	@Override
	public void save(String transitId, DeliveryInfo model) {
		// TODO Auto-generated method stub
		deliveryInfoRepository.save(model);
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitId));
		transitInfo.setDeliveryInfo(model);
		transitInfo.setStatus("开始配送");
	}

}
