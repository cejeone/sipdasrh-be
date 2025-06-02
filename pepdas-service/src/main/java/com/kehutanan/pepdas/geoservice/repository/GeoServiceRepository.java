package com.kehutanan.pepdas.geoservice.repository;

import com.kehutanan.pepdas.geoservice.model.GeoService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoServiceRepository extends JpaRepository<GeoService, Long>, JpaSpecificationExecutor<GeoService> {

}