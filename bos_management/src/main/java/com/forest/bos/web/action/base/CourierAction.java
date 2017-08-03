package com.forest.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.base.Courier;
import com.forest.bos.service.base.ICourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
	private Courier courier = new Courier();

	@Autowired
	private ICourierService cs;

	@Override
	public Courier getModel() {
		// TODO Auto-generated method stub
		return courier;
	}

	@Action(value = "courier_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/base/courier.html") })
	public String save() {
		System.out.println(courier);
		cs.save(courier);
		return SUCCESS;
	}
}
