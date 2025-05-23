package com.kehutanan.pepdas.program.service;

import com.kehutanan.pepdas.program.model.SkemaTanam;
import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.repository.SkemaTanamRepository;
import com.kehutanan.pepdas.program.repository.ProgramRepository;
import com.kehutanan.pepdas.program.dto.SkemaTanamDto;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

@Service
@RequiredArgsConstructor
public class SkemaTanamService {

    private final SkemaTanamRepository skemaTanamRepository;
    private final ProgramRepository programRepository;

    public PagedModel<EntityModel<SkemaTanam>> findAll(String programId, String search, Pageable pageable,
            PagedResourcesAssembler<SkemaTanam> assembler) {

        // Create base specification
        Specification<SkemaTanam> spec = Specification.where(null);

        // Add program ID filter if provided
        if (programId != null && !programId.isEmpty()) {
            try {
                UUID programUuid = UUID.fromString(programId);
                spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("program").get("id"),
                        programUuid));
            } catch (IllegalArgumentException e) {
                // Handle invalid UUID format
                throw new IllegalArgumentException("Invalid Program ID format");
            }
        }

        // Add search filter if provided
        if (search != null && !search.isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("pola")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern)));
        }

        // Execute the query with all applicable filters
        Page<SkemaTanam> page = skemaTanamRepository.findAll(spec, pageable);

        return assembler.toModel(page);
    }

    public List<SkemaTanam> findByProgramId(UUID programId) {
        return skemaTanamRepository.findByProgramId(programId);
    }

    public SkemaTanam findById(UUID id) {
        return skemaTanamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Skema Tanam not found with id: " + id));
    }

    @Transactional
    public SkemaTanam create(SkemaTanamDto dto) {
        Program program = programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + dto.getProgramId()));

        SkemaTanam skemaTanam = new SkemaTanam();
        skemaTanam.setPola(dto.getPola());
        skemaTanam.setJumlahBtgHa(dto.getJumlahBtgHa());
        skemaTanam.setTargetLuas(dto.getTargetLuas());
        skemaTanam.setStatus(dto.getStatus());
        skemaTanam.setKeterangan(dto.getKeterangan());
        skemaTanam.setProgram(program);

        return skemaTanamRepository.save(skemaTanam);
    }

    @Transactional
    public SkemaTanam update(UUID id, SkemaTanamDto dto) {
        SkemaTanam existing = findById(id);
        Program program = programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + dto.getProgramId()));

        existing.setPola(dto.getPola());
        existing.setJumlahBtgHa(dto.getJumlahBtgHa());
        existing.setTargetLuas(dto.getTargetLuas());
        existing.setStatus(dto.getStatus());
        existing.setKeterangan(dto.getKeterangan());
        existing.setProgram(program);

        return skemaTanamRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        skemaTanamRepository.deleteById(id);
    }
}