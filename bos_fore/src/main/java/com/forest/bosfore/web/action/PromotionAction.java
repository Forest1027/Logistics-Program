package com.forest.bosfore.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.common.Constants;
import com.forest.bos.domain.take_delivery.PageBean;
import com.forest.bos.domain.take_delivery.Promotion;
import com.forest.bosfore.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 此处用webservice,因为promotion相关的数据库是通过bos_management操作的
		PageBean pageBean = WebClient
				.create("http://localhost:8080/bos_management/services/promotionService/pageQuery?page=" + page
						+ "&rows=" + rows)
				.accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}

	@Action(value = "promotion_showDetail")
	public String showDetail() throws IOException, TemplateException {
		// 获取存放静态文件的文件夹路径
		String realPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
		// 获取到该路径对应的文件
		File targetFile = new File(realPath + "/" + model.getId() + ".html");
		// 判断文件是否存在
		if (!targetFile.exists()) {
			// 不存在
			// 选择版本
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
			// 设置模板路径
			configuration.setDirectoryForTemplateLoading(
					new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates")));
			// 获取模板
			Template template = configuration.getTemplate("promotion_detail.ftl");
			// 获取数据
			Promotion promotion = WebClient.create(Constants.BOS_MANAGEMENT_URL+"/bos_management/services/promotionService/promotion/"+model.getId()).accept(MediaType.APPLICATION_JSON).get(Promotion.class);
			// 生成文件
			Map<String,Object> map = new HashMap<>();
			map.put("promotion", promotion);
			template.process(map, new OutputStreamWriter(new FileOutputStream(targetFile),"utf-8"));
		}
		// 存在-->将文件中的内容以输出流的形式写到页面上
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		FileUtils.copyFile(targetFile, ServletActionContext.getResponse().getOutputStream());
		return NONE;
	}
}
