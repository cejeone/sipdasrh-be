package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.Integrasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrasiRepository extends JpaRepository<Integrasi, Long>, JpaSpecificationExecutor<Integrasi> {
    // Repository methods
}