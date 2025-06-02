package com.kehutanan.pepdas.program.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kehutanan.pepdas.master.model.Eselon1;
import com.kehutanan.pepdas.master.model.Lov;
import com.kehutanan.pepdas.master.service.Eselon1Service;
import com.kehutanan.pepdas.master.service.LovService;
import com.kehutanan.pepdas.program.dto.ProgramDTO;
import com.kehutanan.pepdas.program.dto.ProgramPageDTO;
import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/program")
public class ProgramController {

    private final ProgramService service;
    private final Eselon1Service eselon1Service;
    private final LovService lovService;
    
    @Autowired
    public ProgramController(
            ProgramService service,
            Eselon1Service eselon1Service,
            LovService lovService) {
        this.service = service;
        this.eselon1Service = eselon1Service;
        this.lovService = lovService;
    }

    @GetMapping
    @Operation(summary = "Get all programs with pagination and optional filtering")
    public ResponseEntity<ProgramPageDTO> getAllPrograms(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String anggaran,
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        ProgramPageDTO programPage;
        
        String baseUrl = request.getRequestURL().toString();
        
        // Check if any filter is provided
        if ((nama != null && !nama.isEmpty()) || 
            (anggaran != null && !anggaran.isEmpty()) ||
            (bpdas != null && !bpdas.isEmpty())) {
            programPage = service.findByFiltersWithCache(nama, anggaran, bpdas, pageable, baseUrl);
        } else {
            programPage = service.findAllWithCache(pageable, baseUrl);
        }
        
        return ResponseEntity.ok(programPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search programs by keyword")
    public ResponseEntity<ProgramPageDTO> searchPrograms(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        ProgramPageDTO programPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        
        return ResponseEntity.ok(programPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get program by ID")
    public ResponseEntity<ProgramDTO> getProgramById(@PathVariable Long id) {
        try {
            ProgramDTO programDTO = service.findDTOById(id);
            return ResponseEntity.ok(programDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new program")
    public ResponseEntity<Program> createProgram(
            @RequestPart String nama,
            @RequestPart(required = false) String tahunRencana,
            @RequestPart(required = false) String totalAnggaran,
            @RequestPart(required = false) String eselon1Id,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String statusId) {

        try {
            Program newProgram = new Program();
            newProgram.setNama(nama);
            
            // Parse integer values
            if (tahunRencana != null && !tahunRencana.isEmpty()) {
                newProgram.setTahunRencana(Integer.parseInt(tahunRencana));
            }
            
            // Parse decimal values
            if (totalAnggaran != null && !totalAnggaran.isEmpty()) {
                newProgram.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            }
            
            // Set relations if IDs are provided
            if (eselon1Id != null && !eselon1Id.isEmpty()) {
                Long eselon1IdLong = Long.parseLong(eselon1Id);
                Eselon1 eselon1 = eselon1Service.findById(eselon1IdLong);
                newProgram.setEselon1(eselon1);
            }
            
            if (kategoriId != null && !kategoriId.isEmpty()) {
                Long kategoriIdLong = Long.parseLong(kategoriId);
                Lov kategori = lovService.findById(kategoriIdLong);
                newProgram.setKategori(kategori);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newProgram.setStatus(status);
            }
            
            Program savedProgram = service.save(newProgram);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgram);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing program")
    public ResponseEntity<Program> updateProgram(
            @PathVariable Long id,
            @RequestPart String nama,
            @RequestPart(required = false) String tahunRencana,
            @RequestPart(required = false) String totalAnggaran,
            @RequestPart(required = false) String eselon1Id,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String statusId) {

        try {
            Program existingProgram = service.findById(id);
            
            existingProgram.setNama(nama);
            
            // Parse integer values
            if (tahunRencana != null && !tahunRencana.isEmpty()) {
                existingProgram.setTahunRencana(Integer.parseInt(tahunRencana));
            }
            
            // Parse decimal values
            if (totalAnggaran != null && !totalAnggaran.isEmpty()) {
                existingProgram.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            }
            
            // Update relations if IDs are provided
            if (eselon1Id != null && !eselon1Id.isEmpty()) {
                Long eselon1IdLong = Long.parseLong(eselon1Id);
                Eselon1 eselon1 = eselon1Service.findById(eselon1IdLong);
                existingProgram.setEselon1(eselon1);
            }
            
            if (kategoriId != null && !kategoriId.isEmpty()) {
                Long kategoriIdLong = Long.parseLong(kategoriId);
                Lov kategori = lovService.findById(kategoriIdLong);
                existingProgram.setKategori(kategori);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingProgram.setStatus(status);
            }
            
            Program updatedProgram = service.update(id, existingProgram);
            return ResponseEntity.ok(updatedProgram);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a program by ID")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}