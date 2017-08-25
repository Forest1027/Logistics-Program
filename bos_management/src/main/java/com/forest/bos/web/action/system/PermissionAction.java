package com.forest.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.system.Permission;
import com.forest.bos.service.system.IPermissionService;
import com.forest.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission>{
	@Autowired
	private IPermissionService permissionService;
	
	@Action(value="permission_list",results={@Result(name="success",type="json")})
	public String list() {
		List<Permission> permissions = permissionService.finaAll();
		ActionContext.getContext().getValueStack().push(permissions);
		return SUCCESS;
	}
	
	@Action(value="permission_add",results={@Result(name="success",type="redirect",location="/pages/system/permission.html")})
	public String add() {
		permissionService.add(model);
		return SUCCESS;
	}
}
