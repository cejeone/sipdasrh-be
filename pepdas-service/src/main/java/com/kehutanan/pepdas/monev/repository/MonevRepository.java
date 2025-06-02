package com.kehutanan.pepdas.monev.repository;

import com.kehutanan.pepdas.monev.model.Monev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MonevRepository extends JpaRepository<Monev, Long>, JpaSpecificationExecutor<Monev> {

}