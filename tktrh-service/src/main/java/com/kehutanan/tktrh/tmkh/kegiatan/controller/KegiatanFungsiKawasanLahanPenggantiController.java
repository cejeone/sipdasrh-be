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
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanFungsiKawasanLahanPenggantiPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanLahanPengganti;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanFungsiKawasanLahanPenggantiDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanFungsiKawasanLahanPenggantiService;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-fungsi-kawasan-lahan-pengganti")
public class KegiatanFungsiKawasanLahanPenggantiController {

    private final KegiatanFungsiKawasanLahanPenggantiService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanFungsiKawasanLahanPengganti> pagedResourcesAssembler;

    @Autowired
    public KegiatanFungsiKawasanLahanPenggantiController(
            KegiatanFungsiKawasanLahanPenggantiService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanFungsiKawasanLahanPengganti> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanFungsiKawasanLahanPenggantiPageDTO> getAllKegiatanFungsiKawasan(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String fungsiKawasan,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> statusList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanFungsiKawasanLahanPenggantiPageDTO kegiatanPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) || 
            (fungsiKawasan != null && !fungsiKawasan.isEmpty()) ||
            (keterangan != null && !keterangan.isEmpty()) ||
            (statusList != null && !statusList.isEmpty())) {
            kegiatanPage = service.findByFiltersWithCache(kegiatanId, fungsiKawasan, keterangan, statusList, pageable, baseUrl);
        } else {
            kegiatanPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanFungsiKawasanLahanPengganti by keyword")
    public ResponseEntity<KegiatanFungsiKawasanLahanPenggantiPageDTO> searchKegiatanFungsiKawasan(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanFungsiKawasanLahanPenggantiPageDTO kegiatanPage = service.searchWithCache(kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanFungsiKawasanLahanPenggantiDTO> getKegiatanFungsiKawasanById(@PathVariable Long id) {
        try {
            KegiatanFungsiKawasanLahanPenggantiDTO kegiatanDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanFungsiKawasanLahanPengganti> createKegiatanFungsiKawasan(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = false) String fungsiKawasanId,
            @RequestPart(required = false) String luas,
            @RequestPart(required = false) String tumpangTindihId,
            @RequestPart(required = false) String rasioTumpangTindih,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusId) {

        try {
            KegiatanFungsiKawasanLahanPengganti newKawasan = new KegiatanFungsiKawasanLahanPengganti();
            
            // Set kegiatan relationship
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long idKegiatan = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(idKegiatan);
                newKawasan.setKegiatan(kegiatan);
            } else {
                return ResponseEntity.badRequest().body(null);
            }

            // Set fungsiKawasanId relationship
            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long idFungsiKawasan = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(idFungsiKawasan);
                newKawasan.setFungsiKawasanId(fungsiKawasan);
            }
            
            // Set luas
            if (luas != null && !luas.isEmpty()) {
                newKawasan.setLuas(Double.parseDouble(luas));
            }
            
            // Set tumpangTindih relationship
            if (tumpangTindihId != null && !tumpangTindihId.isEmpty()) {
                Long idTumpangTindih = Long.parseLong(tumpangTindihId);
                Lov tumpangTindih = lovService.findById(idTumpangTindih);
                newKawasan.setTumpangTindih(tumpangTindih);
            }
            
            // Set rasio tumpang tindih
            if (rasioTumpangTindih != null && !rasioTumpangTindih.isEmpty()) {
                newKawasan.setRasioTumpangTindih(Double.parseDouble(rasioTumpangTindih));
            }
            
            // Set keterangan
            newKawasan.setKeterangan(keterangan);
            
            // Set status relationship
            if (statusId != null && !statusId.isEmpty()) {
                Long idStatus = Long.parseLong(statusId);
                Lov status = lovService.findById(idStatus);
                newKawasan.setStatusId(status);
            }

            KegiatanFungsiKawasanLahanPengganti savedKawasan = service.save(newKawasan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKawasan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanFungsiKawasanLahanPengganti> updateKegiatanFungsiKawasan(
            @PathVariable Long id,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String fungsiKawasanId,
            @RequestPart(required = false) String luas,
            @RequestPart(required = false) String tumpangTindihId,
            @RequestPart(required = false) String rasioTumpangTindih,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusId) {

        try {
            KegiatanFungsiKawasanLahanPengganti existingKawasan = service.findById(id);
            
            // Update kegiatan relationship if provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long idKegiatan = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(idKegiatan);
                existingKawasan.setKegiatan(kegiatan);
            }

            // Update fungsiKawasanId relationship if provided
            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long idFungsiKawasan = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(idFungsiKawasan);
                existingKawasan.setFungsiKawasanId(fungsiKawasan);
            }
            
            // Update luas if provided
            if (luas != null && !luas.isEmpty()) {
                existingKawasan.setLuas(Double.parseDouble(luas));
            }
            
            // Update tumpangTindih relationship if provided
            if (tumpangTindihId != null && !tumpangTindihId.isEmpty()) {
                Long idTumpangTindih = Long.parseLong(tumpangTindihId);
                Lov tumpangTindih = lovService.findById(idTumpangTindih);
                existingKawasan.setTumpangTindih(tumpangTindih);
            }
            
            // Update rasio tumpang tindih if provided
            if (rasioTumpangTindih != null && !rasioTumpangTindih.isEmpty()) {
                existingKawasan.setRasioTumpangTindih(Double.parseDouble(rasioTumpangTindih));
            }
            
            // Update keterangan if provided
            if (keterangan != null) {
                existingKawasan.setKeterangan(keterangan);
            }
            
            // Update status relationship if provided
            if (statusId != null && !statusId.isEmpty()) {
                Long idStatus = Long.parseLong(statusId);
                Lov status = lovService.findById(idStatus);
                existingKawasan.setStatusId(status);
            }

            KegiatanFungsiKawasanLahanPengganti updatedKawasan = service.update(id, existingKawasan);
            return ResponseEntity.ok(updatedKawasan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanFungsiKawasan(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}