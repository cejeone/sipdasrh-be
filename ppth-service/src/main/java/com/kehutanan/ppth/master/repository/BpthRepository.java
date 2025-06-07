package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Bpth;

@Repository
public interface BpthRepository extends JpaRepository<Bpth, Long>, JpaSpecificationExecutor<Bpth> {
    // Repository methods
}