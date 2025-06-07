package com.kehutanan.tktrh.bkta.kegiatan.controller;

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

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanMonevKriteriaPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanMonevKriteriaDTO;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanMonevKriteriaService;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanMonevService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController("bktaKegiatanMonevKriteriaController")
@RequestMapping("/api/bkta/kegiatan-monev-kriteria")
public class KegiatanMonevKriteriaController {

    private final KegiatanMonevKriteriaService service;
    private final KegiatanMonevService kegiatanMonevService;
    private final PagedResourcesAssembler<KegiatanMonevKriteria> pagedResourcesAssembler;

    @Autowired
    public KegiatanMonevKriteriaController(
            KegiatanMonevKriteriaService service,
            KegiatanMonevService kegiatanMonevService,
            PagedResourcesAssembler<KegiatanMonevKriteria> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanMonevService = kegiatanMonevService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all Kegiatan Monev Kriteria with pagination and optional filtering")
    public ResponseEntity<KegiatanMonevKriteriaPageDTO> getAllKriteria(
            @RequestParam(required = false) Long kegiatanMonevId,
            @RequestParam(required = false) String aktivitas,
            @RequestParam(required = false) String target,
            @RequestParam(required = false) List<String> realisasi,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevKriteriaPageDTO kriteriaPage;
        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if (kegiatanMonevId != null || 
            (aktivitas != null && !aktivitas.isEmpty()) || 
            (target != null && !target.isEmpty()) || 
            (realisasi != null && !realisasi.isEmpty())) {
            kriteriaPage = service.findByFiltersWithCache(
                kegiatanMonevId, aktivitas, target, realisasi, pageable, baseUrl);
        } else {
            kriteriaPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kriteriaPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan Monev Kriteria by keyword")
    public ResponseEntity<KegiatanMonevKriteriaPageDTO> searchKriteria(
            @RequestParam(required = false) Long kegiatanMonevId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevKriteriaPageDTO kriteriaPage = service.searchWithCache(
            kegiatanMonevId, keyWord, pageable, request.getRequestURL().toString());
        
        return ResponseEntity.ok(kriteriaPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Kegiatan Monev Kriteria by ID")
    public ResponseEntity<KegiatanMonevKriteriaDTO> getKriteriaById(@PathVariable Long id) {
        try {
            KegiatanMonevKriteriaDTO kriteriaDTO = service.findDTOById(id);
            return ResponseEntity.ok(kriteriaDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new Kegiatan Monev Kriteria")
    public ResponseEntity<KegiatanMonevKriteria> createKriteria(
            @RequestPart(required = true) String kegiatanMonevId,
            @RequestPart(required = false) String aktivitas,
            @RequestPart(required = false) String target,
            @RequestPart(required = false) String realisasi,
            @RequestPart(required = false) String catatan) {
        
        try {
            KegiatanMonevKriteria newKriteria = new KegiatanMonevKriteria();
            
            // Set the related KegiatanMonev
            if (kegiatanMonevId != null && !kegiatanMonevId.isEmpty()) {
                Long monitoringEvaluasiId = Long.parseLong(kegiatanMonevId);
                KegiatanMonev kegiatanMonev = kegiatanMonevService.findById(monitoringEvaluasiId);
                newKriteria.setMonitoringEvaluasi(kegiatanMonev);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
            
            // Set other fields
            newKriteria.setAktivitas(aktivitas);
            newKriteria.setTarget(target);
            newKriteria.setRealisasi(realisasi);
            newKriteria.setCatatan(catatan);
            
            KegiatanMonevKriteria savedKriteria = service.save(newKriteria);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKriteria);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing Kegiatan Monev Kriteria")
    public ResponseEntity<KegiatanMonevKriteria> updateKriteria(
            @PathVariable Long id,
            @RequestPart(required = true) String kegiatanMonevId,
            @RequestPart(required = false) String aktivitas,
            @RequestPart(required = false) String target,
            @RequestPart(required = false) String realisasi,
            @RequestPart(required = false) String catatan) {
        
        try {
            KegiatanMonevKriteria existingKriteria = service.findById(id);
            
            // Set the related KegiatanMonev
            if (kegiatanMonevId != null && !kegiatanMonevId.isEmpty()) {
                Long monitoringEvaluasiId = Long.parseLong(kegiatanMonevId);
                KegiatanMonev kegiatanMonev = kegiatanMonevService.findById(monitoringEvaluasiId);
                existingKriteria.setMonitoringEvaluasi(kegiatanMonev);
            }
            
            // Update other fields
            existingKriteria.setAktivitas(aktivitas);
            existingKriteria.setTarget(target);
            existingKriteria.setRealisasi(realisasi);
            existingKriteria.setCatatan(catatan);
            
            KegiatanMonevKriteria updatedKriteria = service.update(id, existingKriteria);
            return ResponseEntity.ok(updatedKriteria);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Kegiatan Monev Kriteria by ID")
    public ResponseEntity<Void> deleteKriteria(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}