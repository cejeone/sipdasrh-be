package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Kecamatan;

@Repository
public interface KecamatanRepository extends JpaRepository<Kecamatan, Long>, JpaSpecificationExecutor<Kecamatan> {
    // Repository methods
}