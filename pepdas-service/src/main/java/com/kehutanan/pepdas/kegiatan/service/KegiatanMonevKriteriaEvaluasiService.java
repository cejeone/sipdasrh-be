package com.kehutanan.pepdas.kegiatan.service;

import com.kehutanan.pepdas.kegiatan.dto.KegiatanMonevKriteriaEvaluasiDto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanMonevKriteriaEvaluasi;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanMonevKriteriaEvaluasiRepository;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanMonevRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class KegiatanMonevKriteriaEvaluasiService {

    private final KegiatanMonevKriteriaEvaluasiRepository kegiatanMonevKriteriaEvaluasiRepository;
    private final KegiatanMonevRepository kegiatanMonevRepository;

    /**
     * Find all kriteria evaluasi with optional search filtering
     * 
     * @param search search term for filtering
     * @param pageable pagination information
     * @param assembler resource assembler for HATEOAS
     * @return PagedModel with kriteria evaluasi data
     */
    public PagedModel<EntityModel<KegiatanMonevKriteriaEvaluasi>> findAll(
            String search, 
            Pageable pageable,
            PagedResourcesAssembler<KegiatanMonevKriteriaEvaluasi> assembler) {
        
        Page<KegiatanMonevKriteriaEvaluasi> page;
        
        if (search != null && !search.isEmpty()) {
            // Create specification for searching in aktivitas and catatan fields
            Specification<KegiatanMonevKriteriaEvaluasi> spec = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                
                String searchPattern = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("aktivitas")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("catatan")), searchPattern)
                ));
                
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
            
            page = kegiatanMonevKriteriaEvaluasiRepository.findAll(spec, pageable);
        } else {
            page = kegiatanMonevKriteriaEvaluasiRepository.findAll(pageable);
        }
        
        return assembler.toModel(page);
    }

    /**
     * Find a kriteria evaluasi by ID
     * 
     * @param id the ID of the kriteria evaluasi
     * @return the found kriteria evaluasi
     * @throws EntityNotFoundException if kriteria evaluasi is not found
     */
    public KegiatanMonevKriteriaEvaluasi findById(UUID id) {
        return kegiatanMonevKriteriaEvaluasiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonevKriteriaEvaluasi not found with ID: " + id));
    }

    /**
     * Create a new kriteria evaluasi
     * 
     * @param kriteriaEvaluasi the kriteria evaluasi to create
     * @return the created kriteria evaluasi
     */
    public KegiatanMonevKriteriaEvaluasi create(KegiatanMonevKriteriaEvaluasi kriteriaEvaluasi) {
        return kegiatanMonevKriteriaEvaluasiRepository.save(kriteriaEvaluasi);
    }

    /**
     * Update an existing kriteria evaluasi
     * 
     * @param id the ID of the kriteria evaluasi to update
     * @param kriteriaEvaluasiDto the updated kriteria evaluasi data
     * @return the updated kriteria evaluasi
     * @throws EntityNotFoundException if kriteria evaluasi is not found
     */
    public KegiatanMonevKriteriaEvaluasi update(UUID id, KegiatanMonevKriteriaEvaluasiDto kriteriaEvaluasiDto) {
        KegiatanMonevKriteriaEvaluasi existing = findById(id);
        
        // Update fields from the DTO
        existing.setAktivitas(kriteriaEvaluasiDto.getAktivitas());
        existing.setTarget(kriteriaEvaluasiDto.getTarget());
        existing.setRealisasi(kriteriaEvaluasiDto.getRealisasi());
        existing.setCatatan(kriteriaEvaluasiDto.getCatatan());
        
        // Update parent entity if kegiatanMonevId has changed
        if (kriteriaEvaluasiDto.getKegiatanMonevId() != null && 
            !kriteriaEvaluasiDto.getKegiatanMonevId().equals(existing.getKegiatanMonev().getId())) {
            existing.setKegiatanMonev(kegiatanMonevRepository.findById(kriteriaEvaluasiDto.getKegiatanMonevId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "KegiatanMonev not found with ID: " + kriteriaEvaluasiDto.getKegiatanMonevId())));
        }
        
        return kegiatanMonevKriteriaEvaluasiRepository.save(existing);
    }

    /**
     * Delete a kriteria evaluasi
     * 
     * @param id the ID of the kriteria evaluasi to delete
     * @throws EntityNotFoundException if kriteria evaluasi is not found
     */
    public void delete(UUID id) {
        if (!kegiatanMonevKriteriaEvaluasiRepository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanMonevKriteriaEvaluasi not found with ID: " + id);
        }
        
        kegiatanMonevKriteriaEvaluasiRepository.deleteById(id);
    }
}