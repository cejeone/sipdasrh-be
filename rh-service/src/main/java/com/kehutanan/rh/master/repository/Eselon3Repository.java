package com.kehutanan.rh.master.repository;

import com.kehutanan.rh.master.model.Eselon3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface Eselon3Repository extends JpaRepository<Eselon3, Long>, JpaSpecificationExecutor<Eselon3> {
    // Repository methods
}