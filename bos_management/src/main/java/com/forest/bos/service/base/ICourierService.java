package com.forest.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.forest.bos.domain.base.Courier;

public interface ICourierService {

	public void save(Courier courier);

	public Page<Courier> pageQuery(Specification<Courier> specification,Pageable pageable);

}
