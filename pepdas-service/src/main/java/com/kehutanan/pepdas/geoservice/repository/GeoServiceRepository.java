package com.kehutanan.pepdas.geoservice.repository;

import com.kehutanan.pepdas.geoservice.model.GeoService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GeoServiceRepository extends JpaRepository<GeoService, UUID>,JpaSpecificationExecutor<GeoService> {
}