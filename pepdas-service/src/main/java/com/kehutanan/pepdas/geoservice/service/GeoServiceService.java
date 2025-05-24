package com.kehutanan.pepdas.geoservice.service;

import com.kehutanan.pepdas.geoservice.dto.GeoServiceMapper;
import com.kehutanan.pepdas.geoservice.dto.GeoServiceRequestDto;
import com.kehutanan.pepdas.geoservice.model.GeoService;
import com.kehutanan.pepdas.geoservice.repository.GeoServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GeoServiceService {

    private final GeoServiceRepository geoServiceRepository;

    @Autowired
    public GeoServiceService(GeoServiceRepository geoServiceRepository) {
        this.geoServiceRepository = geoServiceRepository;
    }

    public Page<GeoService> findAll(Pageable pageable) {
        return geoServiceRepository.findAll(pageable);
    }

    public Page<GeoService> findByFilters(String direktorat,
                                          String service, Pageable pageable) {
        Specification<GeoService> spec = Specification.where(null);


        if (direktorat != null && !direktorat.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("direktorat")),
                    "%" + direktorat.toLowerCase() + "%"));
        }

        if (service != null && !service.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("service")),
                    "%" + service.toLowerCase() + "%"));
        }

        return geoServiceRepository.findAll(spec, pageable);
    }

    public GeoService findById(UUID id) {
        return geoServiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GeoService not found with id: " + id));
    }

    public GeoService create(GeoServiceRequestDto geoServiceDto) {

        GeoService newGeoService = GeoServiceMapper.toEntity(geoServiceDto);
        return geoServiceRepository.save(newGeoService);
    }

    public GeoService update(UUID id, GeoServiceRequestDto geoServiceDto) {
        GeoService existingGeoService = geoServiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GeoService not found with id: " + id));

        
        // Update fields from DTO to entity
        GeoServiceMapper.updateEntityFromDto(geoServiceDto, existingGeoService);
        
        return geoServiceRepository.save(existingGeoService);
    }

    public void delete(UUID id) {
        if (!geoServiceRepository.existsById(id)) {
            throw new EntityNotFoundException("GeoService not found with id: " + id);
        }
        geoServiceRepository.deleteById(id);
    }

}