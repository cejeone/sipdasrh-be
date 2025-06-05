package com.kehutanan.rm.master.service.impl;

import com.kehutanan.rm.master.repository.IntegrasiRepository;
import com.kehutanan.rm.master.service.IntegrasiService;
import com.kehutanan.rm.master.model.Integrasi;
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
public class IntegrasiServiceImpl implements IntegrasiService {

    private final IntegrasiRepository repository;

    @Autowired
    public IntegrasiServiceImpl(IntegrasiRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Integrasi> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Integrasi> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "integrasiCache", key = "#id")
    public Integrasi findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Integrasi not found with id: " + id));
    }

    @Override
    public Integrasi save(Integrasi integrasi) {
        return repository.save(integrasi);
    }

    @Override
    @CachePut(value = "integrasiCache", key = "#id")
    public Integrasi update(Long id, Integrasi integrasi) {
        return repository.save(integrasi);
    }

    @Override
    @CacheEvict(value = "integrasiCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Integrasi> findByFilters(String url, String tipe, String deskripsi, Long statusId, Pageable pageable) {
        Specification<Integrasi> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for url if provided
        if (url != null && !url.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("url")),
                    "%" + url.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for tipe if provided
        if (tipe != null && !tipe.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("tipe")),
                    "%" + tipe.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for deskripsi if provided
        if (deskripsi != null && !deskripsi.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("deskripsi")),
                    "%" + deskripsi.toLowerCase() + "%"));
        }

        // Add equals filter for statusId if provided
        if (statusId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("status").get("id"), statusId));
        }

        return repository.findAll(spec, pageable);
    }
}