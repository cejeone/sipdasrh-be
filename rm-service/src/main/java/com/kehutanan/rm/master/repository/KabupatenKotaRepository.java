package com.kehutanan.rm.master.repository;

import com.kehutanan.rm.master.model.KabupatenKota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KabupatenKotaRepository extends JpaRepository<KabupatenKota, Long>, JpaSpecificationExecutor<KabupatenKota> {
    // Repository methods
}