package com.kehutanan.tktrh.perijinan.controller;

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

import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.PelakuUsaha;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.master.service.PelakuUsahaService;
import com.kehutanan.tktrh.perijinan.dto.PerijinanPageDTO;
import com.kehutanan.tktrh.perijinan.model.Perijinan;
import com.kehutanan.tktrh.perijinan.model.dto.PerijinanDTO;
import com.kehutanan.tktrh.perijinan.service.PerijinanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/perijinan")
public class PerijinanController {

    private final PerijinanService service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Perijinan> pagedResourcesAssembler;
    private final PelakuUsahaService pelakuUsahaService;

    @Autowired
    public PerijinanController(
            PerijinanService service,
            LovService lovService,
            PelakuUsahaService pelakuUsahaService,
            PagedResourcesAssembler<Perijinan> pagedResourcesAssembler) {
        this.service = service;
        this.pelakuUsahaService = pelakuUsahaService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PerijinanPageDTO> getAllPerijinan(
            @RequestParam(required = false) String pelakuUsaha,
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        PerijinanPageDTO perijinanPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((pelakuUsaha != null && !pelakuUsaha.isEmpty()) ||
                (bpdas != null && !bpdas.isEmpty())) {
            perijinanPage = service.findByFiltersWithCache(pelakuUsaha, bpdas, pageable, baseUrl);
        } else {
            perijinanPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(perijinanPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Perijinan by keyword")
    public ResponseEntity<PerijinanPageDTO> searchPerijinan(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        PerijinanPageDTO perijinanPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(perijinanPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerijinanDTO> getPerijinanById(@PathVariable Long id) {
        try {
            PerijinanDTO perijinanDTO = service.findDTOById(id);
            return ResponseEntity.ok(perijinanDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Perijinan> createPerijinan(
            @RequestPart(required = false) String pelakuUsahaId,
            @RequestPart(required = false) String levelId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String tanggalPengajuan,
            @RequestPart(required = false) String tanggalPenetapan,
            @RequestPart(required = false) String catatanDokumenAwal,
            @RequestPart(required = false) String catatanDokumenBast,
            @RequestPart(required = false) String keterangan) {

        try {
            Perijinan newPerijinan = new Perijinan();
            
            if (pelakuUsahaId != null && !pelakuUsahaId.isEmpty()) {
                Long pelakuUsahaIdLong = Long.parseLong(pelakuUsahaId);
                PelakuUsaha pelakuUsaha = pelakuUsahaService.findById(pelakuUsahaIdLong);
                newPerijinan.setPelakuUsahaId(pelakuUsaha);
            }
            
            if (levelId != null && !levelId.isEmpty()) {
                Long levelIdLong = Long.parseLong(levelId);
                Lov level = lovService.findById(levelIdLong);
                newPerijinan.setLevelId(level);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newPerijinan.setStatusId(status);
            }
            
            if (tanggalPengajuan != null && !tanggalPengajuan.isEmpty()) {
                newPerijinan.setTanggalPengajuan(LocalDateTime.parse(tanggalPengajuan));
            }
            
            if (tanggalPenetapan != null && !tanggalPenetapan.isEmpty()) {
                newPerijinan.setTanggalPenetapan(LocalDateTime.parse(tanggalPenetapan));
            }
            
            newPerijinan.setCatatanDokumenAwal(catatanDokumenAwal);
            newPerijinan.setCatatanDokumenBast(catatanDokumenBast);
            newPerijinan.setKeterangan(keterangan);
            
            Perijinan savedPerijinan = service.save(newPerijinan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPerijinan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Perijinan> updatePerijinan(
            @PathVariable Long id,
            @RequestPart(required = false) String pelakuUsahaId,
            @RequestPart(required = false) String levelId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String tanggalPengajuan,
            @RequestPart(required = false) String tanggalPenetapan,
            @RequestPart(required = false) String catatanDokumenAwal,
            @RequestPart(required = false) String catatanDokumenBast,
            @RequestPart(required = false) String keterangan) {

        try {
            Perijinan existingPerijinan = service.findById(id);
            
            if (pelakuUsahaId != null && !pelakuUsahaId.isEmpty()) {
                Long pelakuUsahaIdLong = Long.parseLong(pelakuUsahaId);
                PelakuUsaha pelakuUsaha = pelakuUsahaService.findById(pelakuUsahaIdLong);
                existingPerijinan.setPelakuUsahaId(pelakuUsaha);
            }
            
            if (levelId != null && !levelId.isEmpty()) {
                Long levelIdLong = Long.parseLong(levelId);
                Lov level = lovService.findById(levelIdLong);
                existingPerijinan.setLevelId(level);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingPerijinan.setStatusId(status);
            }
            
            if (tanggalPengajuan != null && !tanggalPengajuan.isEmpty()) {
                existingPerijinan.setTanggalPengajuan(LocalDateTime.parse(tanggalPengajuan));
            }
            
            if (tanggalPenetapan != null && !tanggalPenetapan.isEmpty()) {
                existingPerijinan.setTanggalPenetapan(LocalDateTime.parse(tanggalPenetapan));
            }
            
            if (catatanDokumenAwal != null) {
                existingPerijinan.setCatatanDokumenAwal(catatanDokumenAwal);
            }
            
            if (catatanDokumenBast != null) {
                existingPerijinan.setCatatanDokumenBast(catatanDokumenBast);
            }
            
            if (keterangan != null) {
                existingPerijinan.setKeterangan(keterangan);
            }
            
            Perijinan updatedPerijinan = service.update(id, existingPerijinan);
            return ResponseEntity.ok(updatedPerijinan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerijinan(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/dokumen-awal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload dokumen awal PDF for Perijinan")
    public ResponseEntity<?> uploadDokumenAwalPdf(
            @PathVariable Long id,
            @RequestPart(value = "files") List<MultipartFile> files) {
        try {
            Perijinan perijinan = service.uploadDokumenAwalPdf(id, files);
            return ResponseEntity.ok(perijinan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload dokumen awal: " + e.getMessage());
        }
    }
    
    @PostMapping(value = "/{id}/dokumen-bast", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload dokumen BAST PDF for Perijinan")
    public ResponseEntity<?> uploadDokumenBastPdf(
            @PathVariable Long id,
            @RequestPart(value = "files") List<MultipartFile> files) {
        try {
            Perijinan perijinan = service.uploadDokumenBastPdf(id, files);
            return ResponseEntity.ok(perijinan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload dokumen BAST: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/dokumen-awal")
    @Operation(summary = "Delete dokumen awal PDF files for Perijinan")
    public ResponseEntity<?> deleteDokumenAwalPdf(
            @PathVariable Long id,
            @RequestParam List<String> uuidPdf) {
        try {
            Perijinan perijinan = service.deleteDokumenAwalPdf(id, uuidPdf);
            return ResponseEntity.ok(perijinan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus dokumen awal: " + e.getMessage());
        }
    }
    
    @DeleteMapping(value = "/{id}/dokumen-bast")
    @Operation(summary = "Delete dokumen BAST PDF files for Perijinan")
    public ResponseEntity<?> deleteDokumenBastPdf(
            @PathVariable Long id,
            @RequestParam List<String> uuidPdf) {
        try {
            Perijinan perijinan = service.deleteDokumenBastPdf(id, uuidPdf);
            return ResponseEntity.ok(perijinan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus dokumen BAST: " + e.getMessage());
        }
    }
}