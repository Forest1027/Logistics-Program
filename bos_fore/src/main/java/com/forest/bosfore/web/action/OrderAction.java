package com.forest.bosfore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.base.Area;
import com.forest.bos.domain.common.Constants;
import com.forest.bos.domain.take_delivery.Order;
import com.forest.bosfore.web.action.common.BaseAction;
import com.forest.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
	private String sendAreaInfo;
	private String recAreaInfo;

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	@Action(value = "order_add", results = { @Result(name = "success", type = "redirect", location = "/index.html") })
	public String add() {
		// 将获取到的地址切分成省市区
		String[] sendAreaData = sendAreaInfo.split("/");
		String[] recAreaData = recAreaInfo.split("/");

		// 一一存进Area
		Area sendArea = new Area();
		sendArea.setProvince(sendAreaData[0]);
		sendArea.setCity(sendAreaData[1]);
		sendArea.setDistrict(sendAreaData[2]);

		Area recArea = new Area();
		recArea.setProvince(recAreaData[0]);
		recArea.setCity(recAreaData[1]);
		recArea.setDistrict(recAreaData[2]);

		// 将Area存进model
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		
		//关联当前客户
		Customer customer = (Customer)ServletActionContext.getRequest().getSession().getAttribute("customer");
		model.setCustomer_id(customer.getId());
		
		// 利用webservice将model传给bos_management保存
		WebClient.create(Constants.BOS_MANAGEMENT_URL + "/bos_management/services/orderService/addOrder")
				.type(MediaType.APPLICATION_JSON).post(Order.class);
		
		return SUCCESS;
	}
}
