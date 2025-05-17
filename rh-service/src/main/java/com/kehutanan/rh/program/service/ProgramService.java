package com.kehutanan.rh.program.service;

import com.kehutanan.rh.monev.model.Monev;
import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.program.repository.ProgramRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
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
}