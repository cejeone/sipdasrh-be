package com.kehutanan.tktrh.bkta.kegiatan.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanMonevPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanMonevDTO;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanMonevService;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController("bktaKegiatanMonevController")
@RequestMapping("/api/bkta/kegiatan-monev")
public class KegiatanMonevController {

    private final KegiatanMonevService service;
    private final KegiatanService kegiatanService;
    private final PagedResourcesAssembler<KegiatanMonev> pagedResourcesAssembler;

    @Autowired
    private LovService lovService;

    @Autowired
    public KegiatanMonevController(
            KegiatanMonevService service,
            KegiatanService kegiatanService,
            PagedResourcesAssembler<KegiatanMonev> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanMonevPageDTO> getAllKegiatanMonev(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String nomor,
            @RequestParam(required = false) String deskripsi,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevPageDTO kegiatanMonevPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) || 
            (nomor != null && !nomor.isEmpty()) || 
            (deskripsi != null && !deskripsi.isEmpty()) ||
            (status != null && !status.isEmpty())) {
            kegiatanMonevPage = service.findByFiltersWithCache(kegiatanId, nomor, deskripsi, status, pageable, baseUrl);
        } else {
            kegiatanMonevPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanMonevPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanMonev by keyword")
    public ResponseEntity<KegiatanMonevPageDTO> searchKegiatanMonev(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevPageDTO kegiatanMonevPage = service.searchWithCache(kegiatanId, keyWord, pageable, 
                request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanMonevPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanMonevDTO> getKegiatanMonevById(@PathVariable Long id) {
        try {
            KegiatanMonevDTO kegiatanMonevDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanMonevDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanMonev> createKegiatanMonev(
            @RequestPart(required = true) String nomor,
            @RequestPart(required = true) String deskripsi,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String kegiatanId) {

        try {
            KegiatanMonev newKegiatanMonev = new KegiatanMonev();
            newKegiatanMonev.setNomor(nomor);
            newKegiatanMonev.setDeskripsi(deskripsi);

            if (tanggal != null && !tanggal.isEmpty()) {
                // Using java.util.Date as per entity definition
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                newKegiatanMonev.setTanggal(dateFormat.parse(tanggal));
            }

            // Set status if provided (assuming you have a LovService to find Lov by id)
            if (statusId != null && !statusId.isEmpty()) {
                Long lovId = Long.parseLong(statusId);
                Lov status = lovService.findById(lovId);
                newKegiatanMonev.setStatus(status);
            }

            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatanMonev.setKegiatan(kegiatan);
            }

            KegiatanMonev savedKegiatanMonev = service.save(newKegiatanMonev);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatanMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanMonev> updateKegiatanMonev(
            @PathVariable Long id,
            @RequestPart(required = true) String nomor,
            @RequestPart(required = true) String deskripsi,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String kegiatanId) {

        try {
            KegiatanMonev existingKegiatanMonev = service.findById(id);

            existingKegiatanMonev.setNomor(nomor);
            existingKegiatanMonev.setDeskripsi(deskripsi);

            if (tanggal != null && !tanggal.isEmpty()) {
                // Using java.util.Date as per entity definition
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                existingKegiatanMonev.setTanggal(dateFormat.parse(tanggal));
            }

            // Set status if provided (assuming you have a LovService to find Lov by id)
            if (statusId != null && !statusId.isEmpty()) {
                Long lovId = Long.parseLong(statusId);
                Lov status = lovService.findById(lovId);
                existingKegiatanMonev.setStatus(status);
            }

            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatanMonev.setKegiatan(kegiatan);
            }

            KegiatanMonev updatedKegiatanMonev = service.update(id, existingKegiatanMonev);
            return ResponseEntity.ok(updatedKegiatanMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanMonev(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PDF files for KegiatanMonev")
    public ResponseEntity<?> uploadPdfFiles(
            @PathVariable Long id,
            @RequestPart(value = "files") List<MultipartFile> files) {
        try {
            KegiatanMonev kegiatanMonev = service.uploadMonevPdf(id, files);
            return ResponseEntity.ok(kegiatanMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload PDF files: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/pdf")
    @Operation(summary = "Delete PDF files for KegiatanMonev")
    public ResponseEntity<?> deletePdfFiles(
            @PathVariable Long id,
            @RequestBody List<String> pdfIds) {
        try {
            KegiatanMonev kegiatanMonev = service.deleteMonevPdf(id, pdfIds);
            return ResponseEntity.ok(kegiatanMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete PDF files: " + e.getMessage());
        }
    }
}