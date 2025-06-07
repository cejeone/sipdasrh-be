package com.kehutanan.tktrh.ppkh.program.controller;

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
import com.kehutanan.tktrh.ppkh.program.dto.ProgramPaguAnggaranPageDTO;
import com.kehutanan.tktrh.ppkh.program.model.Program;
import com.kehutanan.tktrh.ppkh.program.model.ProgramPaguAnggaran;
import com.kehutanan.tktrh.ppkh.program.model.dto.ProgramPaguAnggaranDTO;
import com.kehutanan.tktrh.ppkh.program.service.ProgramPaguAnggaranService;
import com.kehutanan.tktrh.ppkh.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController("ppkhProgramPaguAnggaranController")
@RequestMapping("/api/ppkh/program-pagu-anggaran")
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
    @Operation(summary = "Get all Program Pagu Anggaran with optional filtering")
    public ResponseEntity<ProgramPaguAnggaranPageDTO> getAllProgramPaguAnggaran(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String sumberAnggaran,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        ProgramPaguAnggaranPageDTO programPaguAnggaranPage;
        
        String baseUrl = request.getRequestURL().toString();
        
        // Check if any filter is provided
        if ((programId != null) || 
            (sumberAnggaran != null && !sumberAnggaran.isEmpty()) || 
            (keterangan != null && !keterangan.isEmpty()) || 
            (status != null && !status.isEmpty())) {
            
            programPaguAnggaranPage = service.findByFiltersWithCache(
                programId, sumberAnggaran, keterangan, status, pageable, baseUrl);
        } else {
            programPaguAnggaranPage = service.findAllWithCache(pageable, baseUrl);
        }
        
        return ResponseEntity.ok(programPaguAnggaranPage);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search Program Pagu Anggaran by keyword")
    public ResponseEntity<ProgramPaguAnggaranPageDTO> searchProgramPaguAnggaran(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        ProgramPaguAnggaranPageDTO programPaguAnggaranPage = 
            service.searchWithCache(programId, keyWord, pageable, request.getRequestURL().toString());
        
        return ResponseEntity.ok(programPaguAnggaranPage);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get Program Pagu Anggaran by ID")
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
    @Operation(summary = "Create a new Program Pagu Anggaran")
    public ResponseEntity<ProgramPaguAnggaran> createProgramPaguAnggaran(
            @RequestPart(required = true) String programId,
            @RequestPart(required = true) String sumberAnggaranId,
            @RequestPart(required = true) String tahunAnggaran,
            @RequestPart(required = true) String pagu,
            @RequestPart(required = true) String statusId,
            @RequestPart(required = false) String keterangan) {
        
        try {
            ProgramPaguAnggaran newProgramPaguAnggaran = new ProgramPaguAnggaran();
            
            // Set program
            Long programIdLong = Long.parseLong(programId);
            Program program = programService.findById(programIdLong);
            newProgramPaguAnggaran.setProgram(program);
            
            // Set sumber anggaran
            Long sumberAnggaranIdLong = Long.parseLong(sumberAnggaranId);
            Lov sumberAnggaran = lovService.findById(sumberAnggaranIdLong);
            newProgramPaguAnggaran.setSumberAnggaran(sumberAnggaran);
            
            // Set tahun anggaran
            Integer tahunAnggaranInt = Integer.parseInt(tahunAnggaran);
            newProgramPaguAnggaran.setTahunAnggaran(tahunAnggaranInt);
            
            // Set pagu
            Double paguDouble = Double.parseDouble(pagu);
            newProgramPaguAnggaran.setPagu(paguDouble);
            
            // Set status
            Long statusIdLong = Long.parseLong(statusId);
            Lov status = lovService.findById(statusIdLong);
            newProgramPaguAnggaran.setStatus(status);
            
            // Set keterangan
            newProgramPaguAnggaran.setKeterangan(keterangan);
            
            ProgramPaguAnggaran savedProgramPaguAnggaran = service.save(newProgramPaguAnggaran);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgramPaguAnggaran);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing Program Pagu Anggaran")
    public ResponseEntity<ProgramPaguAnggaran> updateProgramPaguAnggaran(
            @PathVariable Long id,
            @RequestPart(required = true) String programId,
            @RequestPart(required = true) String sumberAnggaranId,
            @RequestPart(required = true) String tahunAnggaran,
            @RequestPart(required = true) String pagu,
            @RequestPart(required = true) String statusId,
            @RequestPart(required = false) String keterangan) {
        
        try {
            ProgramPaguAnggaran existingProgramPaguAnggaran = service.findById(id);
            
            // Update program
            Long programIdLong = Long.parseLong(programId);
            Program program = programService.findById(programIdLong);
            existingProgramPaguAnggaran.setProgram(program);
            
            // Update sumber anggaran
            Long sumberAnggaranIdLong = Long.parseLong(sumberAnggaranId);
            Lov sumberAnggaran = lovService.findById(sumberAnggaranIdLong);
            existingProgramPaguAnggaran.setSumberAnggaran(sumberAnggaran);
            
            // Update tahun anggaran
            Integer tahunAnggaranInt = Integer.parseInt(tahunAnggaran);
            existingProgramPaguAnggaran.setTahunAnggaran(tahunAnggaranInt);
            
            // Update pagu
            Double paguDouble = Double.parseDouble(pagu);
            existingProgramPaguAnggaran.setPagu(paguDouble);
            
            // Update status
            Long statusIdLong = Long.parseLong(statusId);
            Lov status = lovService.findById(statusIdLong);
            existingProgramPaguAnggaran.setStatus(status);
            
            // Update keterangan
            existingProgramPaguAnggaran.setKeterangan(keterangan);
            
            ProgramPaguAnggaran updatedProgramPaguAnggaran = service.update(id, existingProgramPaguAnggaran);
            return ResponseEntity.ok(updatedProgramPaguAnggaran);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Program Pagu Anggaran")
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