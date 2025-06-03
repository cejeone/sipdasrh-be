package com.kehutanan.tktrh.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.tktrh.master.model.Provinsi;

@Repository
public interface ProvinsiRepository extends JpaRepository<Provinsi, Long>, JpaSpecificationExecutor<Provinsi> {
    // Repository methods
}