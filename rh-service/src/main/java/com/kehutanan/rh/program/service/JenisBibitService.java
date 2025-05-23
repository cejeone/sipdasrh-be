package com.kehutanan.rh.program.service;

import com.kehutanan.rh.program.model.JenisBibit;
import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.program.repository.JenisBibitRepository;
import com.kehutanan.rh.program.repository.ProgramRepository;
import com.kehutanan.rh.program.dto.JenisBibitDto;

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
public class JenisBibitService {

    private final JenisBibitRepository jenisBibitRepository;
    private final ProgramRepository programRepository;

    public PagedModel<EntityModel<JenisBibit>> findAll(String programId, String search, Pageable pageable,
            PagedResourcesAssembler<JenisBibit> assembler) {

        Page<JenisBibit> page;

        // Create base specification
        Specification<JenisBibit> spec = Specification.where(null);

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
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("namaBibit")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kategori")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("sumberBibit")), searchPattern)));
        }

        // Execute the query with all applicable filters
        page = jenisBibitRepository.findAll(spec, pageable);

        return assembler.toModel(page);
    }

    public List<JenisBibit> findByProgramId(UUID programId) {
        return jenisBibitRepository.findByProgramId(programId);
    }

    public JenisBibit findById(UUID id) {
        return jenisBibitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Jenis Bibit not found with id: " + id));
    }

    @Transactional
    public JenisBibit create(JenisBibitDto dto) {
        Program program = programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + dto.getProgramId()));

        JenisBibit jenisBibit = new JenisBibit();
        jenisBibit.setKategori(dto.getKategori());
        jenisBibit.setNamaBibit(dto.getNamaBibit());
        jenisBibit.setSumberBibit(dto.getSumberBibit());
        jenisBibit.setJumlah(dto.getJumlah());
        jenisBibit.setStatus(dto.getStatus());
        jenisBibit.setKeterangan(dto.getKeterangan());
        jenisBibit.setProgram(program);

        return jenisBibitRepository.save(jenisBibit);
    }

    @Transactional
    public JenisBibit update(UUID id, JenisBibitDto dto) {
        JenisBibit existing = findById(id);
        Program program = programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + dto.getProgramId()));

        existing.setKategori(dto.getKategori());
        existing.setNamaBibit(dto.getNamaBibit());
        existing.setSumberBibit(dto.getSumberBibit());
        existing.setJumlah(dto.getJumlah());
        existing.setStatus(dto.getStatus());
        existing.setKeterangan(dto.getKeterangan());
        existing.setProgram(program);

        return jenisBibitRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        jenisBibitRepository.deleteById(id);
    }
}