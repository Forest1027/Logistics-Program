package com.forest.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.forest.crm.domain.Customer;

@Produces("*/*")
public interface ICustomerService {
	// 查询所有未关联客户
	@Path("/findnoassosncustomer")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findNoAssosnCustomer();

	// 查询所有已关联客户
	@Path("/findassosncustomer/{fixedareaid}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findAssosnCustomer(@PathParam("fixedareaid") String fixedAreaId);

	// 将客户(多)关联到定区(一)
	@Path("/assosncustomertofixedarea")
	@PUT
	public void assosnCustomerToFixedArea(
			@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId);
}
