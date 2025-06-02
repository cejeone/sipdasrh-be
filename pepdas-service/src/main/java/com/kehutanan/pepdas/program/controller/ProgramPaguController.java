package com.kehutanan.pepdas.program.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kehutanan.pepdas.master.model.Lov;
import com.kehutanan.pepdas.master.service.LovService;
import com.kehutanan.pepdas.program.dto.ProgramPaguDTO;
import com.kehutanan.pepdas.program.dto.ProgramPaguPageDTO;
import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.model.ProgramPagu;
import com.kehutanan.pepdas.program.service.ProgramPaguService;
import com.kehutanan.pepdas.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/program-pagu")
public class ProgramPaguController {

    private final ProgramPaguService service;
    private final ProgramService programService;
    private final LovService lovService;
    
    @Autowired
    public ProgramPaguController(
            ProgramPaguService service,
            ProgramService programService,
            LovService lovService) {
        this.service = service;
        this.programService = programService;
        this.lovService = lovService;
    }

    @GetMapping
    @Operation(summary = "Get all program pagus with pagination and optional filtering")
    public ResponseEntity<ProgramPaguPageDTO> getAllProgramPagus(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String kategori,
            @RequestParam(required = false) String sumberAnggaran,
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        ProgramPaguPageDTO programPaguPage;
        
        String baseUrl = request.getRequestURL().toString();
        
        // Check if any filter is provided
        if (programId != null || 
            (kategori != null && !kategori.isEmpty()) || 
            (sumberAnggaran != null && !sumberAnggaran.isEmpty()) ||
            (bpdas != null && !bpdas.isEmpty())) {
            programPaguPage = service.findByFiltersWithCache(programId, kategori, sumberAnggaran, bpdas, pageable, baseUrl);
        } else {
            programPaguPage = service.findAllWithCache(pageable, baseUrl);
        }
        
        return ResponseEntity.ok(programPaguPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search program pagus by keyword")
    public ResponseEntity<ProgramPaguPageDTO> searchProgramPagus(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        ProgramPaguPageDTO programPaguPage = service.searchWithCache(programId, keyWord, pageable, request.getRequestURL().toString());
        
        return ResponseEntity.ok(programPaguPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get program pagu by ID")
    public ResponseEntity<ProgramPaguDTO> getProgramPaguById(@PathVariable Long id) {
        try {
            ProgramPaguDTO programPaguDTO = service.findDTOById(id);
            return ResponseEntity.ok(programPaguDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new program pagu")
    public ResponseEntity<ProgramPagu> createProgramPagu(
            @RequestPart String programId,
            @RequestPart(required = false) String tahunAnggaran,
            @RequestPart(required = false) String pagu,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String statusId) {

        try {
            ProgramPagu newProgramPagu = new ProgramPagu();
            
            // Set Program (required)
            Program program = programService.findById(Long.parseLong(programId));
            newProgramPagu.setProgram(program);
            
            // Parse integer values
            if (tahunAnggaran != null && !tahunAnggaran.isEmpty()) {
                newProgramPagu.setTahunAnggaran(Integer.parseInt(tahunAnggaran));
            }
            
            // Parse decimal values
            if (pagu != null && !pagu.isEmpty()) {
                newProgramPagu.setPagu(Double.parseDouble(pagu));
            }
            
            newProgramPagu.setKeterangan(keterangan);
            
            // Set relations if IDs are provided
            if (kategoriId != null && !kategoriId.isEmpty()) {
                Long kategoriIdLong = Long.parseLong(kategoriId);
                Lov kategori = lovService.findById(kategoriIdLong);
                newProgramPagu.setKategori(kategori);
            }
            
            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Long sumberAnggaranIdLong = Long.parseLong(sumberAnggaranId);
                Lov sumberAnggaran = lovService.findById(sumberAnggaranIdLong);
                newProgramPagu.setSumberAnggaran(sumberAnggaran);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newProgramPagu.setStatus(status);
            }
            
            ProgramPagu savedProgramPagu = service.save(newProgramPagu);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgramPagu);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing program pagu")
    public ResponseEntity<ProgramPagu> updateProgramPagu(
            @PathVariable Long id,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String tahunAnggaran,
            @RequestPart(required = false) String pagu,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String statusId) {

        try {
            ProgramPagu existingProgramPagu = service.findById(id);
            
            // Update Program if provided
            if (programId != null && !programId.isEmpty()) {
                Program program = programService.findById(Long.parseLong(programId));
                existingProgramPagu.setProgram(program);
            }
            
            // Parse integer values
            if (tahunAnggaran != null && !tahunAnggaran.isEmpty()) {
                existingProgramPagu.setTahunAnggaran(Integer.parseInt(tahunAnggaran));
            }
            
            // Parse decimal values
            if (pagu != null && !pagu.isEmpty()) {
                existingProgramPagu.setPagu(Double.parseDouble(pagu));
            }
            
            if (keterangan != null) {
                existingProgramPagu.setKeterangan(keterangan);
            }
            
            // Update relations if IDs are provided
            if (kategoriId != null && !kategoriId.isEmpty()) {
                Long kategoriIdLong = Long.parseLong(kategoriId);
                Lov kategori = lovService.findById(kategoriIdLong);
                existingProgramPagu.setKategori(kategori);
            }
            
            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Long sumberAnggaranIdLong = Long.parseLong(sumberAnggaranId);
                Lov sumberAnggaran = lovService.findById(sumberAnggaranIdLong);
                existingProgramPagu.setSumberAnggaran(sumberAnggaran);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingProgramPagu.setStatus(status);
            }
            
            ProgramPagu updatedProgramPagu = service.update(id, existingProgramPagu);
            return ResponseEntity.ok(updatedProgramPagu);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a program pagu by ID")
    public ResponseEntity<Void> deleteProgramPagu(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}