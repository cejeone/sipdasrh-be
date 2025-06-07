package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Integrasi;

@Repository
public interface IntegrasiRepository extends JpaRepository<Integrasi, Long>, JpaSpecificationExecutor<Integrasi> {
    // Repository methods
}