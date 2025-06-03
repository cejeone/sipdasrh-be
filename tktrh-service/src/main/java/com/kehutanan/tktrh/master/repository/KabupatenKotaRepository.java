package com.kehutanan.tktrh.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.tktrh.master.model.KabupatenKota;

@Repository
public interface KabupatenKotaRepository extends JpaRepository<KabupatenKota, Long>, JpaSpecificationExecutor<KabupatenKota> {
    // Repository methods
}