package com.forest.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.take_delivery.Order;
import com.forest.bos.service.take_delivery.IOrderService;
import com.forest.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order>{
	@Autowired
	private IOrderService oService;
	
	@Action(value="order_findByOrderNum",results={@Result(name="success",type="json")})
	public String findByOrderNum() {
		//查询订单
		Order order = oService.findByOrderNum(model.getOrderNum());
		//记录结果
		Map<String,Object> result = new HashMap<>();
		if (order!=null) {
			result.put("success", true);
			result.put("orderData", order);
		}else {
			result.put("success", false);
		}
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
