package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.Eselon2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface Eselon2Repository extends JpaRepository<Eselon2, Long>, JpaSpecificationExecutor<Eselon2> {
    // Repository methods
}