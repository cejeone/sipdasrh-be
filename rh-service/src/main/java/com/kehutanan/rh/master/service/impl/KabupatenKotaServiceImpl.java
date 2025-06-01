package com.kehutanan.rh.master.service.impl;

import com.kehutanan.rh.master.repository.KabupatenKotaRepository;
import com.kehutanan.rh.master.service.KabupatenKotaService;
import com.kehutanan.rh.master.model.KabupatenKota;
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
public class KabupatenKotaServiceImpl implements KabupatenKotaService {

    private final KabupatenKotaRepository repository;

    @Autowired
    public KabupatenKotaServiceImpl(KabupatenKotaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<KabupatenKota> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<KabupatenKota> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "kabupatenKotaCache", key = "#id")
    public KabupatenKota findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KabupatenKota not found with id: " + id));
    }

    @Override
    public KabupatenKota save(KabupatenKota kabupatenKota) {
        return repository.save(kabupatenKota);
    }

    @Override
    @CachePut(value = "kabupatenKotaCache", key = "#id")
    public KabupatenKota update(Long id, KabupatenKota kabupatenKota) {
        return repository.save(kabupatenKota);
    }

    @Override
    @CacheEvict(value = "kabupatenKotaCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<KabupatenKota> findByFilters(String kabupatenKota, String kodeDepdagri, Long provinsiId, Pageable pageable) {
        Specification<KabupatenKota> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for kabupatenKota if provided
        if (kabupatenKota != null && !kabupatenKota.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kabupatenKota")),
                    "%" + kabupatenKota.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for kodeDepdagri if provided
        if (kodeDepdagri != null && !kodeDepdagri.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kodeDepdagri")),
                    "%" + kodeDepdagri.toLowerCase() + "%"));
        }

        // Add equals filter for provinsiId if provided
        if (provinsiId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("provinsi").get("id"), provinsiId));
        }

        return repository.findAll(spec, pageable);
    }
}