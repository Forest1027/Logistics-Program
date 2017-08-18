package com.forest.bos.service.take_delivery.imp;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.base.AreaRepository;
import com.forest.bos.dao.base.FixedRepository;
import com.forest.bos.dao.base.OrderRepository;
import com.forest.bos.dao.base.WorkBillRepository;
import com.forest.bos.domain.base.Area;
import com.forest.bos.domain.base.Courier;
import com.forest.bos.domain.base.FixedArea;
import com.forest.bos.domain.base.SubArea;
import com.forest.bos.domain.common.Constants;
import com.forest.bos.domain.take_delivery.Order;
import com.forest.bos.domain.take_delivery.WorkBill;
import com.forest.bos.service.take_delivery.IOrderService;

@Service
@Transactional
public class OrderServiceImp implements IOrderService {
	@Autowired
	private FixedRepository fRepository;

	@Autowired
	private OrderRepository oRepository;

	@Autowired
	private AreaRepository aRepository;

	@Autowired
	private WorkBillRepository wbRepository;

	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public void addOrder(Order order) {
		// 将order中的area查询一下，不然一开始设置的area里面是没有id的，无法建立外键
		Area sendArea = order.getSendArea();
		Area sendAreaP = aRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(),
				sendArea.getDistrict());

		Area recArea = order.getRecArea();
		Area recAreaP = aRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(),
				recArea.getDistrict());

		order.setSendArea(sendAreaP);
		order.setRecArea(recAreaP);

		// 1.基于crm匹配快递员
		// webservice从crm_management根据address获取定区
		String fixedAreaId = WebClient
				.create(Constants.CRM_MANAGEMENT_URL
						+ "/crm_management/services/customerService/findFixedAreaByAddress/" + order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);
		// 根据定区获取快递员
		System.out.println(fixedAreaId + "----------------------");
		if (fixedAreaId != null) {
			FixedArea fixedArea = fRepository.findOne(fixedAreaId);
			saveOrder(order, fixedArea);
		}

		// 2.基于分区关键字匹配
		for (SubArea subArea : sendAreaP.getSubareas()) {
			// 订单地址包哈该分区的关键字
			if (order.getSendAddress().contains(subArea.getKeyWords())) {
				// 找到该分区所属的定区
				FixedArea fixedArea = subArea.getFixedArea();
				System.out.println(fixedArea.getCouriers() + "************************");
				// 根据定区，找到在职的快递员,保存order
				saveOrder(order, fixedArea);
			}
		}

		// 3.自动分单失败，进入人工分单
		order.setOrderType("2");
		oRepository.save(order);
	}

	private void saveOrder(Order order, FixedArea fixedArea) {
		if (fixedArea.getCouriers() != null) {
			Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
			// 先判断迭代器是否是否有下一个元素，不然可能会报空指针异常
			if (iterator.hasNext()) {
				// 此处只是随便取了集合中的第一个快递员
				Courier courier = iterator.next();
				// 将匹配到的快递员存进order
				order.setCourier(courier);
				// 生成订单号
				order.setOrderNum(UUID.randomUUID().toString());
				// 标记为自动分单
				order.setOrderType("1");
				// 记录下单时间
				order.setOrderTime(new Date());
				// 保存订单
				oRepository.save(order);
				// 生成工单,发送短信
				generateWorkbill(order);
				return;
			}
		}
	}

	public void generateWorkbill(final Order order) {
		// 生成工单
		WorkBill workBill = new WorkBill();
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		final String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber);
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		wbRepository.save(workBill);
		// 发送短信
		jmsTemplate.send("bos_sms", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				mapMessage.setString("msg",
						"短信序号：" + smsNumber + "，取件地址：" + order.getSendAddress() + "，联系人：" + order.getSendName()
								+ "，联系电话：" + order.getSendMobile() + "，对快递员说：" + order.getSendMobileMsg());
				return mapMessage;
			}
		});
		// 修改工单状态
		workBill.setPickstate("已通知");
	}
}
