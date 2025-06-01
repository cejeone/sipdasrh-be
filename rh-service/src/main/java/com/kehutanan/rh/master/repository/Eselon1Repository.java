package com.kehutanan.rh.master.repository;

import com.kehutanan.rh.master.model.Eselon1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface Eselon1Repository extends JpaRepository<Eselon1, Long>, JpaSpecificationExecutor<Eselon1> {
    // Repository methods
}