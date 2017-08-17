package com.forest.bos.service.take_delivery.imp;

import java.util.Iterator;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.forest.bos.dao.base.FixedRepository;
import com.forest.bos.dao.base.OrderRepository;
import com.forest.bos.domain.base.Courier;
import com.forest.bos.domain.base.FixedArea;
import com.forest.bos.domain.common.Constants;
import com.forest.bos.domain.take_delivery.Order;
import com.forest.bos.service.take_delivery.IOrderService;

public class OrderServiceImp implements IOrderService {
	@Autowired
	private FixedRepository fRepository;
	
	@Autowired
	private OrderRepository oRepository;

	@Override
	public void addOrder(Order order) {
		// 确定快递员-->
		// webservice从crm_management根据address获取定区
		String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_URL
				+ "/crm_management/services/customerService/findFixedAreaByAddress?address=" + order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);
		// 根据定区获取快递员
		if (fixedAreaId != null) {
			FixedArea fixedArea = fRepository.findOne(fixedAreaId);
			Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
			// 先判断迭代器是否是否有下一个元素，不然可能会报空指针异常
			if (iterator.hasNext()) {
				// 此处只是随便取了集合中的第一个快递员
				Courier courier = iterator.next();
				// 将匹配到的快递员存进order
				order.setCourier(courier);
				// 生成订单号
				order.setOrderNum(UUID.randomUUID().toString());
				// 保存订单
				oRepository.save(order);
				// 生成工单,发送短信
			}
		}

	}

}
