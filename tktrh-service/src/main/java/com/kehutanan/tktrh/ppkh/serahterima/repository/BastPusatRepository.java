package com.kehutanan.tktrh.ppkh.serahterima.repository;

import com.kehutanan.tktrh.ppkh.serahterima.model.BastPusat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("ppkhBastPusatRepository")
public interface BastPusatRepository extends JpaRepository<BastPusat, Long>, JpaSpecificationExecutor<BastPusat> {

}