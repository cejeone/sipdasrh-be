package com.kehutanan.pepdas.monev.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.service.KegiatanService;
import com.kehutanan.pepdas.master.model.Lov;
import com.kehutanan.pepdas.master.service.LovService;
import com.kehutanan.pepdas.monev.dto.MonevDTO;
import com.kehutanan.pepdas.monev.dto.MonevDeleteFilesRequest;
import com.kehutanan.pepdas.monev.dto.MonevPageDTO;
import com.kehutanan.pepdas.monev.model.Monev;
import com.kehutanan.pepdas.monev.service.MonevService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/monev")
public class MonevController {

    private final MonevService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<Monev> pagedResourcesAssembler;

    @Autowired
    public MonevController(
            MonevService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<Monev> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<MonevPageDTO> getAllMonev(
            @RequestParam(required = false) String nomor,
            @RequestParam(required = false) String kegiatan,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        MonevPageDTO monevPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((nomor != null && !nomor.isEmpty()) ||
                (kegiatan != null && !kegiatan.isEmpty()) ||
                (status != null && !status.isEmpty())) {
            monevPage = service.findByFiltersWithCache(nomor, kegiatan, status, pageable, baseUrl);
        } else {
            monevPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(monevPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Monev by keyword")
    public ResponseEntity<MonevPageDTO> searchMonev(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        MonevPageDTO monevPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(monevPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonevDTO> getMonevById(@PathVariable Long id) {
        try {
            MonevDTO monevDTO = service.findDTOById(id);
            return ResponseEntity.ok(monevDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Monev> createMonev(
            @RequestPart String nomor,
            @RequestPart(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String tanggal,
            @RequestPart(required = false) String deskripsi,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String statusId) {

        try {
            Monev newMonev = new Monev();
            newMonev.setNomor(nomor);
            
            if (tanggal != null && !tanggal.isEmpty()) {
                newMonev.setTanggal(LocalDate.parse(tanggal));
            }
            
            newMonev.setDeskripsi(deskripsi);

            // Set relations if IDs are provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newMonev.setKegiatan(kegiatan);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newMonev.setStatus(status);
            }

            Monev savedMonev = service.save(newMonev);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Add detailed logging for troubleshooting
            e.printStackTrace(); // Log stack trace to console

            // Return more specific error details in response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Monev> updateMonev(
            @PathVariable Long id,
            @RequestPart String nomor,
            @RequestPart(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String tanggal,
            @RequestPart(required = false) String deskripsi,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String statusId) {

        try {
            Monev existingMonev = service.findById(id);

            existingMonev.setNomor(nomor);
            
            if (tanggal != null && !tanggal.isEmpty()) {
                existingMonev.setTanggal(LocalDate.parse(tanggal));
            }
            
            existingMonev.setDeskripsi(deskripsi);

            // Update relations if IDs are provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingMonev.setKegiatan(kegiatan);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingMonev.setStatus(status);
            }

            Monev updatedMonev = service.update(id, existingMonev);
            return ResponseEntity.ok(updatedMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonev(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PDF files for Monev")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "monevPdfs", required = false) List<MultipartFile> monevPdfs) {
        try {
            if (monevPdfs != null) {
                service.uploadMonevPdf(id, monevPdfs);
            }

            Monev monev = service.findById(id);
            return ResponseEntity.ok(monev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete PDF files for Monev")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody(required = false) MonevDeleteFilesRequest filesRequest) {
        try {
            // Handle PDF file deletion if provided
            if (filesRequest.getPdfIds() != null && !filesRequest.getPdfIds().isEmpty()) {
                service.deleteMonevPdf(id, filesRequest.getPdfIds());
            }

            // Fetch and return the updated Monev entity
            Monev monev = service.findById(id);
            return ResponseEntity.ok(monev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }
}