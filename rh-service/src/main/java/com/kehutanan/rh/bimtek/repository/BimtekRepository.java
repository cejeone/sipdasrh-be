package com.kehutanan.rh.bimtek.repository;

import com.kehutanan.rh.bimtek.model.Bimtek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BimtekRepository extends JpaRepository<Bimtek, Long>, JpaSpecificationExecutor<Bimtek> {

}