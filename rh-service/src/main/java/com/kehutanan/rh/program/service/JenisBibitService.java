package com.kehutanan.rh.program.service;

import com.kehutanan.rh.program.model.JenisBibit;
import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.program.repository.JenisBibitRepository;
import com.kehutanan.rh.program.repository.ProgramRepository;
import com.kehutanan.rh.program.dto.JenisBibitDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JenisBibitService {
    
    private final JenisBibitRepository jenisBibitRepository;
    private final ProgramRepository programRepository;

    public List<JenisBibit> findAll() {
        return jenisBibitRepository.findAll();
    }

    public List<JenisBibit> findByProgramId(UUID programId) {
        return jenisBibitRepository.findByProgramId(programId);
    }

    public JenisBibit findById(UUID id) {
        return jenisBibitRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Jenis Bibit not found with id: " + id));
    }

    @Transactional
    public JenisBibit create(JenisBibitDTO dto) {
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
    public JenisBibit update(UUID id, JenisBibitDTO dto) {
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