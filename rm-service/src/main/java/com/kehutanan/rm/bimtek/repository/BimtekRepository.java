package com.kehutanan.rm.bimtek.repository;

import com.kehutanan.rm.bimtek.model.Bimtek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BimtekRepository extends JpaRepository<Bimtek, Long>, JpaSpecificationExecutor<Bimtek> {

}