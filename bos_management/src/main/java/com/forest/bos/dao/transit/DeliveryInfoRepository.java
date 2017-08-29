package com.forest.bos.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forest.bos.domain.transit.DeliveryInfo;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer> {

}
