package com.forest.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forest.bos.domain.take_delivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	public Order findByOrderNum(String orderNum);

}
