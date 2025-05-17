package com.kehutanan.rh.program.service;

import com.kehutanan.rh.program.model.PaguAnggaran;
import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.program.repository.PaguAnggaranRepository;
import com.kehutanan.rh.program.repository.ProgramRepository;
import com.kehutanan.rh.program.dto.PaguAnggaranDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaguAnggaranService {
    
    private final PaguAnggaranRepository paguAnggaranRepository;
    private final ProgramRepository programRepository;

    public List<PaguAnggaran> findAll() {
        return paguAnggaranRepository.findAll();
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