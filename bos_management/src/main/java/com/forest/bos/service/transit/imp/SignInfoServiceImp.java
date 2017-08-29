package com.forest.bos.service.transit.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.index.WayBillIndexRepository;
import com.forest.bos.dao.transit.SignInfoRepository;
import com.forest.bos.dao.transit.TransitinfoRepository;
import com.forest.bos.domain.transit.SignInfo;
import com.forest.bos.domain.transit.TransitInfo;
import com.forest.bos.service.transit.ISignInfoService;

@Service
@Transactional
public class SignInfoServiceImp implements ISignInfoService {
	@Autowired
	private SignInfoRepository signInfoRepository;
	
	@Autowired
	private TransitinfoRepository transitinfoRepository;
	
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void save(String transitId, SignInfo model) {
		signInfoRepository.save(model);
		TransitInfo transitInfo = transitinfoRepository.findOne(Integer.parseInt(transitId));
		transitInfo.setSignInfo(model);
		
		if(model.getSignType().equals("正常")) {
			transitInfo.setStatus("正常签收");
			transitInfo.getWayBill().setSignStatus(3);
			wayBillIndexRepository.save(transitInfo.getWayBill());
		}else {
			transitInfo.setStatus("异常");
			transitInfo.getWayBill().setSignStatus(4);
			wayBillIndexRepository.save(transitInfo.getWayBill());
		}
	}

}
