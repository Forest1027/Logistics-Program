package com.forest.bosfore.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

//将类定义为抽象类，则不能实例化此对象
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	// 模型驱动。此处使用protected的原因---让子类可见此属性
	// 此处不能直接new T()。因为泛型在编译的过程中会被擦除。
	protected T model;

	@Override
	public T getModel() {
		return model;
	}

	// model实例化---定义构造方法，则子类在实例化的过程中必然调用此构造方法。即可实例化model
	public BaseAction() {
		// 获取BaseAction<Area>
		Type genericSuperclass = this.getClass().getGenericSuperclass();

		// 获取第一个泛型参数。此处为Area类型
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];

		try {
			// 实例化获取的类型
			model = modelClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("模型构造失败");
		}
	}

	// 分页
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void pushDataToValueStack(Page<T> page) {
		// 将数据封装到map集合，存进值栈
		Map<String, Object> result = new HashMap<>();
		result.put("total", page.getTotalElements());
		result.put("rows", page.getContent());
		
		ActionContext.getContext().getValueStack().push(result);
	}

}