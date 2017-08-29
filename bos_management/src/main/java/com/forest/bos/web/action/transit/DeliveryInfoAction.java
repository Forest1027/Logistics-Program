package com.forest.bos.web.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.transit.DeliveryInfo;
import com.forest.bos.service.transit.IDeliveryInfoService;
import com.forest.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class DeliveryInfoAction extends BaseAction<DeliveryInfo> {
	@Autowired
	private IDeliveryInfoService deliveryInfoService;

	private String transitId;

	public void setTransitId(String transitId) {
		this.transitId = transitId;
	}

	@Action(value = "delivery_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/transit/transitinfo.html") })
	public String save() {
		deliveryInfoService.save(transitId,model);
		return SUCCESS;
	}
}
