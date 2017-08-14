package com.forest.bos.service.take_delivery;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.forest.bos.domain.take_delivery.PageBean;
import com.forest.bos.domain.take_delivery.Promotion;

public interface IPromotionService {

	public void save(Promotion model);

	public Page<Promotion> findPageData(Pageable pageable);

	@Path("/pageQuery")
	@GET
	@Produces({ "application/xml", "application/json" })
	public PageBean<Promotion> findPageData(@QueryParam("page") int page, @QueryParam("rows") int rows);
	
	@Path("/promotion/{id}")
	@GET
	@Produces({"application/xml", "application/json"})
	public Promotion findById(@PathParam("id") Integer id);

	public void updateStatus(Date date);
	
}
