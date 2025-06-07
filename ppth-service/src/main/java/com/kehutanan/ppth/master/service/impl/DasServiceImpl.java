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

import com.kehutanan.ppth.master.model.Das;
import com.kehutanan.ppth.master.repository.DasRepository;
import com.kehutanan.ppth.master.service.DasService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class DasServiceImpl implements DasService {

    private final DasRepository repository;

    @Autowired
    public DasServiceImpl(DasRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Das> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Das> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "dasCache", key = "#id")
    public Das findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Das not found with id: " + id));
    }

    @Override
    public Das save(Das das) {
        return repository.save(das);
    }

    @Override
    @CachePut(value = "dasCache", key = "#id")
    public Das update(Long id, Das das) {
        return repository.save(das);
    }

    @Override
    @CacheEvict(value = "dasCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Das> findByFilters(String namaDas, Long bpdasId, Pageable pageable) {
        Specification<Das> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaDas if provided
        if (namaDas != null && !namaDas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaDas")),
                    "%" + namaDas.toLowerCase() + "%"));
        }

        // Add equals filter for bpdasId if provided
        if (bpdasId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("bpdas").get("id"), bpdasId));
        }

        return repository.findAll(spec, pageable);
    }
}