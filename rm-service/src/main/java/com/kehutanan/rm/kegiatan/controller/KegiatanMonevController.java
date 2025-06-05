package com.kehutanan.rm.kegiatan.controller;

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

import com.kehutanan.rm.kegiatan.dto.KegiatanMonevDeleteFilesRequest;
import com.kehutanan.rm.kegiatan.dto.KegiatanMonevPageDTO;
import com.kehutanan.rm.kegiatan.model.Kegiatan;
import com.kehutanan.rm.kegiatan.model.KegiatanMonev;
import com.kehutanan.rm.kegiatan.model.dto.KegiatanMonevDTO;
import com.kehutanan.rm.kegiatan.service.KegiatanMonevService;
import com.kehutanan.rm.kegiatan.service.KegiatanService;
import com.kehutanan.rm.master.model.Lov;
import com.kehutanan.rm.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-monev")
public class KegiatanMonevController {

    private final KegiatanMonevService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanMonev> pagedResourcesAssembler;

    @Autowired
    public KegiatanMonevController(
            KegiatanMonevService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanMonev> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanMonevPageDTO> getAllKegiatanMonev(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String kegiatan,
            @RequestParam(required = false) List<String> statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevPageDTO kegiatanMonevPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) ||
                (kegiatan != null && !kegiatan.isEmpty()) ||
                (statusId != null && !statusId.isEmpty())) {
            kegiatanMonevPage = service.findByFiltersWithCache(kegiatanId, kegiatan, statusId, pageable, baseUrl);
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
        KegiatanMonevPageDTO kegiatanMonevPage = service.searchWithCache(kegiatanId, keyWord, pageable, request.getRequestURL().toString());
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
            @RequestPart String nomor,
            @RequestPart(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String tanggal,
            @RequestPart(required = false) String deskripsi,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String statusId) {

        try {
            KegiatanMonev newKegiatanMonev = new KegiatanMonev();
            
            newKegiatanMonev.setNomor(nomor);
            
            // Parse and set tanggal if provided
            if (tanggal != null && !tanggal.isEmpty()) {
                newKegiatanMonev.setTanggal(LocalDate.parse(tanggal));
            }
            
            newKegiatanMonev.setDeskripsi(deskripsi);

            // Set kegiatan if ID is provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatanMonev.setKegiatan(kegiatan);
            }

            // Set status if ID is provided
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newKegiatanMonev.setStatusId(status);
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
            @RequestPart String nomor,
            @RequestPart(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String tanggal,
            @RequestPart(required = false) String deskripsi,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String statusId) {

        try {
            KegiatanMonev existingKegiatanMonev = service.findById(id);
            
            existingKegiatanMonev.setNomor(nomor);
            
            // Parse and set tanggal if provided
            if (tanggal != null && !tanggal.isEmpty()) {
                existingKegiatanMonev.setTanggal(LocalDate.parse(tanggal));
            }
            
            existingKegiatanMonev.setDeskripsi(deskripsi);

            // Update kegiatan if ID is provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatanMonev.setKegiatan(kegiatan);
            }

            // Update status if ID is provided
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingKegiatanMonev.setStatusId(status);
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

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PDF files for KegiatanMonev")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "pdfs", required = false) List<MultipartFile> pdfs) {
        try {
            if (pdfs != null && !pdfs.isEmpty()) {
                service.uploadPdf(id, pdfs);
            }

            KegiatanMonev kegiatanMonev = service.findById(id);
            return ResponseEntity.ok(kegiatanMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete PDF files from KegiatanMonev")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody(required = false) KegiatanMonevDeleteFilesRequest filesRequest) {
        try {
            // Handle PDF file deletion if provided
            if (filesRequest.getPdfIds() != null && !filesRequest.getPdfIds().isEmpty()) {
                service.deletePdf(id, filesRequest.getPdfIds());
            }

            // Fetch and return the updated entity
            KegiatanMonev kegiatanMonev = service.findById(id);
            return ResponseEntity.ok(kegiatanMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }
}