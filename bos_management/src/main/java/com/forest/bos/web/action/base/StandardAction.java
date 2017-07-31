package com.forest.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.base.Standard;
import com.forest.bos.service.base.IStandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("struts-default")
@Namespace("/")
@Actions
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{
	private Standard standard = new Standard();
	
	@Autowired
	private IStandardService ss;

	@Override
	public Standard getModel() {
		// TODO Auto-generated method stub
		return standard;
	}
	
	@Action(value="standard_save",results={@Result(name="success",type="redirect",location="/pages/base/standard.html")})
	public String save() {
		System.out.println(standard);
		ss.save(standard);
		return SUCCESS;
	}
	
	/*@Action(value="standard_pageQuery",results={@Result(name="success",type="redirect",location="/pages/base/standard.html")})
	public String query() {
		System.out.println("query");
		return SUCCESS;
	}
	*/
}
