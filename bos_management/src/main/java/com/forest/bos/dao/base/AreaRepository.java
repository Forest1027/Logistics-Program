package com.forest.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.forest.bos.domain.base.Area;

public interface AreaRepository extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area>{

	public Area findByProvinceAndCityAndDistrict(String province, String city, String district);

}
