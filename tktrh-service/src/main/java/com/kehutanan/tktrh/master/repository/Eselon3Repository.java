package com.kehutanan.tktrh.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.tktrh.master.model.Eselon3;

@Repository
public interface Eselon3Repository extends JpaRepository<Eselon3, Long>, JpaSpecificationExecutor<Eselon3> {
    // Repository methods
}