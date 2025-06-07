package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Peran;

@Repository
public interface PeranRepository extends JpaRepository<Peran, Long>, JpaSpecificationExecutor<Peran> {
    // Repository methods
}