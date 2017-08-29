package com.forest.bos.service.transit;

import com.forest.bos.domain.transit.DeliveryInfo;

public interface IDeliveryInfoService {

	public void save(String transitId, DeliveryInfo model);

}
