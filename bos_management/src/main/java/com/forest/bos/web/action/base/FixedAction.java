package com.forest.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.base.FixedArea;
import com.forest.bos.service.base.IFixedService;
import com.forest.bos.web.action.common.BaseAction;
import com.forest.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAction extends BaseAction<FixedArea> {
	@Autowired
	private IFixedService fs;

	@Action(value = "fixedArea_findNoAssosnCustomers", results = { @Result(name = "success", type = "json") })
	public String findNoAssosnCustomers() {
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:9002/crm_management/services/customerService/findnoassosncustomer")
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

	@Action(value = "fixedArea_findAssosnCustomers", results = { @Result(name = "success", type = "json") })
	public String findAssosnCustomer() {
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:9002/crm_management/services/customerService/findassosncustomer/"
						+ model.getId())
				.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

	private String[] customerIds;

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	@Action(value = "fixedArea_assosnCustomerToFixedArea", results = {
			@Result(name = "success", type = "redirect", location = "/pages/base/fixed_area.html") })
	public String assosnCustomerToFixedArea() {
		String customerIdStr = StringUtils.join(customerIds, ",");
		//此处没有传也没有接收json/xml，所以不需要accept或type，直接put就好
		//此处将customerIdStr变成了字符串，即使是null的时候也是字符串"null"
		WebClient
				.create("http://localhost:9002/crm_management/services/customerService/assosncustomertofixedarea?customerIdStr="
						+ customerIdStr + "&fixedAreaId=" + model.getId())
				.put(null);
		return SUCCESS;
	}

	@Action(value = "fixed_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/base/fixed_area.html") })
	public String save() {
		fs.save(model);
		return SUCCESS;
	}

	@Action(value = "fixed_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		System.out.println("查询开始了");
		// 分页
		Pageable pageable = new PageRequest(page - 1, rows);
		// 条件
		Specification<FixedArea> specification = new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// id courier.company subareaName
				List<Predicate> list = new ArrayList<>();
				if (StringUtils.isNotBlank(model.getId())) {
					Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
					list.add(p1);
				}

				if (StringUtils.isNotBlank(model.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), model.getCompany());
					list.add(p2);
				}

				return cb.and(list.toArray(new Predicate[] {}));
			}
		};
		// 调用业务层
		Page<FixedArea> page = fs.findPageData(specification, pageable);
		pushDataToValueStack(page);
		return SUCCESS;
	}
	
	private Integer courierId;
	private Integer takeTimeId;
	
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	@Action(value="fixedArea_associationCourierToFixedArea",results={@Result(name="success",type="redirect",location="/pages/base/fixed_area.html")})
	public String associationCourierToFixedArea() {
		//调用业务层
		fs.associationCourierToFixedArea(model,courierId,takeTimeId);
		return SUCCESS;
	}
}
