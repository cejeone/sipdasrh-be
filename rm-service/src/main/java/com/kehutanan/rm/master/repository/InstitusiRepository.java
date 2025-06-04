package com.kehutanan.rm.master.repository;

import com.kehutanan.rm.master.model.Institusi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitusiRepository extends JpaRepository<Institusi, Long>, JpaSpecificationExecutor<Institusi> {
    // Repository methods
}