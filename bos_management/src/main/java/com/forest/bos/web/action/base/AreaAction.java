package com.forest.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import com.forest.bos.domain.base.Area;
import com.forest.bos.service.base.IAreaService;
import com.forest.bos.utils.PinYin4jUtils;
import com.forest.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {

	@Autowired
	private IAreaService as;

	// excel上传
	private File file;
	private String contentType;
	private String fileName;

	public void setFile(File file) {
		this.file = file;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Action(value = "area_batchImport")
	public String batchImport() throws Exception {
		// 创建集合，用于接收area对象
		List<Area> areas = new ArrayList<>();
		// 创建hssfworkbook对象
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
		// 获取sheet
		HSSFSheet sheet = workbook.getSheetAt(0);
		// 遍历sheet获取rows
		for (Row row : sheet) {
			// 跳过首行
			if (row.getRowNum() == 0) {
				continue;
			}
			// 跳过空行
			if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			// 从rows中取出cell，存入area对象中
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());

			// 基于pinyin4j生成简码和城市编码
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			// 将最后一个字切掉(“省”，“市”)
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			// 简码--省市区字符串的拼音首字母
			String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
			StringBuilder sb = new StringBuilder();
			for (String head : headArray) {
				sb.append(head);
			}
			String shortcode = sb.toString();
			// 城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			// 将简码和城市编码存入area中
			area.setShortcode(shortcode);
			area.setCitycode(citycode);
			// area对象存入集合
			areas.add(area);
		}
		// 将集合传给业务层--->存数据
		as.batchImport(areas);
		return SUCCESS;
	}

	@Action(value = "area_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		System.out.println("findall--------------");
		// 分页数据
		Pageable pageable = new PageRequest(page-1, rows);
		// 添加条件
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 创建集合，接收限制
				List<Predicate> list = new ArrayList<>();
				// 表单中province，city，district的数据是否存在
				if (StringUtils.isNotBlank(model.getProvince())) {
					Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
					list.add(p1);
				}
				if (StringUtils.isNotBlank(model.getCity())) {
					Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
					list.add(p2);
				}
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
					list.add(p3);
				}
				return cb.and(list.toArray(new Predicate[]{}));
			}
		};
		Page<Area> page = as.findAll(specification,pageable);
		// 将数据封装到map集合，存进值栈
		pushDataToValueStack(page);
		return SUCCESS;
	}

}
