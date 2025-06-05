package com.kehutanan.rm.master.service.impl;

import com.kehutanan.rm.master.repository.PeranRepository;
import com.kehutanan.rm.master.service.PeranService;
import com.kehutanan.rm.master.model.Peran;
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
public class PeranServiceImpl implements PeranService {

    private final PeranRepository repository;

    @Autowired
    public PeranServiceImpl(PeranRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Peran> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Peran> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "peranCache", key = "#id")
    public Peran findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peran not found with id: " + id));
    }

    @Override
    public Peran save(Peran peran) {
        return repository.save(peran);
    }

    @Override
    @CachePut(value = "peranCache", key = "#id")
    public Peran update(Long id, Peran peran) {
        return repository.save(peran);
    }

    @Override
    @CacheEvict(value = "peranCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Peran> findByFilters(String nama, String deskripsi, Long statusId, Pageable pageable) {
        Specification<Peran> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for nama if provided
        if (nama != null && !nama.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nama")),
                    "%" + nama.toLowerCase() + "%"));
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