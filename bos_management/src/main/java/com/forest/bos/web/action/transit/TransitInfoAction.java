package com.forest.bos.web.action.transit;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.transit.TransitInfo;
import com.forest.bos.service.transit.ITransitinfoService;
import com.forest.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TransitInfoAction extends BaseAction<TransitInfo> {
	@Autowired
	private ITransitinfoService transitinfoService;

	private String wayBillIds;

	public void setWayBillIds(String wayBillIds) {
		this.wayBillIds = wayBillIds;
	}

	@Action(value="transit_create",results={@Result(name="success",type="json")})
	public String create() {
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			transitinfoService.create(wayBillIds);
			result.put("success", true);
			result.put("msg", "开启中转成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "开启中转失败，异常："+e.getMessage());
		}
		ActionContext.getContext().getValueStack().push(result);;
		return SUCCESS;
	}
	
	@Action(value="transit_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery() {
		Pageable pageable = new PageRequest(page-1, rows);
		Page<TransitInfo> pageData = transitinfoService.findAll(pageable);
		pushDataToValueStack(pageData);
		return SUCCESS;
	}
}
