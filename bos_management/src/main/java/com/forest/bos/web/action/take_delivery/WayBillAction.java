package com.forest.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.take_delivery.WayBill;
import com.forest.bos.service.take_delivery.IWayBillService;
import com.forest.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {
	// 用来记录日志
	private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);

	@Autowired
	private IWayBillService wbService;

	@Action(value = "waybill_save", results = { @Result(name = "success", type = "json") })
	public String save() {
		// 去除没有id的order对象
		if (model.getOrder() != null && (model.getOrder().getId() == null || model.getOrder().getId() == 0)) {
			model.setOrder(null);
		}
		// 创建map集合记录保存状态
		Map<String, Object> map = new HashMap<>();
		try {
			wbService.save(model);
			map.put("success", true);
			map.put("msg", "保存成功");

			LOGGER.info("保存运单成功，运单号：" + model.getWayBillNum());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "保存失败");

			LOGGER.info("保存运单失败，运单号：" + model.getWayBillNum());
		}
		ServletActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}

	@Action(value = "waybill_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 加了一个降序查询
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "wayBillNum")));
		// 调用业务层查询
		Page<WayBill> page = wbService.pageQuery(model,pageable);
		// 将数据存进值栈
		pushDataToValueStack(page);
		return SUCCESS;
	}

	@Action(value = "waybill_findByWayBillNum", results = { @Result(name = "success", type = "json") })
	public String findByWayBillNum() {
		WayBill wayBill = wbService.findByWayBillNum(model.getWayBillNum());
		Map<String, Object> result = new HashMap<>();
		if (wayBill != null) {
			result.put("success", true);
			result.put("wayBillData", wayBill);
		} else {
			result.put("success", false);
		}
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
