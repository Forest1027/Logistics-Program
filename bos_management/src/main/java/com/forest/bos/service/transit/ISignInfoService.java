package com.forest.bos.service.transit;

import com.forest.bos.domain.transit.SignInfo;

public interface ISignInfoService {

	public void save(String transitId, SignInfo model);

}
