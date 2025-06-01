package com.kehutanan.rh.master.repository;

import com.kehutanan.rh.master.model.Provinsi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinsiRepository extends JpaRepository<Provinsi, Long>, JpaSpecificationExecutor<Provinsi> {
    // Repository methods
}