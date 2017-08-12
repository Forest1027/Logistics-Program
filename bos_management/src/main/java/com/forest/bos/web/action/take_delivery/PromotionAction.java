package com.forest.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.forest.bos.domain.take_delivery.Promotion;
import com.forest.bos.service.take_delivery.IPromotionService;
import com.forest.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
	private File titleImgFile;
	private String titleImgFileFileName;
	private String titleImgFileContentType;

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	public void setTitleImgFileContentType(String titleImgFileContentType) {
		this.titleImgFileContentType = titleImgFileContentType;
	}

	@Autowired
	private IPromotionService ps;

	@Action(value = "promotion_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/take_delivery/promotion.html") })
	public String save() throws IOException {
		// 获取绝对路径
		String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
		// 获取相对路径
		String contextPath = ServletActionContext.getRequest().getContextPath() + "/upload/";
		// 生成随机文件名// 获取扩展名
		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		String randomFileName = uuid + ext;
		// 保存上传来的文件
		FileUtils.copyFile(titleImgFile, new File(realPath + "/" + randomFileName));
		// 将路径存进model
		model.setTitleImg(contextPath + randomFileName);
		// 完成数据的保存
		ps.save(model);
		return SUCCESS;
	}

	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		//创建分页查询对象
		Pageable pageable = new PageRequest(page - 1, rows);
		//调用业务层完成查询
		Page<Promotion> pageData = ps.findPageData(pageable);
		pushDataToValueStack(pageData);
		return SUCCESS;
	}
}
