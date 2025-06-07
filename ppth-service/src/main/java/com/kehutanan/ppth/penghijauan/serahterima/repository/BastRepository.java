package com.kehutanan.ppth.penghijauan.serahterima.repository;

import com.kehutanan.ppth.penghijauan.serahterima.model.Bast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BastRepository extends JpaRepository<Bast, Long>, JpaSpecificationExecutor<Bast> {

}