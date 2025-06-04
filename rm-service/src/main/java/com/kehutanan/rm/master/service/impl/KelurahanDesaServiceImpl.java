package com.kehutanan.rm.master.service.impl;

import com.kehutanan.rm.master.repository.KelurahanDesaRepository;
import com.kehutanan.rm.master.service.KelurahanDesaService;
import com.kehutanan.rm.master.model.KelurahanDesa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class KelurahanDesaServiceImpl implements KelurahanDesaService {

    private final KelurahanDesaRepository repository;

    @Autowired
    public KelurahanDesaServiceImpl(KelurahanDesaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<KelurahanDesa> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<KelurahanDesa> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "kelurahanDesaCache", key = "#id")
    public KelurahanDesa findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KelurahanDesa not found with id: " + id));
    }

    @Override
    public KelurahanDesa save(KelurahanDesa kelurahanDesa) {
        return repository.save(kelurahanDesa);
    }

    @Override
    @CachePut(value = "kelurahanDesaCache", key = "#id")
    public KelurahanDesa update(Long id, KelurahanDesa kelurahanDesa) {
        return repository.save(kelurahanDesa);
    }

    @Override
    @CacheEvict(value = "kelurahanDesaCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<KelurahanDesa> findByFilters(String kelurahan, Long kecamatanId, Pageable pageable) {
        Specification<KelurahanDesa> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for kelurahan if provided
        if (kelurahan != null && !kelurahan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kelurahan")),
                    "%" + kelurahan.toLowerCase() + "%"));
        }

        // Add equals filter for kecamatanId if provided
        if (kecamatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kecamatan").get("id"), kecamatanId));
        }

        return repository.findAll(spec, pageable);
    }
}