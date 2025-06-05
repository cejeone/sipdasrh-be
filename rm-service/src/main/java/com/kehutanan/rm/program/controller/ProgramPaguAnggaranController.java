package com.kehutanan.rm.program.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kehutanan.rm.master.model.Lov;
import com.kehutanan.rm.master.service.LovService;
import com.kehutanan.rm.program.dto.ProgramPaguAnggaranPageDTO;
import com.kehutanan.rm.program.model.Program;
import com.kehutanan.rm.program.model.ProgramPaguAnggaran;
import com.kehutanan.rm.program.model.dto.ProgramPaguAnggaranDTO;
import com.kehutanan.rm.program.service.ProgramPaguAnggaranService;
import com.kehutanan.rm.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/program-pagu-anggaran")
public class ProgramPaguAnggaranController {
    private final ProgramPaguAnggaranService service;
    private final LovService lovService;
    private final ProgramService programService;

    @Autowired
    public ProgramPaguAnggaranController(ProgramPaguAnggaranService service, LovService lovService,
            ProgramService programService) {
        this.service = service;
        this.lovService = lovService;
        this.programService = programService;
    }


    @GetMapping
    public ResponseEntity<ProgramPaguAnggaranPageDTO> getAllProgramPaguAnggaran(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String sumberAnggaran,
            @RequestParam(required = false) List<String> kategori,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        ProgramPaguAnggaranPageDTO programPaguAnggaranPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if (programId != null ||
                (sumberAnggaran != null && !sumberAnggaran.isEmpty()) ||
                (kategori != null && !kategori.isEmpty())) {
            programPaguAnggaranPage = service.findByFiltersWithCache(programId, sumberAnggaran, kategori, pageable,
                    baseUrl);
        } else {
            programPaguAnggaranPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(programPaguAnggaranPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search ProgramPaguAnggaran by keterangan")
    public ResponseEntity<ProgramPaguAnggaranPageDTO> searchProgramPaguAnggaran(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        ProgramPaguAnggaranPageDTO programPaguAnggaranPage = service.searchWithCache(programId, keyWord, pageable,
                request.getRequestURL().toString());

        return ResponseEntity.ok(programPaguAnggaranPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramPaguAnggaranDTO> getProgramPaguAnggaranById(@PathVariable Long id) {
        try {
            ProgramPaguAnggaranDTO programPaguAnggaranDTO = service.findDTOById(id);
            return ResponseEntity.ok(programPaguAnggaranDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProgramPaguAnggaran> createProgramPaguAnggaran(
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String tahunAnggaran,
            @RequestPart(required = false) String pagu,
            @RequestPart(required = false) String keterangan) {

        try {
            ProgramPaguAnggaran programPaguAnggaran = new ProgramPaguAnggaran();

            Program program = programService.findById(Long.parseLong(programId));
            programPaguAnggaran.setProgram(program);

            Lov kategori = lovService.findById(Long.parseLong(kategoriId));
            programPaguAnggaran.setKategoriId(kategori);

            Lov sumberAnggaran = lovService.findById(Long.parseLong(sumberAnggaranId));
            programPaguAnggaran.setSumberAnggaranId(sumberAnggaran);

            Lov status = lovService.findById(Long.parseLong(statusId));
            programPaguAnggaran.setStatusId(status);

            programPaguAnggaran.setTahunAnggaran(Integer.parseInt(tahunAnggaran));

            programPaguAnggaran.setPagu(Double.parseDouble(pagu));

            programPaguAnggaran.setKeterangan(keterangan);

            ProgramPaguAnggaran savedProgramPaguAnggaran = service.save(programPaguAnggaran);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgramPaguAnggaran);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProgramPaguAnggaran> updateProgramPaguAnggaran(
            @PathVariable Long id,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String tahunAnggaran,
            @RequestPart(required = false) String pagu,
            @RequestPart(required = false) String keterangan) {

        try {
            ProgramPaguAnggaran programPaguAnggaran = service.findById(id);

            Program program = programService.findById(Long.parseLong(programId));
            programPaguAnggaran.setProgram(program);

            Lov kategori = lovService.findById(Long.parseLong(kategoriId));
            programPaguAnggaran.setKategoriId(kategori);

            Lov sumberAnggaran = lovService.findById(Long.parseLong(sumberAnggaranId));
            programPaguAnggaran.setSumberAnggaranId(sumberAnggaran);

            Lov status = lovService.findById(Long.parseLong(statusId));
            programPaguAnggaran.setStatusId(status);

            programPaguAnggaran.setTahunAnggaran(Integer.parseInt(tahunAnggaran));

            programPaguAnggaran.setPagu(Double.parseDouble(pagu));

            programPaguAnggaran.setKeterangan(keterangan);

            ProgramPaguAnggaran updatedProgramPaguAnggaran = service.update(id, programPaguAnggaran);
            return ResponseEntity.ok(updatedProgramPaguAnggaran);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgramPaguAnggaran(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}