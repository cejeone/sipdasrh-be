package com.kehutanan.superadmin.master.service.impl;

import com.kehutanan.superadmin.master.repository.InstitusiRepository;
import com.kehutanan.superadmin.master.service.InstitusiService;
import com.kehutanan.superadmin.master.model.Institusi;
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
public class InstitusiServiceImpl implements InstitusiService {

    private final InstitusiRepository repository;

    @Autowired
    public InstitusiServiceImpl(InstitusiRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Institusi> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Institusi> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "institusiCache", key = "#id")
    public Institusi findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Institusi not found with id: " + id));
    }

    @Override
    public Institusi save(Institusi institusi) {
        return repository.save(institusi);
    }

    @Override
    @CachePut(value = "institusiCache", key = "#id")
    public Institusi update(Long id, Institusi institusi) {
        return repository.save(institusi);
    }

    @Override
    @CacheEvict(value = "institusiCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Institusi> findByFilters(String nama, String email, Long tipeInstitusiId, Long provinsiId, Long kabupatenKotaId, Pageable pageable) {
        Specification<Institusi> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for nama if provided
        if (nama != null && !nama.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nama")),
                    "%" + nama.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for email if provided
        if (email != null && !email.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%"));
        }

        // Add equals filter for tipeInstitusiId if provided
        if (tipeInstitusiId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("tipeInstitusi").get("id"), tipeInstitusiId));
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