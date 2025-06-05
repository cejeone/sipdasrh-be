package com.kehutanan.rm.master.repository;

import com.kehutanan.rm.master.model.Bpth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BpthRepository extends JpaRepository<Bpth, Long>, JpaSpecificationExecutor<Bpth> {
    // Repository methods
}