package com.kehutanan.rh.kegiatan.service;

import com.kehutanan.rh.kegiatan.dto.KegiatanFungsiKawasanDto;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.rh.kegiatan.repository.KegiatanFungsiKawasanRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class KegiatanFungsiKawasanService {

    private final KegiatanFungsiKawasanRepository kegiatanFungsiKawasanRepository;

    public KegiatanFungsiKawasanService(KegiatanFungsiKawasanRepository kegiatanFungsiKawasanRepository) {
        this.kegiatanFungsiKawasanRepository = kegiatanFungsiKawasanRepository;
    }

    /**
     * Find all kegiatan fungsi kawasan with optional search filtering
     * 
     * @param search    search term for filtering
     * @param pageable  pagination information
     * @param assembler resource assembler for HATEOAS
     * @return PagedModel with kegiatan fungsi kawasan data
     */
    public PagedModel<EntityModel<KegiatanFungsiKawasan>> findAll(String kegiatanId, String search,
            Pageable pageable, PagedResourcesAssembler<KegiatanFungsiKawasan> assembler) {

        // Create base specification
        Specification<KegiatanFungsiKawasan> spec = Specification.where(null);

        // Add kegiatan ID filter if provided
        if (kegiatanId != null && !kegiatanId.isEmpty()) {
            try {
                UUID kegiatanUuid = UUID.fromString(kegiatanId);
                spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("kegiatan").get("id"),
                        kegiatanUuid));
            } catch (IllegalArgumentException e) {
                // Handle invalid UUID format
                throw new IllegalArgumentException("Invalid Kegiatan ID format");
            }
        }

        // Add search filter if provided
        if (search != null && !search.isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fungsiKawasan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern)));
        }

        // Execute the query with all applicable filters
        Page<KegiatanFungsiKawasan> page = kegiatanFungsiKawasanRepository.findAll(spec, pageable);

        return assembler.toModel(page);
    }

    /**
     * Find a kegiatan fungsi kawasan by ID
     * 
     * @param id the ID of the kegiatan fungsi kawasan
     * @return the found kegiatan fungsi kawasan
     * @throws EntityNotFoundException if kegiatan fungsi kawasan is not found
     */
    public KegiatanFungsiKawasan findById(UUID id) {
        return kegiatanFungsiKawasanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanFungsiKawasan not found with ID: " + id));
    }

    /**
     * Create a new kegiatan fungsi kawasan
     * 
     * @param kegiatanFungsiKawasan the kegiatan fungsi kawasan to create
     * @return the created kegiatan fungsi kawasan
     */
    public KegiatanFungsiKawasan create(KegiatanFungsiKawasan kegiatanFungsiKawasan) {

        return kegiatanFungsiKawasanRepository.save(kegiatanFungsiKawasan);
    }

    /**
     * Update an existing kegiatan fungsi kawasan
     * 
     * @param id                           the ID of the kegiatan fungsi kawasan to
     *                                     update
     * @param updatedKegiatanFungsiKawasan the updated kegiatan fungsi kawasan data
     * @return the updated kegiatan fungsi kawasan
     * @throws EntityNotFoundException if kegiatan fungsi kawasan is not found
     */
    public KegiatanFungsiKawasan update(UUID id, KegiatanFungsiKawasanDto kegiatanFungsiKawasanDto) {
        KegiatanFungsiKawasan existing = findById(id);

        // Update fields from the updatedKegiatanFungsiKawasan
        existing.setFungsiKawasan(kegiatanFungsiKawasanDto.getFungsiKawasan());
        existing.setTargetLuasHa(kegiatanFungsiKawasanDto.getTargetLuasHa());
        existing.setRealisasiLuas(kegiatanFungsiKawasanDto.getRealisasiLuas());
        existing.setTahun(kegiatanFungsiKawasanDto.getTahun());
        existing.setKeterangan(kegiatanFungsiKawasanDto.getKeterangan());
        existing.setStatus(kegiatanFungsiKawasanDto.getStatus());

        // Update any other fields as needed

        return kegiatanFungsiKawasanRepository.save(existing);
    }

    /**
     * Delete a kegiatan fungsi kawasan
     * 
     * @param id the ID of the kegiatan fungsi kawasan to delete
     * @throws EntityNotFoundException if kegiatan fungsi kawasan is not found
     */
    public void delete(UUID id) {
        if (!kegiatanFungsiKawasanRepository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanFungsiKawasan not found with ID: " + id);
        }

        kegiatanFungsiKawasanRepository.deleteById(id);
    }
}