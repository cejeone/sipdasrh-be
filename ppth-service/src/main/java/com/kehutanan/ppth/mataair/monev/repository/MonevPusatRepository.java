package com.kehutanan.ppth.mataair.monev.repository;

import com.kehutanan.ppth.mataair.monev.model.MonevPusat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("tmkhMonevPusatRepository")
public interface MonevPusatRepository extends JpaRepository<MonevPusat, Long>, JpaSpecificationExecutor<MonevPusat> {

}