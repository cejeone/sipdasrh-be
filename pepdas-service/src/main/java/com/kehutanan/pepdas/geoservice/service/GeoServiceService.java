package com.kehutanan.pepdas.geoservice.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.pepdas.geoservice.dto.GeoServiceDTO;
import com.kehutanan.pepdas.geoservice.dto.GeoServicePageDTO;
import com.kehutanan.pepdas.geoservice.model.GeoService;

public interface GeoServiceService {

    List<GeoService> findAll();

    GeoServiceDTO findDTOById(Long id);

    GeoService findById(Long id);

    GeoService save(GeoService geoService);

    GeoService update(Long id, GeoService geoService);

    void deleteById(Long id);

    GeoServicePageDTO findAllWithCache(Pageable pageable, String baseUrl);

    GeoServicePageDTO findByFiltersWithCache(String service, List<String> bpdas, Pageable pageable, String baseUrl);

    GeoServicePageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
