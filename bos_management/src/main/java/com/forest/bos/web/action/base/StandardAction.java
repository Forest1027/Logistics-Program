package com.forest.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.base.Standard;
import com.forest.bos.service.base.IStandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Actions
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {
	private Standard standard = new Standard();

	@Autowired
	private IStandardService ss;

	@Override
	public Standard getModel() {
		// TODO Auto-generated method stub
		return standard;
	}

	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Action(value = "standard_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/base/standard.html") })
	public String save() {
		System.out.println(standard);
		ss.save(standard);
		return SUCCESS;
	}

	@Action(value = "standard_pageQuery", results = { @Result(name = "success", type = "json") })
	public String query() {
		// 将接收的两个参数传递给业务层，调用业务层方法获取数据
		// PageRequest是Pageable的实现类--->下面是多态
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Standard> pageData = ss.findPageData(pageable);
		// 将数据转换成json数据，存入值栈
		Map<String, Object> result = new HashMap<>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	@Action(value = "standard_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		// 获取数据
		List<Standard> standards = ss.findAll();
		// 转换成json数据
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}

}
