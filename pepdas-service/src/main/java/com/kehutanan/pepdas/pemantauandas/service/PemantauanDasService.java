package com.kehutanan.pepdas.pemantauandas.service;

import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasMapper;
import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasRequestDto;
import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;
import com.kehutanan.pepdas.pemantauandas.repository.PemantauanDasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PemantauanDasService {

    private final PemantauanDasRepository pemantauanDasRepository;

    @Autowired
    public PemantauanDasService(PemantauanDasRepository pemantauanDasRepository) {
        this.pemantauanDasRepository = pemantauanDasRepository;
    }

    public Page<PemantauanDas> findAll(Pageable pageable) {
        return pemantauanDasRepository.findAll(pageable);
    }

    public Page<PemantauanDas> findByFilters(String bpdas,
                                          String das, Pageable pageable) {
        Specification<PemantauanDas> spec = Specification.where(null);


        if (bpdas != null && !bpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("bpdas")),
                    "%" + bpdas.toLowerCase() + "%"));
        }

        if (das != null && !das.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("das")),
                    "%" + das.toLowerCase() + "%"));
        }

        return pemantauanDasRepository.findAll(spec, pageable);
    }

    public PemantauanDas findById(UUID id) {
        return pemantauanDasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PemantauanDas not found with id: " + id));
    }

    public PemantauanDas create(PemantauanDasRequestDto pemantauanDasDto) {
        PemantauanDas newPemantauanDas = PemantauanDasMapper.toEntity(pemantauanDasDto);
        return pemantauanDasRepository.save(newPemantauanDas);
    }

    public PemantauanDas update(UUID id, PemantauanDasRequestDto pemantauanDasDto) {
        PemantauanDas existingPemantauanDas = pemantauanDasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PemantauanDas not found with id: " + id));

        // Update fields from DTO to entity
        PemantauanDasMapper.updateEntityFromDto(pemantauanDasDto, existingPemantauanDas);
        
        return pemantauanDasRepository.save(existingPemantauanDas);
    }

    public void delete(UUID id) {
        if (!pemantauanDasRepository.existsById(id)) {
            throw new EntityNotFoundException("PemantauanDas not found with id: " + id);
        }
        pemantauanDasRepository.deleteById(id);
    }
}