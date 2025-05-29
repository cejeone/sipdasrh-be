package com.kehutanan.superadmin.master.service.impl;

import com.kehutanan.superadmin.master.repository.Eselon3Repository;
import com.kehutanan.superadmin.master.service.Eselon3Service;
import com.kehutanan.superadmin.master.model.Eselon3;
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
public class Eselon3ServiceImpl implements Eselon3Service {

    private final Eselon3Repository repository;

    @Autowired
    public Eselon3ServiceImpl(Eselon3Repository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Eselon3> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Eselon3> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "eselon3Cache", key = "#id")
    public Eselon3 findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Eselon3 not found with id: " + id));
    }

    @Override
    public Eselon3 save(Eselon3 eselon3) {
        return repository.save(eselon3);
    }

    @Override
    @CachePut(value = "eselon3Cache", key = "#id")
    public Eselon3 update(Long id, Eselon3 eselon3) {
        return repository.save(eselon3);
    }

    @Override
    @CacheEvict(value = "eselon3Cache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Eselon3> findByFilters(String nama, String pejabat, Long eselon2Id, Pageable pageable) {
        Specification<Eselon3> spec = Specification.where(null);

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

        // Add equals filter for eselon2Id if provided
        if (eselon2Id != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("eselon2").get("id"), eselon2Id));
        }

        return repository.findAll(spec, pageable);
    }
}