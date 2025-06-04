package com.kehutanan.rm.master.repository;

import com.kehutanan.rm.master.model.Spas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SpasRepository extends JpaRepository<Spas, Long>, JpaSpecificationExecutor<Spas> {
    // Repository methods
}