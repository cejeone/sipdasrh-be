package com.kehutanan.rh.service;

import com.kehutanan.rh.model.PaguAnggaran;
import com.kehutanan.rh.model.Program;
import com.kehutanan.rh.repository.PaguAnggaranRepository;
import com.kehutanan.rh.repository.ProgramRepository;
import com.kehutanan.rh.dto.PaguAnggaranDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;

@Service
public class PaguAnggaranService {

    private final PaguAnggaranRepository paguAnggaranRepository;
    private final ProgramRepository programRepository;

    @Autowired
    public PaguAnggaranService(PaguAnggaranRepository paguAnggaranRepository,
                              ProgramRepository programRepository) {
        this.paguAnggaranRepository = paguAnggaranRepository;
        this.programRepository = programRepository;
    }

    public List<PaguAnggaran> findAll() {
        return paguAnggaranRepository.findAll();
    }

    public List<PaguAnggaran> findByProgramId(UUID programId) {
        return paguAnggaranRepository.findByProgram_Id(programId);
    }

    public Optional<PaguAnggaran> findById(UUID id) {
        return paguAnggaranRepository.findById(id);
    }

    public PaguAnggaran save(PaguAnggaran paguAnggaran) {
        // Verify that program exists
        programRepository.findById(paguAnggaran.getProgram().getId())
            .orElseThrow(() -> new EntityNotFoundException("Program not found"));
        return paguAnggaranRepository.save(paguAnggaran);
    }

    public PaguAnggaran update(UUID id, PaguAnggaran paguAnggaran) {
        if (paguAnggaranRepository.existsById(id)) {
            // Verify that program exists
            programRepository.findById(paguAnggaran.getProgram().getId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found"));
            paguAnggaran.setId(id);
            return paguAnggaranRepository.save(paguAnggaran);
        }
        return null;
    }

    public PaguAnggaran update(UUID id, UUID programId, String kategori, 
            String sumberAnggaran, Integer tahunAnggaran,
            BigDecimal pagu, String status, String keterangan) {
        
        if (!paguAnggaranRepository.existsById(id)) {
            throw new EntityNotFoundException(
                String.format("PaguAnggaran with ID %s not found", id)
            );
        }

        Program program = programRepository.findById(programId)
            .orElseThrow(() -> new EntityNotFoundException(
                String.format("Program with ID %s not found", programId)
            ));

        PaguAnggaran paguAnggaran = paguAnggaranRepository.findById(id).get();
        paguAnggaran.setKategori(kategori);
        paguAnggaran.setSumberAnggaran(sumberAnggaran);
        paguAnggaran.setTahunAnggaran(tahunAnggaran);
        paguAnggaran.setPagu(pagu);
        paguAnggaran.setStatus(status);
        paguAnggaran.setKeterangan(keterangan);
        paguAnggaran.setProgram(program);

        return paguAnggaranRepository.save(paguAnggaran);
    }

    public void deleteById(UUID id) {
        paguAnggaranRepository.deleteById(id);
    }

    public PaguAnggaran createFromRequest(PaguAnggaranDto request) {
        Program program = programRepository.findById(request.getProgramId())
            .orElseThrow(() -> new EntityNotFoundException(
                String.format("Program with ID %s not found", request.getProgramId())
            ));

        PaguAnggaran paguAnggaran = new PaguAnggaran();
        paguAnggaran.setKategori(request.getKategori());
        paguAnggaran.setSumberAnggaran(request.getSumberAnggaran());
        paguAnggaran.setTahunAnggaran(request.getTahunAnggaran());
        paguAnggaran.setPagu(request.getPagu());
        paguAnggaran.setStatus(request.getStatus());
        paguAnggaran.setKeterangan(request.getKeterangan());
        paguAnggaran.setProgram(program);

        return save(paguAnggaran);
    }
}