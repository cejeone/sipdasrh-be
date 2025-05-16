package com.kehutanan.rh.service;

import com.kehutanan.rh.dto.ProgramDto;
import com.kehutanan.rh.model.Program;
import com.kehutanan.rh.model.JenisBibit;
import com.kehutanan.rh.repository.JenisBibitRepository;
import com.kehutanan.rh.repository.PaguAnggaranRepository;
import com.kehutanan.rh.repository.ProgramRepository;
import com.kehutanan.rh.repository.SkemaTanamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final JenisBibitRepository jenisBibitRepository;
    private final PaguAnggaranRepository paguAnggaranRepository;
    private final SkemaTanamRepository skemaTanamRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository, JenisBibitRepository jenisBibitRepository,
                          PaguAnggaranRepository paguAnggaranRepository, SkemaTanamRepository skemaTanamRepository) {
        this.programRepository = programRepository;
        this.jenisBibitRepository = jenisBibitRepository;
        this.paguAnggaranRepository = paguAnggaranRepository;
        this.skemaTanamRepository = skemaTanamRepository;
    }

    public List<Program> findAll() {
        return programRepository.findAll();
    }

    public Optional<Program> findById(UUID id) {
        return programRepository.findById(id);
    }

    public Program save(Program program) {
        return programRepository.save(program);
    }

    public Program update(UUID id, Program program) {
        if (programRepository.existsById(id)) {
            program.setId(id);
            return programRepository.save(program);
        }
        return null;
    }

    public void deleteById(UUID id) {
        programRepository.deleteById(id);
    }

    public ProgramDto getProgramWithDetails(UUID id) {
        Program program = findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + id));

        ProgramDto response = new ProgramDto();
        // Copy program properties
        response.setId(program.getId());
        response.setDirektorat(program.getDirektorat());
        response.setKategori(program.getKategori());
        response.setNama(program.getNama());
        response.setTahunPelaksanaan(program.getTahunPelaksanaan());
        response.setTotalAnggaran(program.getTotalAnggaran());
        response.setTargetLuas(program.getTargetLuas());
        response.setStatus(program.getStatus());

        // Get related data
        response.setJenisBibit(jenisBibitRepository.findByProgram_Id(id));
        response.setPaguAnggaran(paguAnggaranRepository.findByProgram_Id(id));
        response.setSkemaTanam(skemaTanamRepository.findByProgram_Id(id));

        return response;
    }

    public List<JenisBibit> getJenisBibitByProgramId(UUID programId) {
        return jenisBibitRepository.findByProgram_Id(programId);
    }
}