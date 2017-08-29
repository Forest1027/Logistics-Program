package com.forest.bos.web.action.transit;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.forest.bos.dao.take_delivery.WayBillRepository;
import com.forest.bos.domain.take_delivery.WayBill;
import com.forest.bos.service.take_delivery.IWayBillService;
import com.forest.bos.utils.FileUtils;
import com.forest.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ReportAction extends BaseAction<WayBill> {
	@Autowired
	private IWayBillService wayBillService;

	@Action(value = "report_exportExls")
	public String exportExls() throws IOException {
		// 查询所有数据
		List<WayBill> wayBills = wayBillService.findWayBills(model);

		// 生成Excel文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet sheet = hssfWorkbook.createSheet("运单数据");
		// 表头
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("运单号");
		headRow.createCell(1).setCellValue("寄件人");
		headRow.createCell(2).setCellValue("寄件人电话");
		headRow.createCell(3).setCellValue("寄件人地址");
		headRow.createCell(4).setCellValue("收件人");
		headRow.createCell(5).setCellValue("收件人电话");
		headRow.createCell(6).setCellValue("收件人地址");
		// 表格数据
		for (WayBill wayBill : wayBills) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(wayBill.getWayBillNum());
			dataRow.createCell(1).setCellValue(wayBill.getSendName());
			dataRow.createCell(2).setCellValue(wayBill.getSendMobile());
			dataRow.createCell(3).setCellValue(wayBill.getSendAddress());
			dataRow.createCell(4).setCellValue(wayBill.getRecName());
			dataRow.createCell(5).setCellValue(wayBill.getRecMobile());
			dataRow.createCell(6).setCellValue(wayBill.getRecAddress());
		}
		// 下载导出
		ServletActionContext.getResponse().setContentType("application/vnd.ms-excel");
		String filename = "运单数据.xls";
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment;filename=" + filename);

		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		hssfWorkbook.write(outputStream);
		
		hssfWorkbook.close();
		return NONE;
	}
}
