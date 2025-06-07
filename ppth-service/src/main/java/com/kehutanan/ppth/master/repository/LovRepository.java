package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Lov;

@Repository
public interface LovRepository extends JpaRepository<Lov, Long>, JpaSpecificationExecutor<Lov> {
    // Repository methods
}