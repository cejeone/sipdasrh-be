package com.kehutanan.rh.program.service;

import com.kehutanan.rh.program.model.SkemaTanam;
import com.kehutanan.rh.program.model.Program;  // Add this import
import com.kehutanan.rh.program.repository.SkemaTanamRepository;
import com.kehutanan.rh.program.repository.ProgramRepository;
import com.kehutanan.rh.program.dto.SkemaTanamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;

@Service
public class SkemaTanamService {

    private final SkemaTanamRepository skemaTanamRepository;
    private final ProgramRepository programRepository;

    @Autowired
    public SkemaTanamService(SkemaTanamRepository skemaTanamRepository,
                             ProgramRepository programRepository) {
        this.skemaTanamRepository = skemaTanamRepository;
        this.programRepository = programRepository;
    }

    public List<SkemaTanam> findAll() {
        return skemaTanamRepository.findAll();
    }

    public Optional<SkemaTanam> findById(UUID id) {
        return skemaTanamRepository.findById(id);
    }

    public List<SkemaTanam> findByProgramId(UUID programId) {
        return skemaTanamRepository.findByProgram_Id(programId);
    }

    public SkemaTanam save(SkemaTanam skemaTanam) {
        // Verify that program exists
        programRepository.findById(skemaTanam.getProgram().getId())
            .orElseThrow(() -> new EntityNotFoundException("Program not found"));
        return skemaTanamRepository.save(skemaTanam);
    }

    public SkemaTanam update(UUID id, SkemaTanam skemaTanam) {
        if (skemaTanamRepository.existsById(id)) {
            // Verify that program exists
            programRepository.findById(skemaTanam.getProgram().getId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found"));
            skemaTanam.setId(id);
            return skemaTanamRepository.save(skemaTanam);
        }
        return null;
    }

    public SkemaTanam update(UUID id, UUID programId, String kategori, 
            BigDecimal skemaBtgHa, BigDecimal targetLuas, 
            String status, String keterangan) {
        
        if (!skemaTanamRepository.existsById(id)) {
            throw new EntityNotFoundException(
                String.format("SkemaTanam with ID %s not found", id)
            );
        }

        Program program = programRepository.findById(programId)
            .orElseThrow(() -> new EntityNotFoundException(
                String.format("Program with ID %s not found", programId)
            ));

        SkemaTanam skemaTanam = skemaTanamRepository.findById(id).get();
        skemaTanam.setKategori(kategori);
        skemaTanam.setSkemaBtgHa(skemaBtgHa);
        skemaTanam.setTargetLuas(targetLuas);
        skemaTanam.setStatus(status);
        skemaTanam.setKeterangan(keterangan);
        skemaTanam.setProgram(program);

        return skemaTanamRepository.save(skemaTanam);
    }

    public void deleteById(UUID id) {
        skemaTanamRepository.deleteById(id);
    }

    public SkemaTanam createFromRequest(SkemaTanamDto request) {
        Program program = programRepository.findById(request.getProgramId())
            .orElseThrow(() -> new EntityNotFoundException(
                String.format("Program with ID %s not found", request.getProgramId())
            ));

        SkemaTanam skemaTanam = new SkemaTanam();
        skemaTanam.setKategori(request.getKategori());
        skemaTanam.setSkemaBtgHa(request.getSkemaBtgHa());
        skemaTanam.setTargetLuas(request.getTargetLuas());
        skemaTanam.setStatus(request.getStatus());
        skemaTanam.setKeterangan(request.getKeterangan());
        skemaTanam.setProgram(program);

        return save(skemaTanam);
    }
}