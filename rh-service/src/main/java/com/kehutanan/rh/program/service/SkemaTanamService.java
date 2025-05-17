package com.kehutanan.rh.program.service;

import com.kehutanan.rh.program.model.SkemaTanam;
import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.program.repository.SkemaTanamRepository;
import com.kehutanan.rh.program.repository.ProgramRepository;
import com.kehutanan.rh.program.dto.SkemaTanamDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkemaTanamService {
    
    private final SkemaTanamRepository skemaTanamRepository;
    private final ProgramRepository programRepository;

    public List<SkemaTanam> findAll() {
        return skemaTanamRepository.findAll();
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