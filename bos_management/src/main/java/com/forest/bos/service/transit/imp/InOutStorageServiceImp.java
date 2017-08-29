package com.forest.bos.service.transit.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.transit.InOutStorageRepository;
import com.forest.bos.dao.transit.TransitinfoRepository;
import com.forest.bos.domain.transit.InOutStorageInfo;
import com.forest.bos.domain.transit.TransitInfo;
import com.forest.bos.service.transit.IInOutStorageService;

@Service
@Transactional
public class InOutStorageServiceImp implements IInOutStorageService{
	@Autowired
	private InOutStorageRepository inOutStorageRepository;
	
	@Autowired
	private TransitinfoRepository transitInfoRepository;

	@Override
	public void save(String transitInfoId, InOutStorageInfo model) {
		//保存出入库信息
		inOutStorageRepository.save(model);
		
		//查询transitinfo
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		
		//将inoutstorageinfo关联到查询到的transitinfo.此处一方维护多方，与以前不同
		transitInfo.getInOutStorageInfos().add(model);
		
		//“到达网点”的特殊情况
		if("到达网点".equals(model.getOperation())) {
			transitInfo.setStatus("到达网点");
			transitInfo.setOutletAddress(model.getAddress());
		}
	}
}
