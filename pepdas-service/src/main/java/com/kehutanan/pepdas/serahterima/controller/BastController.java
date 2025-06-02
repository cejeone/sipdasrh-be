package com.kehutanan.pepdas.serahterima.controller;

import java.time.LocalDate;
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
import com.kehutanan.pepdas.serahterima.dto.BastDTO;
import com.kehutanan.pepdas.serahterima.dto.BastDeleteFilesRequest;
import com.kehutanan.pepdas.serahterima.dto.BastPageDTO;
import com.kehutanan.pepdas.serahterima.model.Bast;
import com.kehutanan.pepdas.serahterima.service.BastService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bast")
public class BastController {

    private final BastService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<Bast> pagedResourcesAssembler;

    @Autowired
    public BastController(
            BastService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<Bast> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<BastPageDTO> getAllBast(
            @RequestParam(required = false) String nomor,
            @RequestParam(required = false) String kontrak,
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        BastPageDTO bastPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((nomor != null && !nomor.isEmpty()) ||
                (kontrak != null && !kontrak.isEmpty()) ||
                (bpdas != null && !bpdas.isEmpty())) {
            bastPage = service.findByFiltersWithCache(nomor, kontrak, bpdas, pageable, baseUrl);
        } else {
            bastPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(bastPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search BAST by keyword")
    public ResponseEntity<BastPageDTO> searchBast(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        BastPageDTO bastPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(bastPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BastDTO> getBastById(@PathVariable Long id) {
        try {
            BastDTO bastDto = service.findDTOById(id);
            return ResponseEntity.ok(bastDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Bast> createBast(
            @RequestPart(required = true) String nomorBast,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String deskripsi,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String statusId) {

        try {
            Bast newBast = new Bast();
            newBast.setNomorBast(nomorBast);
            
            if (tanggal != null && !tanggal.isEmpty()) {
                newBast.setTanggal(LocalDate.parse(tanggal));
            }
            
            newBast.setDeskripsi(deskripsi);
            
            // Set relations if IDs are provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newBast.setKegiatan(kegiatan);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newBast.setStatus(status);
            }

            Bast savedBast = service.save(newBast);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBast);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Add detailed logging for troubleshooting
            e.printStackTrace(); // Log stack trace to console

            // Return more specific error details in response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Consider sending a structured error response instead of null
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Bast> updateBast(
            @PathVariable Long id,
            @RequestPart(required = true) String nomorBast,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String deskripsi,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String statusId) {

        try {
            Bast existingBast = service.findById(id);
            
            existingBast.setNomorBast(nomorBast);
            
            if (tanggal != null && !tanggal.isEmpty()) {
                existingBast.setTanggal(LocalDate.parse(tanggal));
            }
            
            if (deskripsi != null) {
                existingBast.setDeskripsi(deskripsi);
            }
            
            // Update relations if IDs are provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingBast.setKegiatan(kegiatan);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingBast.setStatus(status);
            }

            Bast updatedBast = service.update(id, existingBast);
            return ResponseEntity.ok(updatedBast);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBast(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PDF files for BAST")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "bastPdfs", required = false) List<MultipartFile> bastPdfs) {
        try {
            if (bastPdfs != null && !bastPdfs.isEmpty()) {
                service.uploadBastPdf(id, bastPdfs);
            }

            Bast bast = service.findById(id);
            return ResponseEntity.ok(bast);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete PDF files from BAST")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody(required = false) BastDeleteFilesRequest filesRequest) {
        try {
            if (filesRequest != null && filesRequest.getBastPdfIds() != null && !filesRequest.getBastPdfIds().isEmpty()) {
                service.deleteBastPdf(id, filesRequest.getBastPdfIds());
            }

            Bast bast = service.findById(id);
            return ResponseEntity.ok(bast);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }
}