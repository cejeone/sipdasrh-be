package com.kehutanan.tktrh.tmkh.kegiatan.controller;

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
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanMonevKriteriaPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanMonevKriteriaDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanMonevKriteriaService;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanMonevService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController("tmkhKegiatanMonevKriteriaController")
@RequestMapping("/api/tmkh/kegiatan-monev-kriteria")
public class KegiatanMonevKriteriaController {

    private final KegiatanMonevKriteriaService service;
    private final KegiatanMonevService kegiatanMonevService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanMonevKriteria> pagedResourcesAssembler;

    @Autowired
    public KegiatanMonevKriteriaController(
            KegiatanMonevKriteriaService service,
            KegiatanMonevService kegiatanMonevService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanMonevKriteria> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanMonevService = kegiatanMonevService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all Kegiatan Monev Kriteria with pagination")
    public ResponseEntity<KegiatanMonevKriteriaPageDTO> getAllKegiatanMonevKriteria(
            @RequestParam(required = false) Long monevId,
            @RequestParam(required = false) String catatan,
            @RequestParam(required = false) List<String> aktivitasList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevKriteriaPageDTO pageDTO;
        
        String baseUrl = request.getRequestURL().toString();
        
        // Check if any filter is provided
        if (monevId != null || (catatan != null && !catatan.isEmpty()) || 
                (aktivitasList != null && !aktivitasList.isEmpty())) {
            pageDTO = service.findByFiltersWithCache(monevId, catatan, aktivitasList, pageable, baseUrl);
        } else {
            pageDTO = service.findAllWithCache(pageable, baseUrl);
        }
        
        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan Monev Kriteria by keyword")
    public ResponseEntity<KegiatanMonevKriteriaPageDTO> searchKegiatanMonevKriteria(
            @RequestParam(required = false) Long monevId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevKriteriaPageDTO pageDTO = service.searchWithCache(
                monevId, keyWord, pageable, request.getRequestURL().toString());
        
        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Kegiatan Monev Kriteria by ID")
    public ResponseEntity<KegiatanMonevKriteriaDTO> getKegiatanMonevKriteriaById(@PathVariable Long id) {
        try {
            KegiatanMonevKriteriaDTO dto = service.findDTOById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new Kegiatan Monev Kriteria")
    public ResponseEntity<KegiatanMonevKriteria> createKegiatanMonevKriteria(
            @RequestPart(required = true) String kegiatanMonevId,
            @RequestPart(required = true) String aktivitasId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String catatan) {
        
        try {
            KegiatanMonevKriteria newKriteria = new KegiatanMonevKriteria();
            
            // Set KegiatanMonev relationship
            if (kegiatanMonevId != null && !kegiatanMonevId.isEmpty()) {
                Long monevIdLong = Long.parseLong(kegiatanMonevId);
                KegiatanMonev kegiatanMonev = kegiatanMonevService.findById(monevIdLong);
                newKriteria.setKegiatanMonev(kegiatanMonev);
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            // Set Aktivitas (Lov) relationship
            if (aktivitasId != null && !aktivitasId.isEmpty()) {
                Long aktivitasIdLong = Long.parseLong(aktivitasId);
                Lov aktivitas = lovService.findById(aktivitasIdLong);
                newKriteria.setAktivitasId(aktivitas);
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            // Set numeric fields
            if (targetLuas != null && !targetLuas.isEmpty()) {
                newKriteria.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                newKriteria.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            // Set catatan
            newKriteria.setCatatan(catatan);
            
            KegiatanMonevKriteria savedKriteria = service.save(newKriteria);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKriteria);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing Kegiatan Monev Kriteria")
    public ResponseEntity<KegiatanMonevKriteria> updateKegiatanMonevKriteria(
            @PathVariable Long id,
            @RequestPart(required = true) String kegiatanMonevId,
            @RequestPart(required = true) String aktivitasId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String catatan) {
        
        try {
            KegiatanMonevKriteria existingKriteria = service.findById(id);
            
            // Set KegiatanMonev relationship
            if (kegiatanMonevId != null && !kegiatanMonevId.isEmpty()) {
                Long monevIdLong = Long.parseLong(kegiatanMonevId);
                KegiatanMonev kegiatanMonev = kegiatanMonevService.findById(monevIdLong);
                existingKriteria.setKegiatanMonev(kegiatanMonev);
            }
            
            // Set Aktivitas (Lov) relationship
            if (aktivitasId != null && !aktivitasId.isEmpty()) {
                Long aktivitasIdLong = Long.parseLong(aktivitasId);
                Lov aktivitas = lovService.findById(aktivitasIdLong);
                existingKriteria.setAktivitasId(aktivitas);
            }
            
            // Set numeric fields
            if (targetLuas != null && !targetLuas.isEmpty()) {
                existingKriteria.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                existingKriteria.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            // Set catatan
            existingKriteria.setCatatan(catatan);
            
            KegiatanMonevKriteria updatedKriteria = service.update(id, existingKriteria);
            return ResponseEntity.ok(updatedKriteria);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Kegiatan Monev Kriteria by ID")
    public ResponseEntity<Void> deleteKegiatanMonevKriteria(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}