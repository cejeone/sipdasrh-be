package com.kehutanan.tktrh.perijinan.repository;

import com.kehutanan.tktrh.perijinan.model.Perijinan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PerijinanRepository extends JpaRepository<Perijinan, Long>, JpaSpecificationExecutor<Perijinan> {

}