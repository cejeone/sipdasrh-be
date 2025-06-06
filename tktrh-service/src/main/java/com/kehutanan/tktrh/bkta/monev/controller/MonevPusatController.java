package com.kehutanan.tktrh.bkta.monev.controller;

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

import com.kehutanan.tktrh.bkta.monev.dto.MonevPusatPageDTO;
import com.kehutanan.tktrh.bkta.monev.model.MonevPusat;
import com.kehutanan.tktrh.bkta.monev.model.dto.MonevPusatDTO;
import com.kehutanan.tktrh.bkta.monev.service.MonevPusatService;
import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.bkta.program.service.ProgramService;
import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.service.BpdasService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/monev-pusat")
public class MonevPusatController {

    private final MonevPusatService monevPusatService;
    private final ProgramService programService;
    private final BpdasService bpdasService;
    private final PagedResourcesAssembler<MonevPusat> pagedResourcesAssembler;

    @Autowired
    public MonevPusatController(
            MonevPusatService monevPusatService,
            ProgramService programService,
            BpdasService bpdasService,
            PagedResourcesAssembler<MonevPusat> pagedResourcesAssembler) {
        this.monevPusatService = monevPusatService;
        this.programService = programService;
        this.bpdasService = bpdasService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<MonevPusatPageDTO> getAllMonevPusat(
            @RequestParam(required = false) String namaProgram,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> bpdasList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        MonevPusatPageDTO monevPusatPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((namaProgram != null && !namaProgram.isEmpty()) ||
                (keterangan != null && !keterangan.isEmpty()) ||
                (bpdasList != null && !bpdasList.isEmpty())) {
            monevPusatPage = monevPusatService.findByFiltersWithCache(namaProgram, keterangan, bpdasList, pageable, baseUrl);
        } else {
            monevPusatPage = monevPusatService.findAllWithCache(pageable, baseUrl);
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
        MonevPusatPageDTO monevPusatPage = monevPusatService.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(monevPusatPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonevPusatDTO> getMonevPusatById(@PathVariable Long id) {
        try {
            MonevPusatDTO monevPusatDTO = monevPusatService.findDTOById(id);
            return ResponseEntity.ok(monevPusatDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MonevPusat> createMonevPusat(
            @RequestPart(required = true) String programId,
            @RequestPart(required = true) String bpdasId,
            @RequestPart(required = false) String targetTotalDpn,
            @RequestPart(required = false) String targetTotalGp,
            @RequestPart(required = false) String targetTambahanDpn,
            @RequestPart(required = false) String targetTambahanGp,
            @RequestPart(required = false) String realisasiRantekDpn,
            @RequestPart(required = false) String realisasiRantekGp,
            @RequestPart(required = false) String realisasiSpksDpn,
            @RequestPart(required = false) String realisasiSpksGp,
            @RequestPart(required = false) String realisasiFisikDpn,
            @RequestPart(required = false) String realisasiFisikGp,
            @RequestPart(required = false) String keterangan) {

        try {
            MonevPusat newMonevPusat = new MonevPusat();
            
            // Set required relationships
            Program program = programService.findById(Long.parseLong(programId));
            Bpdas bpdas = bpdasService.findById(Long.parseLong(bpdasId));
            
            newMonevPusat.setProgram(program);
            newMonevPusat.setBpdas(bpdas);
            
            // Set numeric fields if provided
            if (targetTotalDpn != null && !targetTotalDpn.isEmpty()) {
                newMonevPusat.setTargetTotalDpn(Integer.parseInt(targetTotalDpn));
            }
            
            if (targetTotalGp != null && !targetTotalGp.isEmpty()) {
                newMonevPusat.setTargetTotalGp(Integer.parseInt(targetTotalGp));
            }
            
            if (targetTambahanDpn != null && !targetTambahanDpn.isEmpty()) {
                newMonevPusat.setTargetTambahanDpn(Integer.parseInt(targetTambahanDpn));
            }
            
            if (targetTambahanGp != null && !targetTambahanGp.isEmpty()) {
                newMonevPusat.setTargetTambahanGp(Integer.parseInt(targetTambahanGp));
            }
            
            if (realisasiRantekDpn != null && !realisasiRantekDpn.isEmpty()) {
                newMonevPusat.setRealisasiRantekDpn(Integer.parseInt(realisasiRantekDpn));
            }
            
            if (realisasiRantekGp != null && !realisasiRantekGp.isEmpty()) {
                newMonevPusat.setRealisasiRantekGp(Integer.parseInt(realisasiRantekGp));
            }
            
            if (realisasiSpksDpn != null && !realisasiSpksDpn.isEmpty()) {
                newMonevPusat.setRealisasiSpksDpn(Integer.parseInt(realisasiSpksDpn));
            }
            
            if (realisasiSpksGp != null && !realisasiSpksGp.isEmpty()) {
                newMonevPusat.setRealisasiSpksGp(Integer.parseInt(realisasiSpksGp));
            }
            
            if (realisasiFisikDpn != null && !realisasiFisikDpn.isEmpty()) {
                newMonevPusat.setRealisasiFisikDpn(Integer.parseInt(realisasiFisikDpn));
            }
            
            if (realisasiFisikGp != null && !realisasiFisikGp.isEmpty()) {
                newMonevPusat.setRealisasiFisikGp(Integer.parseInt(realisasiFisikGp));
            }
            
            // Set text field
            newMonevPusat.setKeterangan(keterangan);
            
            MonevPusat savedMonevPusat = monevPusatService.save(newMonevPusat);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMonevPusat);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MonevPusat> updateMonevPusat(
            @PathVariable Long id,
            @RequestPart(required = true) String programId,
            @RequestPart(required = true) String bpdasId,
            @RequestPart(required = false) String targetTotalDpn,
            @RequestPart(required = false) String targetTotalGp,
            @RequestPart(required = false) String targetTambahanDpn,
            @RequestPart(required = false) String targetTambahanGp,
            @RequestPart(required = false) String realisasiRantekDpn,
            @RequestPart(required = false) String realisasiRantekGp,
            @RequestPart(required = false) String realisasiSpksDpn,
            @RequestPart(required = false) String realisasiSpksGp,
            @RequestPart(required = false) String realisasiFisikDpn,
            @RequestPart(required = false) String realisasiFisikGp,
            @RequestPart(required = false) String keterangan) {

        try {
            MonevPusat existingMonevPusat = monevPusatService.findById(id);
            
            // Update relationships
            Program program = programService.findById(Long.parseLong(programId));
            Bpdas bpdas = bpdasService.findById(Long.parseLong(bpdasId));
            
            existingMonevPusat.setProgram(program);
            existingMonevPusat.setBpdas(bpdas);
            
            // Update numeric fields if provided
            if (targetTotalDpn != null && !targetTotalDpn.isEmpty()) {
                existingMonevPusat.setTargetTotalDpn(Integer.parseInt(targetTotalDpn));
            }
            
            if (targetTotalGp != null && !targetTotalGp.isEmpty()) {
                existingMonevPusat.setTargetTotalGp(Integer.parseInt(targetTotalGp));
            }
            
            if (targetTambahanDpn != null && !targetTambahanDpn.isEmpty()) {
                existingMonevPusat.setTargetTambahanDpn(Integer.parseInt(targetTambahanDpn));
            }
            
            if (targetTambahanGp != null && !targetTambahanGp.isEmpty()) {
                existingMonevPusat.setTargetTambahanGp(Integer.parseInt(targetTambahanGp));
            }
            
            if (realisasiRantekDpn != null && !realisasiRantekDpn.isEmpty()) {
                existingMonevPusat.setRealisasiRantekDpn(Integer.parseInt(realisasiRantekDpn));
            }
            
            if (realisasiRantekGp != null && !realisasiRantekGp.isEmpty()) {
                existingMonevPusat.setRealisasiRantekGp(Integer.parseInt(realisasiRantekGp));
            }
            
            if (realisasiSpksDpn != null && !realisasiSpksDpn.isEmpty()) {
                existingMonevPusat.setRealisasiSpksDpn(Integer.parseInt(realisasiSpksDpn));
            }
            
            if (realisasiSpksGp != null && !realisasiSpksGp.isEmpty()) {
                existingMonevPusat.setRealisasiSpksGp(Integer.parseInt(realisasiSpksGp));
            }
            
            if (realisasiFisikDpn != null && !realisasiFisikDpn.isEmpty()) {
                existingMonevPusat.setRealisasiFisikDpn(Integer.parseInt(realisasiFisikDpn));
            }
            
            if (realisasiFisikGp != null && !realisasiFisikGp.isEmpty()) {
                existingMonevPusat.setRealisasiFisikGp(Integer.parseInt(realisasiFisikGp));
            }
            
            // Update text field
            existingMonevPusat.setKeterangan(keterangan);
            
            MonevPusat updatedMonevPusat = monevPusatService.update(id, existingMonevPusat);
            return ResponseEntity.ok(updatedMonevPusat);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonevPusat(@PathVariable Long id) {
        try {
            monevPusatService.findById(id); // Check if exists
            monevPusatService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}