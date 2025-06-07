package com.kehutanan.ppth.master.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kehutanan.ppth.master.model.Eselon1;
import com.kehutanan.ppth.master.repository.Eselon1Repository;
import com.kehutanan.ppth.master.service.Eselon1Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class Eselon1ServiceImpl implements Eselon1Service {

    private final Eselon1Repository repository;

    @Autowired
    public Eselon1ServiceImpl(Eselon1Repository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Eselon1> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Eselon1> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "eselon1Cache", key = "#id")
    public Eselon1 findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Eselon1 not found with id: " + id));
    }

    @Override
    public Eselon1 save(Eselon1 eselon1) {
        return repository.save(eselon1);
    }

    @Override
    @CachePut(value = "eselon1Cache", key = "#id")
    public Eselon1 update(Long id, Eselon1 eselon1) {
        return repository.save(eselon1);
    }

    @Override
    @CacheEvict(value = "eselon1Cache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Eselon1> findByFilters(String nama, String pejabat, Pageable pageable) {
        Specification<Eselon1> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for nama if provided
        if (nama != null && !nama.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nama")),
                    "%" + nama.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for pejabat if provided
        if (pejabat != null && !pejabat.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("pejabat")),
                    "%" + pejabat.toLowerCase() + "%"));
        }

        return repository.findAll(spec, pageable);
    }
}