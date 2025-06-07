package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.KelurahanDesa;

@Repository
public interface KelurahanDesaRepository extends JpaRepository<KelurahanDesa, Long>, JpaSpecificationExecutor<KelurahanDesa> {
    // Repository methods
}