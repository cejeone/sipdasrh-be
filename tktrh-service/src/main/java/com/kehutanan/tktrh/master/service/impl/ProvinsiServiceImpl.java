package com.kehutanan.tktrh.master.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kehutanan.tktrh.master.model.Provinsi;
import com.kehutanan.tktrh.master.repository.ProvinsiRepository;
import com.kehutanan.tktrh.master.service.ProvinsiService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class ProvinsiServiceImpl implements ProvinsiService {

    private final ProvinsiRepository repository;

    @Autowired
    public ProvinsiServiceImpl(ProvinsiRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Provinsi> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Provinsi> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "provinsiCache", key = "#id")
    public Provinsi findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provinsi not found with id: " + id));
    }

    @Override
    public Provinsi save(Provinsi provinsi) {
        return repository.save(provinsi);
    }

    @Override
    @CachePut(value = "provinsiCache", key = "#id")
    public Provinsi update(Long id, Provinsi provinsi) {
        return repository.save(provinsi);
    }

    @Override
    @CacheEvict(value = "provinsiCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Provinsi> findByFilters(String namaProvinsi, String kodeDepdagri, Pageable pageable) {
        Specification<Provinsi> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaProvinsi if provided
        if (namaProvinsi != null && !namaProvinsi.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaProvinsi")),
                    "%" + namaProvinsi.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for kodeDepdagri if provided
        if (kodeDepdagri != null && !kodeDepdagri.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kodeDepdagri")),
                    "%" + kodeDepdagri.toLowerCase() + "%"));
        }

        return repository.findAll(spec, pageable);
    }
}