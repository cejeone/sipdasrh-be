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
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanRealisasiReboisasiPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRealisasiReboisasi;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanRealisasiReboisasiDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanRealisasiReboisasiService;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-realisasi-reboisasi")
public class KegiatanRealisasiReboisasiController {

    private final KegiatanRealisasiReboisasiService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanRealisasiReboisasi> pagedResourcesAssembler;

    @Autowired
    public KegiatanRealisasiReboisasiController(
            KegiatanRealisasiReboisasiService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanRealisasiReboisasi> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all Kegiatan Realisasi Reboisasi with optional filters")
    public ResponseEntity<KegiatanRealisasiReboisasiPageDTO> getAllKegiatanRealisasiReboisasi(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String fungsiKawasan,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanRealisasiReboisasiPageDTO resultPage;
        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if (kegiatanId != null || 
            (fungsiKawasan != null && !fungsiKawasan.isEmpty()) || 
            (keterangan != null && !keterangan.isEmpty()) ||
            (status != null && !status.isEmpty())) {
            
            resultPage = service.findByFiltersWithCache(kegiatanId, fungsiKawasan, keterangan, status, pageable, baseUrl);
        } else {
            resultPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(resultPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan Realisasi Reboisasi by keyword")
    public ResponseEntity<KegiatanRealisasiReboisasiPageDTO> searchKegiatanRealisasiReboisasi(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanRealisasiReboisasiPageDTO resultPage = service.searchWithCache(
                keyWord, pageable, request.getRequestURL().toString());
        
        return ResponseEntity.ok(resultPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Kegiatan Realisasi Reboisasi by id")
    public ResponseEntity<KegiatanRealisasiReboisasiDTO> getKegiatanRealisasiReboisasiById(
            @PathVariable Long id) {
        
        try {
            KegiatanRealisasiReboisasiDTO dto = service.findDTOById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new Kegiatan Realisasi Reboisasi")
    public ResponseEntity<KegiatanRealisasiReboisasi> createKegiatanRealisasiReboisasi(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = true) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusId) {
        
        try {
            KegiatanRealisasiReboisasi newRealisasi = new KegiatanRealisasiReboisasi();
            
            // Set kegiatan if provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newRealisasi.setKegiatan(kegiatan);
            }
            
            // Set fungsi kawasan if provided
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
            
            // Set keterangan if provided
            newRealisasi.setKeterangan(keterangan);
            
            // Set status if provided
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newRealisasi.setStatusId(status);
            }
            
            KegiatanRealisasiReboisasi savedRealisasi = service.save(newRealisasi);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRealisasi);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing Kegiatan Realisasi Reboisasi")
    public ResponseEntity<KegiatanRealisasiReboisasi> updateKegiatanRealisasiReboisasi(
            @PathVariable Long id,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusId) {
        
        try {
            KegiatanRealisasiReboisasi existingRealisasi = service.findById(id);
            
            // Update kegiatan if provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingRealisasi.setKegiatan(kegiatan);
            }
            
            // Update fungsi kawasan if provided
            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long fungsiKawasanIdLong = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(fungsiKawasanIdLong);
                existingRealisasi.setFungsiKawasanId(fungsiKawasan);
            }
            
            // Update target luas if provided
            if (targetLuas != null && !targetLuas.isEmpty()) {
                existingRealisasi.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            // Update realisasi luas if provided
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                existingRealisasi.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            // Update tahun id if provided
            if (tahunId != null && !tahunId.isEmpty()) {
                existingRealisasi.setTahunId(Integer.parseInt(tahunId));
            }
            
            // Update keterangan if provided
            if (keterangan != null) {
                existingRealisasi.setKeterangan(keterangan);
            }
            
            // Update status if provided
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingRealisasi.setStatusId(status);
            }
            
            KegiatanRealisasiReboisasi updatedRealisasi = service.update(id, existingRealisasi);
            return ResponseEntity.ok(updatedRealisasi);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Kegiatan Realisasi Reboisasi")
    public ResponseEntity<Void> deleteKegiatanRealisasiReboisasi(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}