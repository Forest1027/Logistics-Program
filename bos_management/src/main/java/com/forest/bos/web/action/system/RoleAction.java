package com.forest.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.system.Role;
import com.forest.bos.service.system.IRoleService;
import com.forest.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

import org.apache.struts2.convention.annotation.Result;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {
	@Autowired
	private IRoleService roleService;

	@Action(value = "role_list", results = { @Result(name = "success", type = "json") })
	public String list() {
		List<Role> roles = roleService.findAll();
		ActionContext.getContext().getValueStack().push(roles);
		return SUCCESS;
	}

	private String[] permissionIds;
	private String menuIds;

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	@Action(value = "role_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/system/role.html") })
	public String save() {
		roleService.save(model,permissionIds,menuIds);
		return SUCCESS;
	}
}
