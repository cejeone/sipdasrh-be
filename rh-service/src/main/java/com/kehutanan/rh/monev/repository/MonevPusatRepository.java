package com.kehutanan.rh.monev.repository;

import com.kehutanan.rh.monev.model.MonevPusat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MonevPusatRepository extends JpaRepository<MonevPusat, Long>, JpaSpecificationExecutor<MonevPusat> {

}