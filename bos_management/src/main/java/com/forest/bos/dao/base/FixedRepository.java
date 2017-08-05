package com.forest.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.forest.bos.domain.base.FixedArea;

public interface FixedRepository extends JpaRepository<FixedArea, String>,JpaSpecificationExecutor<FixedArea>{

}
