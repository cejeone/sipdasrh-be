package com.kehutanan.rh.master.repository;

import com.kehutanan.rh.master.model.Peran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PeranRepository extends JpaRepository<Peran, Long>, JpaSpecificationExecutor<Peran> {
    // Repository methods
}