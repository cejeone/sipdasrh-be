package com.kehutanan.tktrh.tmkh.kegiatan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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
import org.springframework.web.bind.annotation.RestController;

import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanBastRehabDasPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastRehabDas;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanBastRehabDasDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanBastRehabDasService;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-bast-rehab-das")
public class KegiatanBastRehabDasController {

    private final KegiatanBastRehabDasService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanBastRehabDas> pagedResourcesAssembler;

    @Autowired
    public KegiatanBastRehabDasController(
            KegiatanBastRehabDasService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanBastRehabDas> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all KegiatanBastRehabDas entries with pagination")
    public ResponseEntity<KegiatanBastRehabDasPageDTO> getAllKegiatanBastRehabDas(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> statusList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanBastRehabDasPageDTO kegiatanBastRehabDasPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) ||
                (keterangan != null && !keterangan.isEmpty()) ||
                (statusList != null && !statusList.isEmpty())) {
            kegiatanBastRehabDasPage = service.findByFiltersWithCache(kegiatanId, keterangan, statusList, pageable, baseUrl);
        } else {
            kegiatanBastRehabDasPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanBastRehabDasPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanBastRehabDas by keyword")
    public ResponseEntity<KegiatanBastRehabDasPageDTO> searchKegiatanBastRehabDas(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanBastRehabDasPageDTO kegiatanBastRehabDasPage = service.searchWithCache(kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanBastRehabDasPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get KegiatanBastRehabDas by ID")
    public ResponseEntity<KegiatanBastRehabDasDTO> getKegiatanBastRehabDasById(@PathVariable Long id) {
        try {
            KegiatanBastRehabDasDTO kegiatanBastRehabDasDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanBastRehabDasDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new KegiatanBastRehabDas")
    public ResponseEntity<KegiatanBastRehabDas> createKegiatanBastRehabDas(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusSerahTerimaId) {
        
        try {
            KegiatanBastRehabDas newKegiatanBastRehabDas = new KegiatanBastRehabDas();
            
            // Set kegiatan if provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatanBastRehabDas.setKegiatan(kegiatan);
            } else {
                return ResponseEntity.badRequest().build(); // Kegiatan ID is required
            }
            
            // Set tahunId if provided
            if (tahunId != null && !tahunId.isEmpty()) {
                newKegiatanBastRehabDas.setTahunId(Integer.parseInt(tahunId));
            }
            
            // Set targetLuas if provided
            if (targetLuas != null && !targetLuas.isEmpty()) {
                newKegiatanBastRehabDas.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            // Set keterangan if provided
            newKegiatanBastRehabDas.setKeterangan(keterangan);
            
            // Set statusSerahTerima if provided
            if (statusSerahTerimaId != null && !statusSerahTerimaId.isEmpty()) {
                Long lovId = Long.parseLong(statusSerahTerimaId);
                Lov statusSerahTerima = lovService.findById(lovId);
                newKegiatanBastRehabDas.setStatusSerahTerima(statusSerahTerima);
            }
            
            KegiatanBastRehabDas savedKegiatanBastRehabDas = service.save(newKegiatanBastRehabDas);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatanBastRehabDas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing KegiatanBastRehabDas")
    public ResponseEntity<KegiatanBastRehabDas> updateKegiatanBastRehabDas(
            @PathVariable Long id,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusSerahTerimaId) {
        
        try {
            KegiatanBastRehabDas existingKegiatanBastRehabDas = service.findById(id);
            
            // Update kegiatan if provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatanBastRehabDas.setKegiatan(kegiatan);
            }
            
            // Update tahunId if provided
            if (tahunId != null && !tahunId.isEmpty()) {
                existingKegiatanBastRehabDas.setTahunId(Integer.parseInt(tahunId));
            }
            
            // Update targetLuas if provided
            if (targetLuas != null && !targetLuas.isEmpty()) {
                existingKegiatanBastRehabDas.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            // Update keterangan if provided
            if (keterangan != null) {
                existingKegiatanBastRehabDas.setKeterangan(keterangan);
            }
            
            // Update statusSerahTerima if provided
            if (statusSerahTerimaId != null && !statusSerahTerimaId.isEmpty()) {
                Long lovId = Long.parseLong(statusSerahTerimaId);
                Lov statusSerahTerima = lovService.findById(lovId);
                existingKegiatanBastRehabDas.setStatusSerahTerima(statusSerahTerima);
            }
            
            KegiatanBastRehabDas updatedKegiatanBastRehabDas = service.update(id, existingKegiatanBastRehabDas);
            return ResponseEntity.ok(updatedKegiatanBastRehabDas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a KegiatanBastRehabDas by ID")
    public ResponseEntity<Void> deleteKegiatanBastRehabDas(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}