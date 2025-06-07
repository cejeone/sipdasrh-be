package com.kehutanan.ppth.perijinan.repository;

import com.kehutanan.ppth.perijinan.model.Perijinan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PerijinanRepository extends JpaRepository<Perijinan, Long>, JpaSpecificationExecutor<Perijinan> {

}