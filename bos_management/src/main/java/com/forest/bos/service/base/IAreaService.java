package com.forest.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.forest.bos.domain.base.Area;

public interface IAreaService {

	public void batchImport(List<Area> areas);

	public Page<Area> findAll(Specification<Area> specification, Pageable pageable);

}
