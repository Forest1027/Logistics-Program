package com.forest.bos.service.transit;

import com.forest.bos.domain.transit.InOutStorageInfo;

public interface IInOutStorageService {

	public void save(String transitInfoId, InOutStorageInfo model);

}
