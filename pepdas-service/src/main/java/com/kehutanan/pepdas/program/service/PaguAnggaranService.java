package com.kehutanan.pepdas.program.service;

import com.kehutanan.pepdas.program.model.PaguAnggaran;
import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.repository.PaguAnggaranRepository;
import com.kehutanan.pepdas.program.repository.ProgramRepository;
import com.kehutanan.pepdas.kegiatan.model.KegiatanMonev;
import com.kehutanan.pepdas.program.dto.PaguAnggaranDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaguAnggaranService {

    private final PaguAnggaranRepository paguAnggaranRepository;
    private final ProgramRepository programRepository;

    public PagedModel<EntityModel<PaguAnggaran>> findAll(String programId, String search, Pageable pageable,
            PagedResourcesAssembler<PaguAnggaran> assembler) {

        Page<PaguAnggaran> page;

        // Create base specification
        Specification<PaguAnggaran> spec = Specification.where(null);

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
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern)));
        }

        // Execute the query with all applicable filters
        page = paguAnggaranRepository.findAll(spec, pageable);

        return assembler.toModel(page);
    }

    public List<PaguAnggaran> findByProgramId(UUID programId) {
        return paguAnggaranRepository.findByProgramId(programId);
    }

    public PaguAnggaran findById(UUID id) {
        return paguAnggaranRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagu Anggaran not found with id: " + id));
    }

    @Transactional
    public PaguAnggaran create(PaguAnggaranDto dto) {
        Program program = programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + dto.getProgramId()));

        PaguAnggaran paguAnggaran = new PaguAnggaran();
        paguAnggaran.setSumberAnggaran(dto.getSumberAnggaran());
        paguAnggaran.setTahunAnggaran(dto.getTahunAnggaran());
        paguAnggaran.setPagu(dto.getPagu());
        paguAnggaran.setStatus(dto.getStatus());
        paguAnggaran.setKeterangan(dto.getKeterangan());
        paguAnggaran.setProgram(program);

        return paguAnggaranRepository.save(paguAnggaran);
    }

    @Transactional
    public PaguAnggaran update(UUID id, PaguAnggaranDto dto) {
        PaguAnggaran existing = findById(id);
        Program program = programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + dto.getProgramId()));

        existing.setSumberAnggaran(dto.getSumberAnggaran());
        existing.setTahunAnggaran(dto.getTahunAnggaran());
        existing.setPagu(dto.getPagu());
        existing.setStatus(dto.getStatus());
        existing.setKeterangan(dto.getKeterangan());
        existing.setProgram(program);

        return paguAnggaranRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        paguAnggaranRepository.deleteById(id);
    }
}