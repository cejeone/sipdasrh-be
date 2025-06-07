package com.kehutanan.ppth.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.ppth.master.model.Spas;

@Repository("masterSpasRepository")
public interface SpasRepository extends JpaRepository<Spas, Long>, JpaSpecificationExecutor<Spas> {
    // Repository methods
}