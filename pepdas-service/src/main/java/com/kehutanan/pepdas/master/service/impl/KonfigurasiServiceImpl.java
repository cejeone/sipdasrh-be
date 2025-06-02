package com.kehutanan.pepdas.master.service.impl;

import com.kehutanan.pepdas.master.repository.KonfigurasiRepository;
import com.kehutanan.pepdas.master.service.KonfigurasiService;
import com.kehutanan.pepdas.master.model.Konfigurasi;
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
public class KonfigurasiServiceImpl implements KonfigurasiService {

    private final KonfigurasiRepository repository;

    @Autowired
    public KonfigurasiServiceImpl(KonfigurasiRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Konfigurasi> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Konfigurasi> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "konfigurasiCache", key = "#id")
    public Konfigurasi findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konfigurasi not found with id: " + id));
    }

    @Override
    public Konfigurasi save(Konfigurasi konfigurasi) {
        return repository.save(konfigurasi);
    }

    @Override
    @CachePut(value = "konfigurasiCache", key = "#id")
    public Konfigurasi update(Long id, Konfigurasi konfigurasi) {
        return repository.save(konfigurasi);
    }

    @Override
    @CacheEvict(value = "konfigurasiCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Konfigurasi> findByFilters(Integer key, String value, String deskripsi, Long tipeId, Pageable pageable) {
        Specification<Konfigurasi> spec = Specification.where(null);

        // Add equals filter for key if provided
        if (key != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("key"), key));
        }

        // Add case-insensitive LIKE filter for value if provided
        if (value != null && !value.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("value")),
                    "%" + value.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for deskripsi if provided
        if (deskripsi != null && !deskripsi.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("deskripsi")),
                    "%" + deskripsi.toLowerCase() + "%"));
        }

        // Add equals filter for tipeId if provided
        if (tipeId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("tipe").get("id"), tipeId));
        }

        return repository.findAll(spec, pageable);
    }
}