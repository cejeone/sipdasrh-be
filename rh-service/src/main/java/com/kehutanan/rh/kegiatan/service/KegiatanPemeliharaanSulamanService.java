package com.kehutanan.rh.kegiatan.service;

import com.kehutanan.rh.kegiatan.dto.KegiatanPemeliharaanSulamanDto;
import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanSulaman;
import com.kehutanan.rh.kegiatan.repository.KegiatanPemeliharaanSulamanRepository;

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
public class KegiatanPemeliharaanSulamanService {

    private final KegiatanPemeliharaanSulamanRepository kegiatanPemeliharaanSulamanRepository;

    public KegiatanPemeliharaanSulamanService(
            KegiatanPemeliharaanSulamanRepository kegiatanPemeliharaanSulamanRepository) {
        this.kegiatanPemeliharaanSulamanRepository = kegiatanPemeliharaanSulamanRepository;
    }

    /**
     * Find all kegiatan pemeliharaan sulaman with optional search filtering
     * 
     * @param search    search term for filtering
     * @param pageable  pagination information
     * @param assembler resource assembler for HATEOAS
     * @return PagedModel with kegiatan pemeliharaan sulaman data
     */
    public PagedModel<EntityModel<KegiatanPemeliharaanSulaman>> findAll(String kegiatanId, String search,
            Pageable pageable, PagedResourcesAssembler<KegiatanPemeliharaanSulaman> assembler) {

        // Create base specification
        Specification<KegiatanPemeliharaanSulaman> spec = Specification.where(null);

        // Add kegiatan ID filter
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
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kategori")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kondisiTanaman")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("namaBibit")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern)));
        }

        // Execute the query with all applicable filters
        Page<KegiatanPemeliharaanSulaman> page = kegiatanPemeliharaanSulamanRepository.findAll(spec, pageable);

        return assembler.toModel(page);
    }

    /**
     * Find a kegiatan pemeliharaan sulaman by ID
     * 
     * @param id the ID of the kegiatan pemeliharaan sulaman
     * @return the found kegiatan pemeliharaan sulaman
     * @throws EntityNotFoundException if kegiatan pemeliharaan sulaman is not found
     */
    public KegiatanPemeliharaanSulaman findById(UUID id) {
        return kegiatanPemeliharaanSulamanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanPemeliharaanSulaman not found with ID: " + id));
    }

    /**
     * Create a new kegiatan pemeliharaan sulaman
     * 
     * @param kegiatanPemeliharaanSulaman the kegiatan pemeliharaan sulaman to
     *                                    create
     * @return the created kegiatan pemeliharaan sulaman
     */
    public KegiatanPemeliharaanSulaman create(KegiatanPemeliharaanSulaman kegiatanPemeliharaanSulaman) {
        return kegiatanPemeliharaanSulamanRepository.save(kegiatanPemeliharaanSulaman);
    }

    /**
     * Update an existing kegiatan pemeliharaan sulaman
     * 
     * @param id                             the ID of the kegiatan pemeliharaan
     *                                       sulaman to update
     * @param kegiatanPemeliharaanSulamanDto the updated kegiatan pemeliharaan
     *                                       sulaman data
     * @return the updated kegiatan pemeliharaan sulaman
     * @throws EntityNotFoundException if kegiatan pemeliharaan sulaman is not found
     */
    public KegiatanPemeliharaanSulaman update(UUID id, KegiatanPemeliharaanSulamanDto kegiatanPemeliharaanSulamanDto) {
        KegiatanPemeliharaanSulaman existing = findById(id);

        // Update fields from the DTO
        existing.setKategori(kegiatanPemeliharaanSulamanDto.getKategori());
        existing.setWaktuPenyulaman(kegiatanPemeliharaanSulamanDto.getWaktuPenyulaman());
        existing.setNamaBibit(kegiatanPemeliharaanSulamanDto.getNamaBibit());
        existing.setJumlahBibit(kegiatanPemeliharaanSulamanDto.getJumlahBibit());
        existing.setKondisiTanaman(kegiatanPemeliharaanSulamanDto.getKondisiTanaman());
        existing.setJumlahTanamanHidup(kegiatanPemeliharaanSulamanDto.getJumlahTanamanHidup());
        existing.setJumlahHokPerempuan(kegiatanPemeliharaanSulamanDto.getJumlahHokPerempuan());
        existing.setJumlahHokLakiLaki(kegiatanPemeliharaanSulamanDto.getJumlahHokLakiLaki());
        existing.setKeterangan(kegiatanPemeliharaanSulamanDto.getKeterangan());
        existing.setStatus(kegiatanPemeliharaanSulamanDto.getStatus());

        return kegiatanPemeliharaanSulamanRepository.save(existing);
    }

    /**
     * Delete a kegiatan pemeliharaan sulaman
     * 
     * @param id the ID of the kegiatan pemeliharaan sulaman to delete
     * @throws EntityNotFoundException if kegiatan pemeliharaan sulaman is not found
     */
    public void delete(UUID id) {
        if (!kegiatanPemeliharaanSulamanRepository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanPemeliharaanSulaman not found with ID: " + id);
        }

        kegiatanPemeliharaanSulamanRepository.deleteById(id);
    }
}