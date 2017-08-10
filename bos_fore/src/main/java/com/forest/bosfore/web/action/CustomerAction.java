package com.forest.bosfore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bosfore.web.action.common.BaseAction;
import com.forest.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {
	@Action(value = "customer_sendSms")
	public String sendSms() {
		// 生成验证码.注意此处使用randomNumeric方法，不然生成的是奇怪的汉字
		String checkCode = RandomStringUtils.randomNumeric(4);
		// 与手机号一起保存至session中
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), checkCode);
		// 编辑短信内容
		String msg = "您好，本次获取的验证码为" + checkCode;
		System.out.println(msg);
		// 发送短信，获取对应的结果
		String result = "000/yyy";
		// 根据结果，判断是否发送成功
		if (result.startsWith("000")) {
			// 发送成功
			return NONE;
		} else {
			throw new RuntimeException("发送失败，信息码：" + result);
		}
	}

	private String checkCode;

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	@Action(value = "customer_register", results = {
			@Result(name = "success", type = "redirect", location = "/signup-success.html"),
			@Result(name = "input", type = "redirect", location = "/signup.html") })
	public String register() {
		// 判断checkCode对不对
		String checkCodeStr = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(model.getTelephone());
		if (!checkCode.equals(checkCodeStr)) {
			// 不对--->返回注册页面
			System.out.println("短信验证错误");
			return INPUT;
		}
		// 对--->使用webService将信息存进去
		WebClient.create("http://localhost:9002/crm_management/services/customerService/regist").type(MediaType.APPLICATION_JSON)
				.post(model);
		System.out.println("注册成功");
		return SUCCESS;
	}
}
