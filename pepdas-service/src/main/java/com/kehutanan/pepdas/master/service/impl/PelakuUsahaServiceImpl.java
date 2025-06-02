package com.kehutanan.pepdas.master.service.impl;

import com.kehutanan.pepdas.master.repository.PelakuUsahaRepository;
import com.kehutanan.pepdas.master.service.PelakuUsahaService;
import com.kehutanan.pepdas.master.model.PelakuUsaha;
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
public class PelakuUsahaServiceImpl implements PelakuUsahaService {

    private final PelakuUsahaRepository repository;

    @Autowired
    public PelakuUsahaServiceImpl(PelakuUsahaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<PelakuUsaha> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<PelakuUsaha> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "pelakuUsahaCache", key = "#id")
    public PelakuUsaha findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PelakuUsaha not found with id: " + id));
    }

    @Override
    public PelakuUsaha save(PelakuUsaha pelakuUsaha) {
        return repository.save(pelakuUsaha);
    }

    @Override
    @CachePut(value = "pelakuUsahaCache", key = "#id")
    public PelakuUsaha update(Long id, PelakuUsaha pelakuUsaha) {
        return repository.save(pelakuUsaha);
    }

    @Override
    @CacheEvict(value = "pelakuUsahaCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<PelakuUsaha> findByFilters(
            String namaBadanUsaha, 
            String nomorIndukBerusahaNib, 
            String ruangLingkupUsaha, 
            String namaDirektur,
            Long kategoriPelakuUsahaId, 
            Long provinsiId, 
            Long kabupatenKotaId, 
            Long kecamatanId, 
            Long kelurahanDesaId,
            Pageable pageable) {
        
        Specification<PelakuUsaha> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaBadanUsaha if provided
        if (namaBadanUsaha != null && !namaBadanUsaha.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaBadanUsaha")),
                    "%" + namaBadanUsaha.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for nomorIndukBerusahaNib if provided
        if (nomorIndukBerusahaNib != null && !nomorIndukBerusahaNib.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nomorIndukBerusahaNib")),
                    "%" + nomorIndukBerusahaNib.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for ruangLingkupUsaha if provided
        if (ruangLingkupUsaha != null && !ruangLingkupUsaha.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("ruangLingkupUsaha")),
                    "%" + ruangLingkupUsaha.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for namaDirektur if provided
        if (namaDirektur != null && !namaDirektur.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaDirektur")),
                    "%" + namaDirektur.toLowerCase() + "%"));
        }

        // Add equals filter for kategoriPelakuUsahaId if provided
        if (kategoriPelakuUsahaId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kategoriPelakuUsaha").get("id"), kategoriPelakuUsahaId));
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

        // Add equals filter for kecamatanId if provided
        if (kecamatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kecamatan").get("id"), kecamatanId));
        }

        // Add equals filter for kelurahanDesaId if provided
        if (kelurahanDesaId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kelurahanDesa").get("id"), kelurahanDesaId));
        }

        return repository.findAll(spec, pageable);
    }
}