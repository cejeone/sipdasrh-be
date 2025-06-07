package com.kehutanan.tktrh.tmkh.monev.controller;

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

import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.service.BpdasService;
import com.kehutanan.tktrh.tmkh.monev.dto.MonevPusatPageDTO;
import com.kehutanan.tktrh.tmkh.monev.model.MonevPusat;
import com.kehutanan.tktrh.tmkh.monev.model.dto.MonevPusatDTO;
import com.kehutanan.tktrh.tmkh.monev.service.MonevPusatService;
import com.kehutanan.tktrh.tmkh.program.model.Program;
import com.kehutanan.tktrh.tmkh.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController("tmkhMonevPusatController")
@RequestMapping("/api/tmkh/monev-pusat")
public class MonevPusatController {

    private final MonevPusatService service;
    private final ProgramService programService;
    private final BpdasService bpdasService;
    private final PagedResourcesAssembler<MonevPusat> pagedResourcesAssembler;

    @Autowired
    public MonevPusatController(
            MonevPusatService service,
            ProgramService programService,
            BpdasService bpdasService,
            PagedResourcesAssembler<MonevPusat> pagedResourcesAssembler) {
        this.service = service;
        this.programService = programService;
        this.bpdasService = bpdasService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all MonevPusat data with optional filters")
    public ResponseEntity<MonevPusatPageDTO> getAllMonevPusat(
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> bpdasList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        MonevPusatPageDTO monevPusatPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((keterangan != null && !keterangan.isEmpty()) || 
                (bpdasList != null && !bpdasList.isEmpty())) {
            monevPusatPage = service.findByFiltersWithCache(keterangan, bpdasList, pageable, baseUrl);
        } else {
            monevPusatPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(monevPusatPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search MonevPusat by keyword")
    public ResponseEntity<MonevPusatPageDTO> searchMonevPusat(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        MonevPusatPageDTO monevPusatPage = service.searchWithCache(
            keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(monevPusatPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get MonevPusat by ID")
    public ResponseEntity<MonevPusatDTO> getMonevPusatById(@PathVariable Long id) {
        try {
            MonevPusatDTO monevPusatDTO = service.findDTOById(id);
            return ResponseEntity.ok(monevPusatDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new MonevPusat record")
    public ResponseEntity<MonevPusat> createMonevPusat(
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String luasTotalTarget,
            @RequestPart(required = false) String luasTotalRealisasi,
            @RequestPart(required = false) String targetT1,
            @RequestPart(required = false) String realisasiT1,
            @RequestPart(required = false) String targetP0,
            @RequestPart(required = false) String realisasiP0,
            @RequestPart(required = false) String targetP1,
            @RequestPart(required = false) String realisasiP1,
            @RequestPart(required = false) String targetP2,
            @RequestPart(required = false) String realisasiP2,
            @RequestPart(required = false) String targetBast,
            @RequestPart(required = false) String realisasiBast,
            @RequestPart(required = false) String keterangan) {

        try {
            MonevPusat newMonevPusat = new MonevPusat();
            
            // Set relations if IDs are provided
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                newMonevPusat.setProgram(program);
            }
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                newMonevPusat.setBpdasId(bpdas);
            }
            
            // Set numeric values
            if (luasTotalTarget != null && !luasTotalTarget.isEmpty()) {
                newMonevPusat.setLuasTotalTarget(Double.parseDouble(luasTotalTarget));
            }
            
            if (luasTotalRealisasi != null && !luasTotalRealisasi.isEmpty()) {
                newMonevPusat.setLuasTotalRealisasi(Double.parseDouble(luasTotalRealisasi));
            }
            
            if (targetT1 != null && !targetT1.isEmpty()) {
                newMonevPusat.setTargetT1(Double.parseDouble(targetT1));
            }
            
            if (realisasiT1 != null && !realisasiT1.isEmpty()) {
                newMonevPusat.setRealisasiT1(Double.parseDouble(realisasiT1));
            }
            
            if (targetP0 != null && !targetP0.isEmpty()) {
                newMonevPusat.setTargetP0(Double.parseDouble(targetP0));
            }
            
            if (realisasiP0 != null && !realisasiP0.isEmpty()) {
                newMonevPusat.setRealisasiP0(Double.parseDouble(realisasiP0));
            }
            
            if (targetP1 != null && !targetP1.isEmpty()) {
                newMonevPusat.setTargetP1(Double.parseDouble(targetP1));
            }
            
            if (realisasiP1 != null && !realisasiP1.isEmpty()) {
                newMonevPusat.setRealisasiP1(Double.parseDouble(realisasiP1));
            }
            
            if (targetP2 != null && !targetP2.isEmpty()) {
                newMonevPusat.setTargetP2(Double.parseDouble(targetP2));
            }
            
            if (realisasiP2 != null && !realisasiP2.isEmpty()) {
                newMonevPusat.setRealisasiP2(Double.parseDouble(realisasiP2));
            }
            
            if (targetBast != null && !targetBast.isEmpty()) {
                newMonevPusat.setTargetBast(Double.parseDouble(targetBast));
            }
            
            if (realisasiBast != null && !realisasiBast.isEmpty()) {
                newMonevPusat.setRealisasiBast(Double.parseDouble(realisasiBast));
            }
            
            newMonevPusat.setKeterangan(keterangan);

            MonevPusat savedMonevPusat = service.save(newMonevPusat);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMonevPusat);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Add detailed logging for troubleshooting
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing MonevPusat record")
    public ResponseEntity<MonevPusat> updateMonevPusat(
            @PathVariable Long id,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String luasTotalTarget,
            @RequestPart(required = false) String luasTotalRealisasi,
            @RequestPart(required = false) String targetT1,
            @RequestPart(required = false) String realisasiT1,
            @RequestPart(required = false) String targetP0,
            @RequestPart(required = false) String realisasiP0,
            @RequestPart(required = false) String targetP1,
            @RequestPart(required = false) String realisasiP1,
            @RequestPart(required = false) String targetP2,
            @RequestPart(required = false) String realisasiP2,
            @RequestPart(required = false) String targetBast,
            @RequestPart(required = false) String realisasiBast,
            @RequestPart(required = false) String keterangan) {

        try {
            MonevPusat existingMonevPusat = service.findById(id);
            
            // Set relations if IDs are provided
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                existingMonevPusat.setProgram(program);
            }
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                existingMonevPusat.setBpdasId(bpdas);
            }
            
            // Set numeric values
            if (luasTotalTarget != null && !luasTotalTarget.isEmpty()) {
                existingMonevPusat.setLuasTotalTarget(Double.parseDouble(luasTotalTarget));
            }
            
            if (luasTotalRealisasi != null && !luasTotalRealisasi.isEmpty()) {
                existingMonevPusat.setLuasTotalRealisasi(Double.parseDouble(luasTotalRealisasi));
            }
            
            if (targetT1 != null && !targetT1.isEmpty()) {
                existingMonevPusat.setTargetT1(Double.parseDouble(targetT1));
            }
            
            if (realisasiT1 != null && !realisasiT1.isEmpty()) {
                existingMonevPusat.setRealisasiT1(Double.parseDouble(realisasiT1));
            }
            
            if (targetP0 != null && !targetP0.isEmpty()) {
                existingMonevPusat.setTargetP0(Double.parseDouble(targetP0));
            }
            
            if (realisasiP0 != null && !realisasiP0.isEmpty()) {
                existingMonevPusat.setRealisasiP0(Double.parseDouble(realisasiP0));
            }
            
            if (targetP1 != null && !targetP1.isEmpty()) {
                existingMonevPusat.setTargetP1(Double.parseDouble(targetP1));
            }
            
            if (realisasiP1 != null && !realisasiP1.isEmpty()) {
                existingMonevPusat.setRealisasiP1(Double.parseDouble(realisasiP1));
            }
            
            if (targetP2 != null && !targetP2.isEmpty()) {
                existingMonevPusat.setTargetP2(Double.parseDouble(targetP2));
            }
            
            if (realisasiP2 != null && !realisasiP2.isEmpty()) {
                existingMonevPusat.setRealisasiP2(Double.parseDouble(realisasiP2));
            }
            
            if (targetBast != null && !targetBast.isEmpty()) {
                existingMonevPusat.setTargetBast(Double.parseDouble(targetBast));
            }
            
            if (realisasiBast != null && !realisasiBast.isEmpty()) {
                existingMonevPusat.setRealisasiBast(Double.parseDouble(realisasiBast));
            }
            
            if (keterangan != null) {
                existingMonevPusat.setKeterangan(keterangan);
            }

            MonevPusat updatedMonevPusat = service.update(id, existingMonevPusat);
            return ResponseEntity.ok(updatedMonevPusat);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a MonevPusat record")
    public ResponseEntity<Void> deleteMonevPusat(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    



}