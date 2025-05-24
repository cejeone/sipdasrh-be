package com.kehutanan.pepdas.pemantauandas.repository;

import com.kehutanan.pepdas.geoservice.model.GeoService;
import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PemantauanDasRepository extends JpaRepository<PemantauanDas, UUID>,JpaSpecificationExecutor<PemantauanDas> {
}