package com.kehutanan.tktrh.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kehutanan.tktrh.master.model.Uptd;

@Repository
public interface UptdRepository extends JpaRepository<Uptd, Long>, JpaSpecificationExecutor<Uptd> {

}