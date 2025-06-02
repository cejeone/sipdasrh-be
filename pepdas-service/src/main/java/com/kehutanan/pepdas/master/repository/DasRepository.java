package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DasRepository extends JpaRepository<Das, Long>, JpaSpecificationExecutor<Das> {
    // Repository methods
}