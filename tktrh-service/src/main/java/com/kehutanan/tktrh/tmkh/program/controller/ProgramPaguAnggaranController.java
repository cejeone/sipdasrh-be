package com.kehutanan.tktrh.tmkh.program.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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

import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.tmkh.program.dto.ProgramPaguAnggaranPageDTO;
import com.kehutanan.tktrh.tmkh.program.model.Program;
import com.kehutanan.tktrh.tmkh.program.model.ProgramPaguAnggaran;
import com.kehutanan.tktrh.tmkh.program.model.dto.ProgramPaguAnggaranDTO;
import com.kehutanan.tktrh.tmkh.program.service.ProgramPaguAnggaranService;
import com.kehutanan.tktrh.tmkh.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/program-pagu-anggaran")
public class ProgramPaguAnggaranController {

    private final ProgramPaguAnggaranService service;
    private final ProgramService programService;
    private final LovService lovService;
    private final PagedResourcesAssembler<ProgramPaguAnggaran> pagedResourcesAssembler;

    @Autowired
    public ProgramPaguAnggaranController(
            ProgramPaguAnggaranService service,
            ProgramService programService,
            LovService lovService,
            PagedResourcesAssembler<ProgramPaguAnggaran> pagedResourcesAssembler) {
        this.service = service;
        this.programService = programService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all ProgramPaguAnggaran with optional filters")
    public ResponseEntity<ProgramPaguAnggaranPageDTO> getAllPaguAnggaran(
            @RequestParam(required = false) String programId,
            @RequestParam(required = false) String sumberAnggaran,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        ProgramPaguAnggaranPageDTO paguAnggaranPage;
        
        String baseUrl = request.getRequestURL().toString();
        
        // Check if any filter is provided
        if ((programId != null && !programId.isEmpty()) || 
            (sumberAnggaran != null && !sumberAnggaran.isEmpty()) || 
            (keterangan != null && !keterangan.isEmpty()) || 
            (status != null && !status.isEmpty())) {
            paguAnggaranPage = service.findByFiltersWithCache(programId, sumberAnggaran, keterangan, status, pageable, baseUrl);
        } else {
            paguAnggaranPage = service.findAllWithCache(pageable, baseUrl);
        }
        
        return ResponseEntity.ok(paguAnggaranPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search ProgramPaguAnggaran by keyword")
    public ResponseEntity<ProgramPaguAnggaranPageDTO> searchPaguAnggaran(
            @RequestParam(required = false) String programId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        ProgramPaguAnggaranPageDTO paguAnggaranPage = service.searchWithCache(
                programId, keyWord, pageable, request.getRequestURL().toString());
        
        return ResponseEntity.ok(paguAnggaranPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ProgramPaguAnggaran by ID")
    public ResponseEntity<ProgramPaguAnggaranDTO> getPaguAnggaranById(@PathVariable Long id) {
        try {
            ProgramPaguAnggaranDTO paguAnggaranDTO = service.findDTOById(id);
            return ResponseEntity.ok(paguAnggaranDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new ProgramPaguAnggaran")
    public ResponseEntity<ProgramPaguAnggaran> createPaguAnggaran(
            @RequestPart String programId,
            @RequestPart String sumberAnggaranId,
            @RequestPart(required = false) String tahunAnggaran,
            @RequestPart(required = false) String pagu,
            @RequestPart String statusId,
            @RequestPart(required = false) String keterangan) {
        
        try {
            ProgramPaguAnggaran newPaguAnggaran = new ProgramPaguAnggaran();
            
            // Set program
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                newPaguAnggaran.setProgram(program);
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            // Set sumber anggaran
            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Long sumberAnggaranIdLong = Long.parseLong(sumberAnggaranId);
                Lov sumberAnggaran = lovService.findById(sumberAnggaranIdLong);
                newPaguAnggaran.setSumberAnggaran(sumberAnggaran);
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            // Set tahun anggaran
            if (tahunAnggaran != null && !tahunAnggaran.isEmpty()) {
                newPaguAnggaran.setTahunAnggaran(Integer.parseInt(tahunAnggaran));
            }
            
            // Set pagu
            if (pagu != null && !pagu.isEmpty()) {
                newPaguAnggaran.setPagu(Double.parseDouble(pagu));
            }
            
            // Set status
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newPaguAnggaran.setStatus(status);
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            // Set keterangan
            newPaguAnggaran.setKeterangan(keterangan);
            
            ProgramPaguAnggaran savedPaguAnggaran = service.save(newPaguAnggaran);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPaguAnggaran);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update existing ProgramPaguAnggaran")
    public ResponseEntity<ProgramPaguAnggaran> updatePaguAnggaran(
            @PathVariable Long id,
            @RequestPart String programId,
            @RequestPart String sumberAnggaranId,
            @RequestPart(required = false) String tahunAnggaran,
            @RequestPart(required = false) String pagu,
            @RequestPart String statusId,
            @RequestPart(required = false) String keterangan) {
        
        try {
            ProgramPaguAnggaran existingPaguAnggaran = service.findById(id);
            
            // Set program
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                existingPaguAnggaran.setProgram(program);
            }
            
            // Set sumber anggaran
            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Long sumberAnggaranIdLong = Long.parseLong(sumberAnggaranId);
                Lov sumberAnggaran = lovService.findById(sumberAnggaranIdLong);
                existingPaguAnggaran.setSumberAnggaran(sumberAnggaran);
            }
            
            // Set tahun anggaran
            if (tahunAnggaran != null && !tahunAnggaran.isEmpty()) {
                existingPaguAnggaran.setTahunAnggaran(Integer.parseInt(tahunAnggaran));
            }
            
            // Set pagu
            if (pagu != null && !pagu.isEmpty()) {
                existingPaguAnggaran.setPagu(Double.parseDouble(pagu));
            }
            
            // Set status
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingPaguAnggaran.setStatus(status);
            }
            
            // Set keterangan
            if (keterangan != null) {
                existingPaguAnggaran.setKeterangan(keterangan);
            }
            
            ProgramPaguAnggaran updatedPaguAnggaran = service.update(id, existingPaguAnggaran);
            return ResponseEntity.ok(updatedPaguAnggaran);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ProgramPaguAnggaran by ID")
    public ResponseEntity<Void> deletePaguAnggaran(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}