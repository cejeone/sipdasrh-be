package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Das;

@Repository
public interface DasRepository extends JpaRepository<Das, Long>, JpaSpecificationExecutor<Das> {
    // Repository methods
}