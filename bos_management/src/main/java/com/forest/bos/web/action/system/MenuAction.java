package com.forest.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.system.Menu;
import com.forest.bos.domain.system.User;
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
	
	@Action(value="menu_showmenu",results={@Result(name="success",type="json")})
	public String showMenu() {
		//拿到当前登录的用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		//根据用户查询对应的菜单
		List<Menu> menus = menuService.findByUser(user);
		ActionContext.getContext().getValueStack().push(menus);
		return SUCCESS;
	}
}
