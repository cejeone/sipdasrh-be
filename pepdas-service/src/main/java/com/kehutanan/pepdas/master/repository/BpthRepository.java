package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.Bpth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BpthRepository extends JpaRepository<Bpth, Long>, JpaSpecificationExecutor<Bpth> {
    // Repository methods
}