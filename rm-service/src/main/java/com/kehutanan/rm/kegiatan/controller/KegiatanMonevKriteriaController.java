package com.kehutanan.rm.kegiatan.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kehutanan.rm.kegiatan.dto.KegiatanMonevKriteriaDTO;
import com.kehutanan.rm.kegiatan.dto.KegiatanMonevKriteriaPageDTO;
import com.kehutanan.rm.kegiatan.model.KegiatanMonev;
import com.kehutanan.rm.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.rm.kegiatan.service.KegiatanMonevKriteriaService;
import com.kehutanan.rm.kegiatan.service.KegiatanMonevService;
import com.kehutanan.rm.master.model.Lov;
import com.kehutanan.rm.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-monev-kriteria")
public class KegiatanMonevKriteriaController {

    private final KegiatanMonevKriteriaService service;
    private final KegiatanMonevService kegiatanMonevService;
    private final LovService lovService;
    
    @Autowired
    public KegiatanMonevKriteriaController(
            KegiatanMonevKriteriaService service,
            KegiatanMonevService kegiatanMonevService,
            LovService lovService) {
        this.service = service;
        this.kegiatanMonevService = kegiatanMonevService;
        this.lovService = lovService;
    }
    
    @GetMapping
    @Operation(summary = "Get all Kegiatan Monev Kriteria with pagination")
    public ResponseEntity<KegiatanMonevKriteriaPageDTO> getAllKegiatanMonevKriteria(
            @RequestParam(required = false) Long kegiatanMonevId,
            @RequestParam(required = false) String namaAktivitas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevKriteriaPageDTO kriteriasPage;
        
        String baseUrl = request.getRequestURL().toString();
        
        // Check if any filter is provided
        if ((kegiatanMonevId != null) || 
            (namaAktivitas != null && !namaAktivitas.isEmpty())) {
            kriteriasPage = service.findByFiltersWithCache(kegiatanMonevId, namaAktivitas, pageable, baseUrl);
        } else {
            kriteriasPage = service.findAllWithCache(pageable, baseUrl);
        }
        
        return ResponseEntity.ok(kriteriasPage);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan Monev Kriteria")
    public ResponseEntity<KegiatanMonevKriteriaPageDTO> searchKegiatanMonevKriteria(
            @RequestParam(required = false) Long kegiatanMonevId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevKriteriaPageDTO kriteriasPage = service.searchWithCache(
                kegiatanMonevId, keyWord, pageable, request.getRequestURL().toString());
                
        return ResponseEntity.ok(kriteriasPage);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get Kegiatan Monev Kriteria by ID")
    public ResponseEntity<KegiatanMonevKriteriaDTO> getKegiatanMonevKriteriaById(@PathVariable Long id) {
        try {
            KegiatanMonevKriteriaDTO kriteriaDTO = service.findDTOById(id);
            return ResponseEntity.ok(kriteriaDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/by-kegiatan-monev/{kegiatanMonevId}")
    @Operation(summary = "Get all Kegiatan Monev Kriteria by Kegiatan Monev ID")
    public ResponseEntity<List<KegiatanMonevKriteria>> getByKegiatanMonevId(@PathVariable Long kegiatanMonevId) {
        List<KegiatanMonevKriteria> kriterias = service.findByKegiatanMonevId(kegiatanMonevId);
        return ResponseEntity.ok(kriterias);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new Kegiatan Monev Kriteria")
    public ResponseEntity<KegiatanMonevKriteria> createKegiatanMonevKriteria(
            @RequestPart String kegiatanMonevId,
            @RequestPart String aktivitasId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String catatan) {
        
        try {
            KegiatanMonevKriteria newKriteria = new KegiatanMonevKriteria();
            
            // Set kegiatan monev relationship
            Long kegiatanMonevIdLong = Long.parseLong(kegiatanMonevId);
            KegiatanMonev kegiatanMonev = kegiatanMonevService.findById(kegiatanMonevIdLong);
            newKriteria.setKegiatanMonev(kegiatanMonev);
            
            // Set aktivitas relationship
            Long aktivitasIdLong = Long.parseLong(aktivitasId);
            Lov aktivitas = lovService.findById(aktivitasIdLong);
            newKriteria.setAktivitasId(aktivitas);
            
            // Set optional fields
            if (targetLuas != null && !targetLuas.isEmpty()) {
                newKriteria.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                newKriteria.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            newKriteria.setCatatan(catatan);
            
            // Save the entity
            KegiatanMonevKriteria savedKriteria = service.save(newKriteria);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKriteria);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update Kegiatan Monev Kriteria by ID")
    public ResponseEntity<KegiatanMonevKriteria> updateKegiatanMonevKriteria(
            @PathVariable Long id,
            @RequestPart String kegiatanMonevId,
            @RequestPart String aktivitasId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String catatan) {
        
        try {
            // Get existing entity
            KegiatanMonevKriteria existingKriteria = service.findById(id);
            
            // Update kegiatan monev relationship
            Long kegiatanMonevIdLong = Long.parseLong(kegiatanMonevId);
            KegiatanMonev kegiatanMonev = kegiatanMonevService.findById(kegiatanMonevIdLong);
            existingKriteria.setKegiatanMonev(kegiatanMonev);
            
            // Update aktivitas relationship
            Long aktivitasIdLong = Long.parseLong(aktivitasId);
            Lov aktivitas = lovService.findById(aktivitasIdLong);
            existingKriteria.setAktivitasId(aktivitas);
            
            // Update optional fields
            if (targetLuas != null && !targetLuas.isEmpty()) {
                existingKriteria.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                existingKriteria.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            existingKriteria.setCatatan(catatan);
            
            // Update the entity
            KegiatanMonevKriteria updatedKriteria = service.update(id, existingKriteria);
            return ResponseEntity.ok(updatedKriteria);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Kegiatan Monev Kriteria by ID")
    public ResponseEntity<Void> deleteKegiatanMonevKriteria(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}