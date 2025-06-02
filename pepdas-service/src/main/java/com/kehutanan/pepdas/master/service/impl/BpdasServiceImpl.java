package com.kehutanan.pepdas.master.service.impl;

import com.kehutanan.pepdas.master.repository.BpdasRepository;
import com.kehutanan.pepdas.master.service.BpdasService;
import com.kehutanan.pepdas.master.model.Bpdas;
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
public class BpdasServiceImpl implements BpdasService {

    private final BpdasRepository repository;

    @Autowired
    public BpdasServiceImpl(BpdasRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Bpdas> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Bpdas> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "bpdasCache", key = "#id")
    public Bpdas findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bpdas not found with id: " + id));
    }

    @Override
    public Bpdas save(Bpdas bpdas) {
        return repository.save(bpdas);
    }

    @Override
    @CachePut(value = "bpdasCache", key = "#id")
    public Bpdas update(Long id, Bpdas bpdas) {
        return repository.save(bpdas);
    }

    @Override
    @CacheEvict(value = "bpdasCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Bpdas> findByFilters(String namaBpdas, Long provinsiId, Long kabupatenKotaId, Pageable pageable) {
        Specification<Bpdas> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaBpdas if provided
        if (namaBpdas != null && !namaBpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaBpdas")),
                    "%" + namaBpdas.toLowerCase() + "%"));
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