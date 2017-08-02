package com.forest.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.forest.bos.domain.base.Standard;

public interface IStandardService {
	public void save(Standard standard);

	public Page<Standard> findPageData(Pageable pageable);

	public List<Standard> findAll();
}
