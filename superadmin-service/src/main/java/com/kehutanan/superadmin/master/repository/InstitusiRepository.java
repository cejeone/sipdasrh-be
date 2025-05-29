package com.kehutanan.superadmin.master.repository;

import com.kehutanan.superadmin.master.model.Institusi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitusiRepository extends JpaRepository<Institusi, Long>, JpaSpecificationExecutor<Institusi> {
    // Repository methods
}