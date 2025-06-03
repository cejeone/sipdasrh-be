package com.kehutanan.pepdas.master.service.impl;

import com.kehutanan.pepdas.master.repository.LovRepository;
import com.kehutanan.pepdas.master.service.LovService;
import com.kehutanan.pepdas.master.model.Lov;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class LovServiceImpl implements LovService {

    private final LovRepository repository;

    @Autowired
    public LovServiceImpl(LovRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Lov> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Lov> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "lovCache", key = "#id")
    public Lov findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lov not found with id: " + id));
   
    }

    @Override
    public Lov save(Lov lov) {
        return repository.save(lov);
    }

    @Override
    @CachePut(value = "lovCache", key = "#id")
    public Lov update(Long id, Lov lov) {

        return repository.save(lov);
    }

    @Override
    @CacheEvict(value = "lovCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Lov> findByFilters(String namaKategori, String nilai, List<String> status, Pageable pageable) {
        Specification<Lov> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaKategori if provided
        if (namaKategori != null && !namaKategori.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaKategori")),
                    "%" + namaKategori.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for nilai if provided
        if (nilai != null && !nilai.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nilai")),
                    "%" + nilai.toLowerCase() + "%"));
        }

        // Add IN filter for status if provided
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("status").in(status));
        }

        return repository.findAll(spec, pageable);
    }
}