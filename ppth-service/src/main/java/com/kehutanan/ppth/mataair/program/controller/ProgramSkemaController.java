package com.kehutanan.ppth.mataair.program.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.ppth.master.model.Lov;
import com.kehutanan.ppth.master.service.LovService;
import com.kehutanan.ppth.mataair.program.dto.ProgramSkemaPageDTO;
import com.kehutanan.ppth.mataair.program.model.Program;
import com.kehutanan.ppth.mataair.program.model.ProgramSkema;
import com.kehutanan.ppth.mataair.program.model.dto.ProgramSkemaDTO;
import com.kehutanan.ppth.mataair.program.service.ProgramService;
import com.kehutanan.ppth.mataair.program.service.ProgramSkemaService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController("mataAirProgramSkemaController")
@RequestMapping("/api/program-skema")
public class ProgramSkemaController {

    private final ProgramSkemaService service;
    private final ProgramService programService;
    private final LovService lovService;

    @Autowired
    public ProgramSkemaController(ProgramSkemaService service, ProgramService programService, LovService lovService) {
        this.service = service;
        this.programService = programService;
        this.lovService = lovService;
    }

    @GetMapping
    public ResponseEntity<ProgramSkemaPageDTO> getAllProgramSkema(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) List<String> kategori,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        ProgramSkemaPageDTO programSkemaPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((programId != null) ||
                (nama != null && !nama.isEmpty()) ||
                (kategori != null && !kategori.isEmpty())) {
            programSkemaPage = service.findByFiltersWithCache(programId, nama, kategori, pageable, baseUrl);
        } else {
            programSkemaPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(programSkemaPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search ProgramSkema by keyword in keterangan")
    public ResponseEntity<ProgramSkemaPageDTO> searchProgramSkema(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        ProgramSkemaPageDTO programSkemaPage = service.searchWithCache(programId, keyWord, pageable,
                request.getRequestURL().toString());
        return ResponseEntity.ok(programSkemaPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramSkemaDTO> getProgramSkemaById(@PathVariable Long id) {
        try {
            ProgramSkemaDTO programSkemaDTO = service.findDTOById(id);
            return ResponseEntity.ok(programSkemaDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProgramSkema> createProgramSkema(
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String skemaBatangHa,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String keterangan) {

        try {
            ProgramSkema programSkema = new ProgramSkema();

            // Set program
            Program program = programService.findById(Long.parseLong(programId));
            programSkema.setProgram(program);

            // Set kategori
            Lov kategori = lovService.findById(Long.parseLong(kategoriId));
            programSkema.setKategoriId(kategori);

            // Set status
            Lov status = lovService.findById(Long.parseLong(statusId));
            programSkema.setStatusId(status);

            // Set other fields
            programSkema.setSkemaBatangHa(Double.parseDouble(skemaBatangHa));
            programSkema.setTargetLuas(Double.parseDouble(targetLuas));
            programSkema.setKeterangan(keterangan);

            ProgramSkema savedProgramSkema = service.save(programSkema);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgramSkema);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProgramSkema> updateProgramSkema(
            @PathVariable Long id,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String skemaBatangHa,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String keterangan) {

        try {
            ProgramSkema existingProgramSkema = service.findById(id);

            // Update program
            Program program = programService.findById(Long.parseLong(programId));
            existingProgramSkema.setProgram(program);

            // Update kategori
            Lov kategori = lovService.findById(Long.parseLong(kategoriId));
            existingProgramSkema.setKategoriId(kategori);

            // Update status
            Lov status = lovService.findById(Long.parseLong(statusId));
            existingProgramSkema.setStatusId(status);

            // Update other fields
            existingProgramSkema.setSkemaBatangHa(Double.parseDouble(skemaBatangHa));

            existingProgramSkema.setTargetLuas(Double.parseDouble(targetLuas));
            existingProgramSkema.setKeterangan(keterangan);

            ProgramSkema updatedProgramSkema = service.update(id, existingProgramSkema);
            return ResponseEntity.ok(updatedProgramSkema);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgramSkema(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}