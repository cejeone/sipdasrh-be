package com.kehutanan.tktrh.ppkh.spas.controller;

import java.time.LocalDateTime;
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
import com.kehutanan.tktrh.master.model.Das;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.BpdasService;
import com.kehutanan.tktrh.master.service.DasService;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.ppkh.spas.dto.SpasPageDTO;
import com.kehutanan.tktrh.ppkh.spas.model.Spas;
import com.kehutanan.tktrh.ppkh.spas.model.dto.SpasDTO;
import com.kehutanan.tktrh.ppkh.spas.service.SpasService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/spas")
public class SpasController {

    private final SpasService service;
    private final BpdasService bpdasService;
    private final DasService dasService;
    private final LovService lovService;
    private final PagedResourcesAssembler<Spas> pagedResourcesAssembler;

    @Autowired
    public SpasController(
            SpasService service,
            BpdasService bpdasService,
            DasService dasService,
            LovService lovService,
            PagedResourcesAssembler<Spas> pagedResourcesAssembler) {
        this.service = service;
        this.bpdasService = bpdasService;
        this.dasService = dasService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all SPAS with optional filtering")
    public ResponseEntity<SpasPageDTO> getAllSpas(
            @RequestParam(required = false) String spasId,
            @RequestParam(required = false) String namaDas,
            @RequestParam(required = false) List<String> bpdasList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        SpasPageDTO spasPage;
        
        String baseUrl = request.getRequestURL().toString();
        
        // Check if any filter is provided
        if ((spasId != null && !spasId.isEmpty()) || 
            (namaDas != null && !namaDas.isEmpty()) || 
            (bpdasList != null && !bpdasList.isEmpty())) {
            
            spasPage = service.findByFiltersWithCache(spasId, namaDas, bpdasList, pageable, baseUrl);
        } else {
            spasPage = service.findAllWithCache(pageable, baseUrl);
        }
        
        return ResponseEntity.ok(spasPage);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search SPAS by keyword")
    public ResponseEntity<SpasPageDTO> searchSpas(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        SpasPageDTO spasPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        
        return ResponseEntity.ok(spasPage);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get SPAS by ID")
    public ResponseEntity<SpasDTO> getSpasById(@PathVariable Long id) {
        try {
            SpasDTO spasDTO = service.findDTOById(id);
            return ResponseEntity.ok(spasDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new SPAS")
    public ResponseEntity<Spas> createSpas(
            @RequestPart(required = true) String bpdasId,
            @RequestPart(required = true) String dasId,
            @RequestPart(required = true) String spasLovId,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String nilaiTma,
            @RequestPart(required = false) String nilaiCurahHujan,
            @RequestPart(required = false) String teganganBaterai) {
        
        try {
            Spas newSpas = new Spas();
            
            // Set BPDAS
            Long bpdasIdLong = Long.parseLong(bpdasId);
            Bpdas bpdas = bpdasService.findById(bpdasIdLong);
            newSpas.setBpdas(bpdas);
            
            // Set DAS
            Long dasIdLong = Long.parseLong(dasId);
            Das das = dasService.findById(dasIdLong);
            newSpas.setDas(das);
            
            // Set SPAS ID (Lov)
            Long spasLovIdLong = Long.parseLong(spasLovId);
            Lov spasLov = lovService.findById(spasLovIdLong);
            newSpas.setSpasId(spasLov);
            
            // Set date
            if (tanggal != null && !tanggal.isEmpty()) {
                // Assuming date is in ISO format (yyyy-MM-ddTHH:mm:ss)
                newSpas.setTanggal(LocalDateTime.parse(tanggal));
            } else {
                // Set current date if not provided
                newSpas.setTanggal(LocalDateTime.now());
            }
            
            // Set TMA value
            if (nilaiTma != null && !nilaiTma.isEmpty()) {
                newSpas.setNilaiTma(Double.parseDouble(nilaiTma));
            }
            
            // Set rainfall value
            if (nilaiCurahHujan != null && !nilaiCurahHujan.isEmpty()) {
                newSpas.setNilaiCurahHujan(Double.parseDouble(nilaiCurahHujan));
            }
            
            // Set battery voltage
            if (teganganBaterai != null && !teganganBaterai.isEmpty()) {
                newSpas.setTeganganBaterai(Double.parseDouble(teganganBaterai));
            }
            
            Spas savedSpas = service.save(newSpas);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSpas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing SPAS")
    public ResponseEntity<Spas> updateSpas(
            @PathVariable Long id,
            @RequestPart(required = true) String bpdasId,
            @RequestPart(required = true) String dasId,
            @RequestPart(required = true) String spasLovId,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String nilaiTma,
            @RequestPart(required = false) String nilaiCurahHujan,
            @RequestPart(required = false) String teganganBaterai) {
        
        try {
            Spas existingSpas = service.findById(id);
            
            // Update BPDAS
            Long bpdasIdLong = Long.parseLong(bpdasId);
            Bpdas bpdas = bpdasService.findById(bpdasIdLong);
            existingSpas.setBpdas(bpdas);
            
            // Update DAS
            Long dasIdLong = Long.parseLong(dasId);
            Das das = dasService.findById(dasIdLong);
            existingSpas.setDas(das);
            
            // Update SPAS ID (Lov)
            Long spasLovIdLong = Long.parseLong(spasLovId);
            Lov spasLov = lovService.findById(spasLovIdLong);
            existingSpas.setSpasId(spasLov);
            
            // Update date
            if (tanggal != null && !tanggal.isEmpty()) {
                existingSpas.setTanggal(LocalDateTime.parse(tanggal));
            }
            
            // Update TMA value
            if (nilaiTma != null && !nilaiTma.isEmpty()) {
                existingSpas.setNilaiTma(Double.parseDouble(nilaiTma));
            }
            
            // Update rainfall value
            if (nilaiCurahHujan != null && !nilaiCurahHujan.isEmpty()) {
                existingSpas.setNilaiCurahHujan(Double.parseDouble(nilaiCurahHujan));
            }
            
            // Update battery voltage
            if (teganganBaterai != null && !teganganBaterai.isEmpty()) {
                existingSpas.setTeganganBaterai(Double.parseDouble(teganganBaterai));
            }
            
            Spas updatedSpas = service.update(id, existingSpas);
            return ResponseEntity.ok(updatedSpas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a SPAS")
    public ResponseEntity<Void> deleteSpas(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}