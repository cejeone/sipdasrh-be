package com.kehutanan.pepdas.serahterima.repository;

import com.kehutanan.pepdas.serahterima.model.Bast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BastRepository extends JpaRepository<Bast, Long>, JpaSpecificationExecutor<Bast> {

}