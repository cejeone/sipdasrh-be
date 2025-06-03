package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.KelurahanDesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KelurahanDesaRepository extends JpaRepository<KelurahanDesa, Long>, JpaSpecificationExecutor<KelurahanDesa> {
    // Repository methods
}