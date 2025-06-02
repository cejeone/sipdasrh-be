package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.Bpdas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BpdasRepository extends JpaRepository<Bpdas, Long>, JpaSpecificationExecutor<Bpdas> {
    // Repository methods
}