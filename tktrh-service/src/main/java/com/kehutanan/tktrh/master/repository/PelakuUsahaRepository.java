package com.kehutanan.tktrh.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.tktrh.master.model.PelakuUsaha;

@Repository
public interface PelakuUsahaRepository extends JpaRepository<PelakuUsaha, Long>, JpaSpecificationExecutor<PelakuUsaha> {
    // Repository methods
}