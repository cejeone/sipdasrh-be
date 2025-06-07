package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Konfigurasi;

@Repository
public interface KonfigurasiRepository extends JpaRepository<Konfigurasi, Long>, JpaSpecificationExecutor<Konfigurasi> {
    // Repository methods
}