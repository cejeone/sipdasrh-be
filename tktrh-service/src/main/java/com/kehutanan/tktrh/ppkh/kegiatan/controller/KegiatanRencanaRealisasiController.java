package com.kehutanan.tktrh.ppkh.kegiatan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanRencanaRealisasiPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRencanaRealisasi;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanRencanaRealisasiDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanRencanaRealisasiService;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ppkh/kegiatan-rencana-realisasi")
public class KegiatanRencanaRealisasiController {

    private final KegiatanRencanaRealisasiService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanRencanaRealisasi> pagedResourcesAssembler;

    @Autowired
    public KegiatanRencanaRealisasiController(
            KegiatanRencanaRealisasiService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanRencanaRealisasi> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all Kegiatan Rencana Realisasi with pagination")
    public ResponseEntity<KegiatanRencanaRealisasiPageDTO> getAllKegiatanRencanaRealisasi(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String fungsiKawasan,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanRencanaRealisasiPageDTO realisasiPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) || 
            (fungsiKawasan != null && !fungsiKawasan.isEmpty()) || 
            (keterangan != null && !keterangan.isEmpty()) ||
            (status != null && !status.isEmpty())) {
            realisasiPage = service.findByFiltersWithCache(kegiatanId, fungsiKawasan, keterangan, status, pageable, baseUrl);
        } else {
            realisasiPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(realisasiPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan Rencana Realisasi by keyword")
    public ResponseEntity<KegiatanRencanaRealisasiPageDTO> searchKegiatanRencanaRealisasi(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanRencanaRealisasiPageDTO realisasiPage = service.searchWithCache(kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(realisasiPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Kegiatan Rencana Realisasi by ID")
    public ResponseEntity<KegiatanRencanaRealisasiDTO> getKegiatanRencanaRealisasiById(@PathVariable Long id) {
        try {
            KegiatanRencanaRealisasiDTO realisasiDTO = service.findDTOById(id);
            return ResponseEntity.ok(realisasiDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new Kegiatan Rencana Realisasi")
    public ResponseEntity<KegiatanRencanaRealisasi> createKegiatanRencanaRealisasi(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = true) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = true) String statusId) {
        
        try {
            KegiatanRencanaRealisasi newRealisasi = new KegiatanRencanaRealisasi();
            
            // Set kegiatan
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newRealisasi.setKegiatan(kegiatan);
            }
            
            // Set fungsi kawasan
            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long fungsiKawasanIdLong = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(fungsiKawasanIdLong);
                newRealisasi.setFungsiKawasanId(fungsiKawasan);
            }
            
            // Set target luas if provided
            if (targetLuas != null && !targetLuas.isEmpty()) {
                newRealisasi.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            // Set realisasi luas if provided
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                newRealisasi.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            // Set tahun id if provided
            if (tahunId != null && !tahunId.isEmpty()) {
                newRealisasi.setTahunId(Integer.parseInt(tahunId));
            }
            
            // Set keterangan
            newRealisasi.setKeterangan(keterangan);
            
            // Set status
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newRealisasi.setStatusId(status);
            }
            
            KegiatanRencanaRealisasi savedRealisasi = service.save(newRealisasi);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRealisasi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing Kegiatan Rencana Realisasi")
    public ResponseEntity<KegiatanRencanaRealisasi> updateKegiatanRencanaRealisasi(
            @PathVariable Long id,
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = true) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = true) String statusId) {
        
        try {
            KegiatanRencanaRealisasi existingRealisasi = service.findById(id);
            
            // Set kegiatan
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingRealisasi.setKegiatan(kegiatan);
            }
            
            // Set fungsi kawasan
            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long fungsiKawasanIdLong = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(fungsiKawasanIdLong);
                existingRealisasi.setFungsiKawasanId(fungsiKawasan);
            }
            
            // Set target luas if provided
            if (targetLuas != null && !targetLuas.isEmpty()) {
                existingRealisasi.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            // Set realisasi luas if provided
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                existingRealisasi.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            // Set tahun id if provided
            if (tahunId != null && !tahunId.isEmpty()) {
                existingRealisasi.setTahunId(Integer.parseInt(tahunId));
            }
            
            // Set keterangan
            existingRealisasi.setKeterangan(keterangan);
            
            // Set status
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingRealisasi.setStatusId(status);
            }
            
            KegiatanRencanaRealisasi updatedRealisasi = service.update(id, existingRealisasi);
            return ResponseEntity.ok(updatedRealisasi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Kegiatan Rencana Realisasi")
    public ResponseEntity<Void> deleteKegiatanRencanaRealisasi(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}