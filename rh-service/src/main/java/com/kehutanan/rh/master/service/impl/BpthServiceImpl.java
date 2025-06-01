package com.kehutanan.rh.master.service.impl;

import com.kehutanan.rh.master.repository.BpthRepository;
import com.kehutanan.rh.master.service.BpthService;
import com.kehutanan.rh.master.model.Bpth;
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
public class BpthServiceImpl implements BpthService {

    private final BpthRepository repository;

    @Autowired
    public BpthServiceImpl(BpthRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Bpth> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Bpth> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "bpthCache", key = "#id")
    public Bpth findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BPTH not found with id: " + id));
    }

    @Override
    public Bpth save(Bpth bpth) {
        return repository.save(bpth);
    }

    @Override
    @CachePut(value = "bpthCache", key = "#id")
    public Bpth update(Long id, Bpth bpth) {
        return repository.save(bpth);
    }

    @Override
    @CacheEvict(value = "bpthCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Bpth> findByFilters(String namaBpth, Long provinsiId, Long kabupatenKotaId, Pageable pageable) {
        Specification<Bpth> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaBpth if provided
        if (namaBpth != null && !namaBpth.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaBpth")),
                    "%" + namaBpth.toLowerCase() + "%"));
        }

        // Add equals filter for provinsiId if provided
        if (provinsiId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("provinsi").get("id"), provinsiId));
        }

        // Add equals filter for kabupatenKotaId if provided
        if (kabupatenKotaId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kabupatenKota").get("id"), kabupatenKotaId));
        }

        return repository.findAll(spec, pageable);
    }
}