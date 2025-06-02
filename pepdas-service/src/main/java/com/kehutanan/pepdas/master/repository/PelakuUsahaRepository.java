package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.PelakuUsaha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PelakuUsahaRepository extends JpaRepository<PelakuUsaha, Long>, JpaSpecificationExecutor<PelakuUsaha> {
    // Repository methods
}