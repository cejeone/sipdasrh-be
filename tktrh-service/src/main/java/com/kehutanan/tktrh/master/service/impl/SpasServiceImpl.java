package com.kehutanan.tktrh.master.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kehutanan.tktrh.master.model.Spas;
import com.kehutanan.tktrh.master.repository.SpasRepository;
import com.kehutanan.tktrh.master.service.SpasService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service("masterSpasServiceImpl")
@Transactional
public class SpasServiceImpl implements SpasService {

    private final SpasRepository repository;

    @Autowired
    public SpasServiceImpl(SpasRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Spas> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Spas> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "spasCache", key = "#id")
    public Spas findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Spas not found with id: " + id));
    }

    @Override
    public Spas save(Spas spas) {
        return repository.save(spas);
    }

    @Override
    @CachePut(value = "spasCache", key = "#id")
    public Spas update(Long id, Spas spas) {
        return repository.save(spas);
    }

    @Override
    @CacheEvict(value = "spasCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Spas> findByFilters(String spas, Long bpdasId, Long dasId, Long provinsiId, 
                                   Long kabupatenKotaId, Long tipeSpasId, Long statusId, Pageable pageable) {
        Specification<Spas> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for spas name if provided
        if (spas != null && !spas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("spas")),
                    "%" + spas.toLowerCase() + "%"));
        }

        // Add equals filter for bpdasId if provided
        if (bpdasId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("bpdas").get("id"), bpdasId));
        }

        // Add equals filter for dasId if provided
        if (dasId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("das").get("id"), dasId));
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

        // Add equals filter for tipeSpasId if provided
        if (tipeSpasId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("tipeSpas").get("id"), tipeSpasId));
        }

        // Add equals filter for statusId if provided
        if (statusId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("status").get("id"), statusId));
        }

        return repository.findAll(spec, pageable);
    }
}