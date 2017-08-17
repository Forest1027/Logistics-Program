package com.forest.bos.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.forest.bos.domain.take_delivery.Order;

@Produces("*/*")
public interface IOrderService {
	
	@Path("/addOrder")
	@POST
	@Consumes({ "application/xml", "application/json" })
	public void addOrder(Order order);
}
