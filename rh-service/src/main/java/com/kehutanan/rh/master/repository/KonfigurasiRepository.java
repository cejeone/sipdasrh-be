package com.kehutanan.rh.master.repository;

import com.kehutanan.rh.master.model.Konfigurasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KonfigurasiRepository extends JpaRepository<Konfigurasi, Long>, JpaSpecificationExecutor<Konfigurasi> {
    // Repository methods
}