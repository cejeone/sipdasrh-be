package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.Lov;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LovRepository extends JpaRepository<Lov, Long>, JpaSpecificationExecutor<Lov> {
    // Repository methods
}