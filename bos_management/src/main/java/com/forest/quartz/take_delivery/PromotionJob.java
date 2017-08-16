package com.forest.quartz.take_delivery;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.forest.bos.service.take_delivery.IPromotionService;

public class PromotionJob implements Job{
	@Autowired
	private IPromotionService ps;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("-----hahaha-----");
		ps.updateStatus(new Date());
	}

}
