package com.kehutanan.rh.service;

import com.kehutanan.rh.model.JenisBibit;
import com.kehutanan.rh.model.Program;
import com.kehutanan.rh.repository.JenisBibitRepository;
import com.kehutanan.rh.repository.ProgramRepository;
import com.kehutanan.rh.dto.JenisBibitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JenisBibitService {

    private final JenisBibitRepository jenisBibitRepository;
    private final ProgramRepository programRepository;

    @Autowired
    public JenisBibitService(JenisBibitRepository jenisBibitRepository, 
                            ProgramRepository programRepository) {
        this.jenisBibitRepository = jenisBibitRepository;
        this.programRepository = programRepository;
    }

    public List<JenisBibit> findAll() {
        return jenisBibitRepository.findAll();
    }

    public List<JenisBibit> findByProgramId(UUID programId) {
        return jenisBibitRepository.findByProgram_Id(programId);
    }

    public Optional<JenisBibit> findById(UUID id) {
        return jenisBibitRepository.findById(id);
    }

    public JenisBibit save(JenisBibit jenisBibit) {
        // Verify that program exists
        programRepository.findById(jenisBibit.getProgram().getId())
            .orElseThrow(() -> new EntityNotFoundException("Program not found"));
        return jenisBibitRepository.save(jenisBibit);
    }

    public JenisBibit update(UUID id, JenisBibit jenisBibit) {
        if (jenisBibitRepository.existsById(id)) {
            // Verify that program exists
            programRepository.findById(jenisBibit.getProgram().getId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found"));
            jenisBibit.setId(id);
            return jenisBibitRepository.save(jenisBibit);
        }
        return null;
    }

    public JenisBibit update(UUID id, UUID programId, String kategori, 
        String namaBibit, String sumberBibit, Integer jumlah, 
        String status, String keterangan) {
        
        if (!jenisBibitRepository.existsById(id)) {
            throw new EntityNotFoundException("Jenis Bibit not found");
        }

        Program program = programRepository.findById(programId)
            .orElseThrow(() -> new EntityNotFoundException(
                String.format("Program with ID %s not found", programId)
            ));

        JenisBibit jenisBibit = new JenisBibit();
        jenisBibit.setId(id);
        jenisBibit.setKategori(kategori);
        jenisBibit.setNamaBibit(namaBibit);
        jenisBibit.setSumberBibit(sumberBibit);
        jenisBibit.setJumlah(jumlah);
        jenisBibit.setStatus(status);
        jenisBibit.setKeterangan(keterangan);
        jenisBibit.setProgram(program);

        return jenisBibitRepository.save(jenisBibit);
    }

    public void deleteById(UUID id) {
        jenisBibitRepository.deleteById(id);
    }

    public JenisBibit createFromRequest(JenisBibitDto request) {
        Program program = programRepository.findById(request.getProgramId())
            .orElseThrow(() -> new EntityNotFoundException("Program not found"));

        JenisBibit jenisBibit = new JenisBibit();
        jenisBibit.setKategori(request.getKategori());
        jenisBibit.setNamaBibit(request.getNamaBibit());
        jenisBibit.setSumberBibit(request.getSumberBibit());
        jenisBibit.setJumlah(request.getJumlah());
        jenisBibit.setStatus(request.getStatus());
        jenisBibit.setKeterangan(request.getKeterangan());
        jenisBibit.setProgram(program);

        return save(jenisBibit);
    }
}