package com.forest.bos.service.base.imp;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.base.CourierRepository;
import com.forest.bos.domain.base.Courier;
import com.forest.bos.service.base.ICourierService;

@Service
@Transactional
public class CourierServiceImp implements ICourierService {
	@Autowired
	private CourierRepository repository;

	@Override
	public void save(Courier courier) {
		repository.save(courier);
	}

	@Override
	public Page<Courier> pageQuery(Specification<Courier> specification, Pageable pageable) {
		Page<Courier> page = repository.findAll(specification, pageable);
		return page;
	}

	@Override
	public void delBatch(String[] idArr) {
		// 字符串-->数字
		for (String idStr : idArr) {
			Integer id = Integer.parseInt(idStr);
			repository.updateDeltag(id);
		}
	}

	@Override
	public List<Courier> findNoAssociation() {
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				return p;
			}
		};
		List<Courier> couriers = repository.findAll(specification);
		return couriers;
	}
}
