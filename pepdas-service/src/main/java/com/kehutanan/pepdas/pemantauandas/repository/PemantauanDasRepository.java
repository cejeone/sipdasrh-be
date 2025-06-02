package com.kehutanan.pepdas.pemantauandas.repository;

import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PemantauanDasRepository extends JpaRepository<PemantauanDas, Long>, JpaSpecificationExecutor<PemantauanDas> {

}