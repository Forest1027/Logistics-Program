package com.forest.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.system.Menu;
import com.forest.bos.service.system.IMenuService;
import com.forest.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu>{
	@Autowired
	private IMenuService menuService;
	
	@Action(value="menu_list",results={@Result(name="success",type="json")})
	public String list() {
		List<Menu> menu = menuService.findAll();
		ActionContext.getContext().getValueStack().push(menu);
		return SUCCESS;
	}
	
	@Action(value="menu_add",results={@Result(name="success",type="redirect",location="/pages/system/menu.html")})
	public String add() {
		menuService.add(model);
		return SUCCESS;
	}
}
