package com.kehutanan.rm.serahterima.repository;

import com.kehutanan.rm.serahterima.model.BastPusat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BastPusatRepository extends JpaRepository<BastPusat, Long>, JpaSpecificationExecutor<BastPusat> {

}