package com.kehutanan.rm.master.repository;

import com.kehutanan.rm.master.model.Bpdas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BpdasRepository extends JpaRepository<Bpdas, Long>, JpaSpecificationExecutor<Bpdas> {
    // Repository methods
}