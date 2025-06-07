package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Eselon1;

@Repository
public interface Eselon1Repository extends JpaRepository<Eselon1, Long>, JpaSpecificationExecutor<Eselon1> {
    // Repository methods
}