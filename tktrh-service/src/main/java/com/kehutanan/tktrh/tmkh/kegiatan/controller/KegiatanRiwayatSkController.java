package com.kehutanan.tktrh.tmkh.kegiatan.controller;

import java.time.LocalDate;
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
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanRiwayatSkPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRiwayatSk;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanRiwayatSkDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanRiwayatSkService;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-riwayat-sk")
public class KegiatanRiwayatSkController {

    private final KegiatanRiwayatSkService service;
    private final KegiatanService kegiatanService;
    private final PagedResourcesAssembler<KegiatanRiwayatSk> pagedResourcesAssembler;

    @Autowired
    private LovService lovService; // Add this to your class fields and constructor

    @Autowired
    public KegiatanRiwayatSkController(
            KegiatanRiwayatSkService service,
            KegiatanService kegiatanService,
            PagedResourcesAssembler<KegiatanRiwayatSk> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanRiwayatSkPageDTO> getAllKegiatanRiwayatSk(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String skPenetapan,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> statusList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanRiwayatSkPageDTO riwayatSkPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) || 
            (skPenetapan != null && !skPenetapan.isEmpty()) ||
            (keterangan != null && !keterangan.isEmpty()) ||
            (statusList != null && !statusList.isEmpty())) {
            riwayatSkPage = service.findByFiltersWithCache(kegiatanId, skPenetapan, keterangan, statusList, pageable, baseUrl);
        } else {
            riwayatSkPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(riwayatSkPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanRiwayatSk by keyword")
    public ResponseEntity<KegiatanRiwayatSkPageDTO> searchKegiatanRiwayatSk(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanRiwayatSkPageDTO riwayatSkPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(riwayatSkPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanRiwayatSkDTO> getKegiatanRiwayatSkById(@PathVariable Long id) {
        try {
            KegiatanRiwayatSkDTO riwayatSkDto = service.findDTOById(id);
            return ResponseEntity.ok(riwayatSkDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanRiwayatSk> createKegiatanRiwayatSk(
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String jenisPerubahanId,
            @RequestPart(required = false) String nomorSk,
            @RequestPart(required = false) String tanggalSk,
            @RequestPart(required = false) String tanggalBerakhirSk,
            @RequestPart(required = false) String luasSkHa,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String keterangan
            ) {

        try {
            KegiatanRiwayatSk newRiwayatSk = new KegiatanRiwayatSk();
            
            // Set optional fields if provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                newRiwayatSk.setKegiatan(kegiatanService.findById(Long.parseLong(kegiatanId)));
            }
            
            if (jenisPerubahanId != null && !jenisPerubahanId.isEmpty()) {
                newRiwayatSk.setJenisPerubahan(lovService.findById(Long.parseLong(jenisPerubahanId)));
            }
            
            if (nomorSk != null) {
                newRiwayatSk.setNomorSk(nomorSk);
            }
            
            if (tanggalSk != null && !tanggalSk.isEmpty()) {
                newRiwayatSk.setTanggalSk(LocalDate.parse(tanggalSk));
            }
            
            if (tanggalBerakhirSk != null && !tanggalBerakhirSk.isEmpty()) {
                newRiwayatSk.setTanggalBerakhirSk(LocalDate.parse(tanggalBerakhirSk));
            }
            
            if (luasSkHa != null && !luasSkHa.isEmpty()) {
                newRiwayatSk.setLuasSkHa(Double.parseDouble(luasSkHa));
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                newRiwayatSk.setStatus(lovService.findById(Long.parseLong(statusId)));
            }
            
            if (keterangan != null) {
                newRiwayatSk.setKeterangan(keterangan);
            }

            // Save the entity first to get an ID
            KegiatanRiwayatSk savedRiwayatSk = service.save(newRiwayatSk);

    

            return ResponseEntity.status(HttpStatus.CREATED).body(savedRiwayatSk);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Add detailed logging for troubleshooting
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanRiwayatSk> updateKegiatanRiwayatSk(
            @PathVariable Long id,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String jenisPerubahanId,
            @RequestPart(required = false) String nomorSk,
            @RequestPart(required = false) String tanggalSk,
            @RequestPart(required = false) String tanggalBerakhirSk,
            @RequestPart(required = false) String luasSkHa,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String keterangan) {

        try {
            KegiatanRiwayatSk existingRiwayatSk = service.findById(id);
            
            // Update optional fields if provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                existingRiwayatSk.setKegiatan(kegiatanService.findById(Long.parseLong(kegiatanId)));
            }
            
            if (jenisPerubahanId != null && !jenisPerubahanId.isEmpty()) {
                existingRiwayatSk.setJenisPerubahan(lovService.findById(Long.parseLong(jenisPerubahanId)));
            }
            
            if (nomorSk != null) {
                existingRiwayatSk.setNomorSk(nomorSk);
            }
            
            if (tanggalSk != null && !tanggalSk.isEmpty()) {
                existingRiwayatSk.setTanggalSk(LocalDate.parse(tanggalSk));
            }
            
            if (tanggalBerakhirSk != null && !tanggalBerakhirSk.isEmpty()) {
                existingRiwayatSk.setTanggalBerakhirSk(LocalDate.parse(tanggalBerakhirSk));
            }
            
            if (luasSkHa != null && !luasSkHa.isEmpty()) {
                existingRiwayatSk.setLuasSkHa(Double.parseDouble(luasSkHa));
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                existingRiwayatSk.setStatus(lovService.findById(Long.parseLong(statusId)));
            }
            
            if (keterangan != null) {
                existingRiwayatSk.setKeterangan(keterangan);
            }

            KegiatanRiwayatSk updatedRiwayatSk = service.update(id, existingRiwayatSk);
            return ResponseEntity.ok(updatedRiwayatSk);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanRiwayatSk(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/shp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload SHP files for KegiatanRiwayatSk")
    public ResponseEntity<?> uploadShpFiles(
            @PathVariable Long id,
            @RequestPart List<MultipartFile> shpFiles) {
        try {
            KegiatanRiwayatSk updatedRiwayatSk = service.uploadShp(id, shpFiles);
            return ResponseEntity.ok(updatedRiwayatSk);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload SHP files: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/shp")
    @Operation(summary = "Delete SHP files for KegiatanRiwayatSk")
    public ResponseEntity<?> deleteShpFiles(
            @PathVariable Long id,
            @RequestParam List<String> uuidShp) {
        try {
            KegiatanRiwayatSk updatedRiwayatSk = service.deleteShp(id, uuidShp);
            return ResponseEntity.ok(updatedRiwayatSk);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete SHP files: " + e.getMessage());
        }
    }
}