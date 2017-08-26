package com.forest.bos.service.take_delivery.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.index.WayBillIndexRepository;
import com.forest.bos.dao.take_delivery.WayBillRepository;
import com.forest.bos.domain.take_delivery.WayBill;
import com.forest.bos.service.take_delivery.IWayBillService;

@Service
@Transactional
public class WayBillServiceImp implements IWayBillService {
	@Autowired
	private WayBillRepository repository;
	@Autowired
	private WayBillIndexRepository indexRepository;

	@Override
	public void save(WayBill wayBill) {
		// 实现查询运单号是否存在。以便防止由于wayBill的id 为空，而只能实现增加，不能实现update的情况
		WayBill persistWayBill = repository.findByWayBillNum(wayBill.getWayBillNum());
		if (persistWayBill == null || persistWayBill.getId() == null) {
			// 运单不存在
			wayBill.setWayBillType("1");
			// 保存运单
			repository.save(wayBill);
			// 保存索引
			indexRepository.save(wayBill);
		} else {
			try {
				// 运单存在
				if("1".equals(wayBill.getWayBillType())) {
					// 将waybill的属性给到persist
					Integer id = persistWayBill.getId();
					BeanUtils.copyProperties(wayBill, persistWayBill);
					persistWayBill.setId(id);
					// 保存运单
					repository.save(persistWayBill);
					// 保存索引
					indexRepository.save(wayBill);
				}else {
					throw new RuntimeException("运单已发出，无法再修改");
				}
			} catch (BeansException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public Page<WayBill> pageQuery(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return repository.findByWayBillNum(wayBillNum);
	}

	@Override
	public Page<WayBill> pageQuery(WayBill model, Pageable pageable) {
		if (StringUtils.isBlank(model.getWayBillNum()) && StringUtils.isBlank(model.getSendAddress())
				&& StringUtils.isBlank(model.getRecAddress()) && StringUtils.isBlank(model.getSendProNum())
				&& (model.getSignStatus() == null || model.getSignStatus() == 0)) {
			return repository.findAll(pageable);
		}
		BoolQueryBuilder query = new BoolQueryBuilder();
		// 添加条件
		if (StringUtils.isNoneBlank(model.getWayBillNum())) {
			// 等值查询
			QueryBuilder termQueryBuilder = new TermQueryBuilder("wayBillNum", model.getWayBillNum());
			query.must(termQueryBuilder);
		}
		if (StringUtils.isNoneBlank(model.getSendAddress())) {
			// 模糊查询
			// 情况一：输入的值是词条的一部分，因此使用模糊查询
			QueryBuilder wildcardQueryBuilder1 = new WildcardQueryBuilder("sendAddress",
					"*" + model.getSendAddress() + "*");
			// 情况二：输入的值比较完整，包含词条-->先切分
			QueryBuilder defaultOperator = new QueryStringQueryBuilder(model.getSendAddress()).field("sendAddress")
					.defaultOperator(Operator.AND);

			// 两种情况取或
			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
			boolQueryBuilder.should(wildcardQueryBuilder1);
			boolQueryBuilder.should(defaultOperator);

			query.must(boolQueryBuilder);
		}
		if (StringUtils.isNoneBlank(model.getRecAddress())) {
			// 模糊查询
			QueryBuilder wildcardQueryBuilder2 = new WildcardQueryBuilder("recAddress",
					"*" + model.getRecAddress() + "*");
			// 情况二：输入的值比较完整，包含词条-->先切分
			QueryBuilder defaultOperator = new QueryStringQueryBuilder(model.getRecAddress()).field("recAddress")
					.defaultOperator(Operator.AND);

			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
			boolQueryBuilder.should(wildcardQueryBuilder2);
			boolQueryBuilder.should(defaultOperator);
			query.must(boolQueryBuilder);
		}
		if (StringUtils.isNoneBlank(model.getSendProNum())) {
			QueryBuilder termQueryBuilder2 = new TermQueryBuilder("sendProNum", model.getSendProNum());
			query.must(termQueryBuilder2);
		}
		if (model.getSignStatus() != null && model.getSignStatus() != 0) {
			QueryBuilder termQueryBuilder3 = new TermQueryBuilder("signStatus", model.getSignStatus());
			query.must(termQueryBuilder3);
		}
		SearchQuery nativeSearchQuery = new NativeSearchQuery(query);
		nativeSearchQuery.setPageable(pageable);
		return indexRepository.search(nativeSearchQuery);
	}

	@Override
	public void syncIndex() {
		// TODO Auto-generated method stub
		List<WayBill> wayBills = repository.findAll();
		indexRepository.save(wayBills);
	}

}
