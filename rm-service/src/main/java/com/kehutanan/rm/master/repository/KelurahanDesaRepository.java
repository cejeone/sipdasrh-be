package com.kehutanan.rm.master.repository;

import com.kehutanan.rm.master.model.KelurahanDesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KelurahanDesaRepository extends JpaRepository<KelurahanDesa, Long>, JpaSpecificationExecutor<KelurahanDesa> {
    // Repository methods
}