package com.kehutanan.pepdas.program.service;

import com.kehutanan.pepdas.monev.model.Monev;
import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.repository.ProgramRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgramService {
    
    private final ProgramRepository programRepository;

    public Page<Program> findAll(Pageable pageable) {
        return programRepository.findAll(pageable);
    }

    public List<Program> findAll() {
        return programRepository.findAll();
    }

    public Program findById(UUID id) {
        return programRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + id));
    }

    @Transactional
    public Program save(Program program) {
        return programRepository.save(program);
    }

    @Transactional
    public Program update(UUID id, Program program) {
        Program existingProgram = findById(id);
        
        existingProgram.setKategori(program.getKategori());
        existingProgram.setNama(program.getNama());
        existingProgram.setTahunPelaksanaan(program.getTahunPelaksanaan());
        existingProgram.setTotalAnggaran(program.getTotalAnggaran());
        existingProgram.setTargetLuas(program.getTargetLuas());
        existingProgram.setStatus(program.getStatus());
        
        return programRepository.save(existingProgram);
    }

    @Transactional
    public void deleteById(UUID id) {
        programRepository.deleteById(id);
    }

    public Page<Program> findByFilters(String nama, String totalAnggaran, List<String> status, Pageable pageable) {
        Specification<Program> spec = Specification.where(null);
        
        // Add case-insensitive LIKE filter for nama if provided
        if (nama != null && !nama.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nama")),
                        "%" + nama.toLowerCase() + "%"
                    ));
        }
        
        // Add case-insensitive LIKE filter for totalAnggaran if provided
        if (totalAnggaran != null && !totalAnggaran.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("totalAnggaran")),
                        "%" + totalAnggaran.toLowerCase() + "%"
                    ));
        }
        
        // Add IN filter for status if provided
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("status").in(status));
        }
        
        return programRepository.findAll(spec, pageable);
    }
}