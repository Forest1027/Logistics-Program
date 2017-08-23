package com.forest.bos.dao.index;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.forest.bos.domain.take_delivery.WayBill;

public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill, Integer>{
}
