package com.forest.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.base.TakeTime;
import com.forest.bos.service.base.ITakeTimeService;
import com.forest.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TakeTimeAction extends BaseAction<TakeTime>{
	@Autowired
	private ITakeTimeService ts;
	
	@Action(value="taketime_findAll",results={@Result(name="success",type="json")})
	public String findAll() {
		List<TakeTime> list = ts.finAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
