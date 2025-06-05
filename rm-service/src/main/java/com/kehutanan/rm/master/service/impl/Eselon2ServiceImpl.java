package com.kehutanan.rm.master.service.impl;

import com.kehutanan.rm.master.repository.Eselon2Repository;
import com.kehutanan.rm.master.service.Eselon2Service;
import com.kehutanan.rm.master.model.Eselon2;
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
public class Eselon2ServiceImpl implements Eselon2Service {

    private final Eselon2Repository repository;

    @Autowired
    public Eselon2ServiceImpl(Eselon2Repository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Eselon2> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Eselon2> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "eselon2Cache", key = "#id")
    public Eselon2 findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Eselon2 not found with id: " + id));
    }

    @Override
    public Eselon2 save(Eselon2 eselon2) {
        return repository.save(eselon2);
    }

    @Override
    @CachePut(value = "eselon2Cache", key = "#id")
    public Eselon2 update(Long id, Eselon2 eselon2) {
        return repository.save(eselon2);
    }

    @Override
    @CacheEvict(value = "eselon2Cache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Eselon2> findByFilters(String nama, String pejabat, Long eselon1Id, Pageable pageable) {
        Specification<Eselon2> spec = Specification.where(null);

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

        // Add equals filter for eselon1Id if provided
        if (eselon1Id != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("eselon1").get("id"), eselon1Id));
        }

        return repository.findAll(spec, pageable);
    }
}