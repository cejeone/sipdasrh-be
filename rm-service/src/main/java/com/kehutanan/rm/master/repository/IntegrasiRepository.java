package com.kehutanan.rm.master.repository;

import com.kehutanan.rm.master.model.Integrasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrasiRepository extends JpaRepository<Integrasi, Long>, JpaSpecificationExecutor<Integrasi> {
    // Repository methods
}