package com.kehutanan.pepdas.master.service.impl;

import com.kehutanan.pepdas.master.repository.KecamatanRepository;
import com.kehutanan.pepdas.master.service.KecamatanService;
import com.kehutanan.pepdas.master.model.Kecamatan;
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
public class KecamatanServiceImpl implements KecamatanService {

    private final KecamatanRepository repository;

    @Autowired
    public KecamatanServiceImpl(KecamatanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Kecamatan> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Kecamatan> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "kecamatanCache", key = "#id")
    public Kecamatan findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kecamatan not found with id: " + id));
    }

    @Override
    public Kecamatan save(Kecamatan kecamatan) {
        return repository.save(kecamatan);
    }

    @Override
    @CachePut(value = "kecamatanCache", key = "#id")
    public Kecamatan update(Long id, Kecamatan kecamatan) {
        return repository.save(kecamatan);
    }

    @Override
    @CacheEvict(value = "kecamatanCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Kecamatan> findByFilters(String kecamatan, String kodeDepdagri, Long kabupatenKotaId, Pageable pageable) {
        Specification<Kecamatan> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for kecamatan if provided
        if (kecamatan != null && !kecamatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kecamatan")),
                    "%" + kecamatan.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for kodeDepdagri if provided
        if (kodeDepdagri != null && !kodeDepdagri.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kodeDepdagri")),
                    "%" + kodeDepdagri.toLowerCase() + "%"));
        }

        // Add equals filter for kabupatenKotaId if provided
        if (kabupatenKotaId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kabupatenKota").get("id"), kabupatenKotaId));
        }

        return repository.findAll(spec, pageable);
    }
}