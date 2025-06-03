package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.Spas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SpasRepository extends JpaRepository<Spas, Long>, JpaSpecificationExecutor<Spas> {
    // Repository methods
}