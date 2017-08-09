package com.forest.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.forest.bos.domain.base.FixedArea;

public interface IFixedService {

	public void save(FixedArea model);

	public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable);

	public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId);

}
