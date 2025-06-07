package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Eselon2;

@Repository
public interface Eselon2Repository extends JpaRepository<Eselon2, Long>, JpaSpecificationExecutor<Eselon2> {
    // Repository methods
}