package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.Kecamatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KecamatanRepository extends JpaRepository<Kecamatan, Long>, JpaSpecificationExecutor<Kecamatan> {
    // Repository methods
}