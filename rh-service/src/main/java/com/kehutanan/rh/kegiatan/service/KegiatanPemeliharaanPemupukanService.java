package com.kehutanan.rh.kegiatan.service;

import com.kehutanan.rh.kegiatan.dto.KegiatanPemeliharaanPemupukanDto;
import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanPemupukan;
import com.kehutanan.rh.kegiatan.repository.KegiatanPemeliharaanPemupukanRepository;

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
public class KegiatanPemeliharaanPemupukanService {

    private final KegiatanPemeliharaanPemupukanRepository kegiatanPemeliharaanPemupukanRepository;

    public KegiatanPemeliharaanPemupukanService(KegiatanPemeliharaanPemupukanRepository kegiatanPemeliharaanPemupukanRepository) {
        this.kegiatanPemeliharaanPemupukanRepository = kegiatanPemeliharaanPemupukanRepository;
    }

    /**
     * Find all kegiatan pemeliharaan pemupukan with optional search filtering
     * 
     * @param search search term for filtering
     * @param pageable pagination information
     * @param assembler resource assembler for HATEOAS
     * @return PagedModel with kegiatan pemeliharaan pemupukan data
     */
    public PagedModel<EntityModel<KegiatanPemeliharaanPemupukan>> findAll(
            String search, 
            Pageable pageable,
            PagedResourcesAssembler<KegiatanPemeliharaanPemupukan> assembler) {
        
        Page<KegiatanPemeliharaanPemupukan> page;
        
        if (search != null && !search.isEmpty()) {
            // Create specification for searching in jenis and keterangan fields
            Specification<KegiatanPemeliharaanPemupukan> spec = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                
                String searchPattern = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jenis")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("satuan")), searchPattern)
                ));
                
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
            
            page = kegiatanPemeliharaanPemupukanRepository.findAll(spec, pageable);
        } else {
            page = kegiatanPemeliharaanPemupukanRepository.findAll(pageable);
        }
        
        return assembler.toModel(page);
    }

    /**
     * Find a kegiatan pemeliharaan pemupukan by ID
     * 
     * @param id the ID of the kegiatan pemeliharaan pemupukan
     * @return the found kegiatan pemeliharaan pemupukan
     * @throws EntityNotFoundException if kegiatan pemeliharaan pemupukan is not found
     */
    public KegiatanPemeliharaanPemupukan findById(UUID id) {
        return kegiatanPemeliharaanPemupukanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanPemeliharaanPemupukan not found with ID: " + id));
    }

    /**
     * Create a new kegiatan pemeliharaan pemupukan
     * 
     * @param kegiatanPemeliharaanPemupukan the kegiatan pemeliharaan pemupukan to create
     * @return the created kegiatan pemeliharaan pemupukan
     */
    public KegiatanPemeliharaanPemupukan create(KegiatanPemeliharaanPemupukan kegiatanPemeliharaanPemupukan) {
        return kegiatanPemeliharaanPemupukanRepository.save(kegiatanPemeliharaanPemupukan);
    }

    /**
     * Update an existing kegiatan pemeliharaan pemupukan
     * 
     * @param id the ID of the kegiatan pemeliharaan pemupukan to update
     * @param kegiatanPemeliharaanPemupukanDto the updated kegiatan pemeliharaan pemupukan data
     * @return the updated kegiatan pemeliharaan pemupukan
     * @throws EntityNotFoundException if kegiatan pemeliharaan pemupukan is not found
     */
    public KegiatanPemeliharaanPemupukan update(UUID id, KegiatanPemeliharaanPemupukanDto kegiatanPemeliharaanPemupukanDto) {
        KegiatanPemeliharaanPemupukan existing = findById(id);
        
        // Update fields from the DTO
        existing.setJenis(kegiatanPemeliharaanPemupukanDto.getJenis());
        existing.setWaktuPemupukan(kegiatanPemeliharaanPemupukanDto.getWaktuPemupukan());
        existing.setJumlahPupuk(kegiatanPemeliharaanPemupukanDto.getJumlahPupuk());
        existing.setSatuan(kegiatanPemeliharaanPemupukanDto.getSatuan());
        existing.setJumlahHokPerempuan(kegiatanPemeliharaanPemupukanDto.getJumlahHokPerempuan());
        existing.setJumlahHokLakiLaki(kegiatanPemeliharaanPemupukanDto.getJumlahHokLakiLaki());
        existing.setKeterangan(kegiatanPemeliharaanPemupukanDto.getKeterangan());
        existing.setStatus(kegiatanPemeliharaanPemupukanDto.getStatus());
        
        return kegiatanPemeliharaanPemupukanRepository.save(existing);
    }

    /**
     * Delete a kegiatan pemeliharaan pemupukan
     * 
     * @param id the ID of the kegiatan pemeliharaan pemupukan to delete
     * @throws EntityNotFoundException if kegiatan pemeliharaan pemupukan is not found
     */
    public void delete(UUID id) {
        if (!kegiatanPemeliharaanPemupukanRepository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanPemeliharaanPemupukan not found with ID: " + id);
        }
        
        kegiatanPemeliharaanPemupukanRepository.deleteById(id);
    }
}