package com.forest.bos.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forest.bos.domain.transit.InOutStorageInfo;

public interface InOutStorageRepository extends JpaRepository<InOutStorageInfo, Integer>{

}
