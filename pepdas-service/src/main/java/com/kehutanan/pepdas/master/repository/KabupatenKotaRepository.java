package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.KabupatenKota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KabupatenKotaRepository extends JpaRepository<KabupatenKota, Long>, JpaSpecificationExecutor<KabupatenKota> {
    // Repository methods
}