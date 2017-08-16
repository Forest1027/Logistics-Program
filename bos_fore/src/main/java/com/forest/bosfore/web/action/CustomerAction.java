package com.forest.bosfore.web.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.common.Constants;
import com.forest.bosfore.web.action.common.BaseAction;
import com.forest.bosfore.web.utils.MailUtils;
import com.forest.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Action(value = "customer_sendSms")
	public String sendSms() {
		// 生成验证码.注意此处使用randomNumeric方法，不然生成的是奇怪的汉字
		String checkCode = RandomStringUtils.randomNumeric(4);
		// 与手机号一起保存至session中
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), checkCode);
		// 将信息发送给activemq
		jmsTemplate.send("bos_sms", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// 要传两个参数，因此要使用mapmessage
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", model.getTelephone());
				mapMessage.setString("checkcode", checkCode);
				return mapMessage;
			}
		});

		/*
		 * // 发送短信，获取对应的结果 String result = "000/yyy"; // 根据结果，判断是否发送成功 if
		 * (result.startsWith("000")) { // 发送成功 return NONE; } else { throw new
		 * RuntimeException("发送失败，信息码：" + result); }
		 */
		return NONE;
	}

	private String checkCode;

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	@Autowired
	private RedisTemplate<String, String> template;

	@Action(value = "customer_register", results = {
			@Result(name = "success", type = "redirect", location = "/signup-success.html"),
			@Result(name = "input", type = "redirect", location = "/signup.html") })
	public String register() {
		// 短信验证
		// 判断checkCode对不对
		String checkCodeStr = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(model.getTelephone());
		if (!checkCode.equals(checkCodeStr)) {
			// 不对--->返回注册页面
			System.out.println("短信验证错误");
			return INPUT;
		}
		// 对--->使用webService将信息存进去
		WebClient.create("http://localhost:9002/crm_management/services/customerService/regist")
				.type(MediaType.APPLICATION_JSON).post(model);
		// 发送激活邮件
		// 生成激活码
		String activeCode = RandomStringUtils.randomNumeric(32);
		// 将激活码和用户手机号存入redis---之所以存手机号，是因为此时手机号已经验证成功，手机号成为了类似于id的东西
		template.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);
		// 设置邮件内容
		String content = "你好，请点击以下链接完成邮箱验证：<a href='" + MailUtils.activeUrl + "?telephone=" + model.getTelephone()
				+ "&activeCode=" + activeCode + "'>邮箱绑定链接</a>";
		// 发送邮件
		MailUtils.sendMail("激活邮件", content, model.getEmail(), activeCode);
		System.out.println("注册成功");
		return SUCCESS;
	}

	private String activeCode;

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	@Action(value = "customer_activeMail")
	public String activeMail() throws IOException {
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		// 判断激活码是否有效
		String activeCodeStr = template.opsForValue().get(model.getTelephone());
		if (activeCodeStr == null || !activeCode.equals(activeCodeStr)) {
			// 无效
			ServletActionContext.getResponse().getWriter().println("激活码无效，请登录系统，重新绑定邮箱");
		} else {
			// 有效
			// 防止重复绑定
			// 通过webService查到customer的信息
			Customer customer = WebClient
					.create("http://localhost:9002/crm_management/services/customerService/regist/telephone/"
							+ model.getTelephone())
					.accept(MediaType.APPLICATION_JSON).get(Customer.class);
			// 判断邮箱激活状态
			if (customer.getType() == null || customer.getType() != 1) {
				// 未激活--->调用webclient
				WebClient
						.create(Constants.CRM_MANAGEMENT_URL
								+ "/crm_management/services/customerService/regist/updateType/" + model.getTelephone())
						.put(null);
				ServletActionContext.getResponse().getWriter().println("绑定成功！");
			} else {
				// 已激活
				ServletActionContext.getResponse().getWriter().println("已经绑定，请勿重复绑定！");
			}
			// 移除redis中的此条激活码
			template.delete(model.getTelephone());
		}
		return NONE;
	}

	@Action(value = "customer_login", results = {
			@Result(name = "success", type = "redirect", location = "/index.html#/myhome"),
			@Result(name = "login", type = "redirect", location = "login.html") })
	public String login() {
		// 将帐户名和密码传递给crm，查找customer
		Customer customer = WebClient
				.create(Constants.CRM_MANAGEMENT_URL + "/crm_management/services/customerService/login?telephone="
						+ model.getTelephone() + "&password=" + model.getPassword())
				.accept(MediaType.APPLICATION_JSON).get(Customer.class);
		// 如果customer为空-->登录失败
		if (customer == null) {
			return LOGIN;
		} else {
			// 否则-->登录成功
			return SUCCESS;
		}
	}
}
