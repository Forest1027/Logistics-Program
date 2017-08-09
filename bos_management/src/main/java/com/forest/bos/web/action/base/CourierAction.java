package com.forest.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
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

import com.forest.bos.domain.base.Courier;
import com.forest.bos.service.base.ICourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Actions
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
	private Courier courier = new Courier();

	@Autowired
	private ICourierService cs;
	
	//分页
	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	//批量删除
	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}

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

	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		System.out.println("pageQuery-----");
		// 将两个属性存进pageable（pagerequest）
		Pageable pageable = new PageRequest(page - 1, rows);
		// 编写条件
		Specification<Courier> specification = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				/*
				 * 参数说明 Root 用于获取属性字段， CriteriaQuery可以用于简单条件查询，
				 * CriteriaBuilder用于构造复杂条件查询
				 */
				/*
				 * courierNum:1 standard.name company type
				 */
				// 创建集合，接收条件
				List<Predicate> list = new ArrayList<>();
				// 判断表单中的那四个条件存不存在
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					// 员工号精确查询
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					list.add(p1);
				}
				if (StringUtils.isNotBlank(courier.getCompany())) {
					// 公司 模糊查询
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+courier.getCompany()+"%");
					list.add(p2);
				}
				if (StringUtils.isNoneBlank(courier.getType())) {
					// 快递员类型精确查询
					Predicate p3 = cb.like(root.get("type").as(String.class), courier.getType());
					list.add(p3);
				}
				// 多表查询需要先关联到对象
				Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER);
				if (courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {
					// 名字 模糊查询
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class), "%"+courier.getStandard().getName()+"%");
					list.add(p4);
				}
				Predicate predicate = cb.and(list.toArray(new Predicate[]{}));
				return predicate;
			}
			
		};

		// 调用业务层的findAll方法
		Page<Courier> page = cs.pageQuery(specification,pageable);
		// 将查询到的数据存入值栈
		Map<String, Object> result = new HashMap<>();
		result.put("total", page.getTotalElements());
		result.put("rows", page.getContent());
		ActionContext.getContext().getValueStack().push(result);

		return SUCCESS;
	}
	
	@Action(value="courier_delBatch",results={@Result(name="success",type="redirect",location="/pages/base/courier.html")})
	public String delBatch() {
		//获取前台数据，切割字符串，还原id
		String[] idArr = ids.split(",");
		//调用后台，批量删除
		cs.delBatch(idArr);
		return SUCCESS;
	}
	
	@Action(value="courier_findnoassociation",results={@Result(name="success",type="json")})
	public String findNoAssociation() {
		List<Courier> couriers = cs.findNoAssociation();
		ActionContext.getContext().getValueStack().push(couriers);
		return SUCCESS;
	}
}
