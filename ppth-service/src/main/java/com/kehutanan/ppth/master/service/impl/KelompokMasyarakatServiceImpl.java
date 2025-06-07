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

import com.kehutanan.ppth.master.dto.KelompokMasyarakatDTO;
import com.kehutanan.ppth.master.model.KelompokMasyarakat;
import com.kehutanan.ppth.master.repository.KelompokMasyarakatRepository;
import com.kehutanan.ppth.master.service.KelompokMasyarakatService;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class KelompokMasyarakatServiceImpl implements KelompokMasyarakatService {

    private final KelompokMasyarakatRepository repository;

    @Autowired
    public KelompokMasyarakatServiceImpl(KelompokMasyarakatRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<KelompokMasyarakat> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<KelompokMasyarakat> findAll() {
        return repository.findAll();
    }

    @Override

    public KelompokMasyarakat findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KelompokMasyarakat not found with id: " + id));
    }

    @Override
    @Cacheable(value = "kelompokMasyarakatCache", key = "#id")
    public KelompokMasyarakatDTO findDtoById(Long id) {
        KelompokMasyarakat kelompokMasyarakat = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KelompokMasyarakat not found with id: " + id));

        KelompokMasyarakatDTO dto = new KelompokMasyarakatDTO(kelompokMasyarakat);

        return dto;

    }

    @Override
    public KelompokMasyarakat save(KelompokMasyarakat kelompokMasyarakat) {
        return repository.save(kelompokMasyarakat);
    }

    @Override
    @CachePut(value = "kelompokMasyarakatCache", key = "#id")
    public KelompokMasyarakat update(Long id, KelompokMasyarakat kelompokMasyarakat) {
        return repository.save(kelompokMasyarakat);
    }

    @Override
    @CachePut(value = "kelompokMasyarakatCache", key = "#id")
    public KelompokMasyarakatDTO updateWithDto(Long id, KelompokMasyarakat kelompokMasyarakat) {
        repository.save(kelompokMasyarakat);
        KelompokMasyarakatDTO dto = new KelompokMasyarakatDTO(kelompokMasyarakat);

        return dto;
    }

    @Override
    @CacheEvict(value = "kelompokMasyarakatCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<KelompokMasyarakat> findByFilters(String namaKelompokMasyarakat, String nomorSkPenetapan,
            LocalDate tanggalSkPenetapan, Long provinsiId, Long kabupatenKotaId, Long kecamatanId,
            Long kelurahanDesaId, Pageable pageable) {

        Specification<KelompokMasyarakat> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaKelompokMasyarakat if provided
        if (namaKelompokMasyarakat != null && !namaKelompokMasyarakat.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaKelompokMasyarakat")),
                    "%" + namaKelompokMasyarakat.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for nomorSkPenetapan if provided
        if (nomorSkPenetapan != null && !nomorSkPenetapan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nomorSkPenetapan")),
                    "%" + nomorSkPenetapan.toLowerCase() + "%"));
        }

        // Add equals filter for tanggalSkPenetapan if provided
        if (tanggalSkPenetapan != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("tanggalSkPenetapan"), tanggalSkPenetapan));
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