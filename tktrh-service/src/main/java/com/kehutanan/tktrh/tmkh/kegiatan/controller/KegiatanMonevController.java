package com.kehutanan.tktrh.tmkh.kegiatan.controller;

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

import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanMonevPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanMonevDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanMonevService;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanService;

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
    @Operation(summary = "Get all Kegiatan Monev with optional filtering")
    public ResponseEntity<KegiatanMonevPageDTO> getAllKegiatanMonev(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String nomor,
            @RequestParam(required = false) String deskripsi,
            @RequestParam(required = false) List<String> statusList,
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
            (statusList != null && !statusList.isEmpty())) {
            
            kegiatanMonevPage = service.findByFiltersWithCache(
                kegiatanId, nomor, deskripsi, statusList, pageable, baseUrl);
        } else {
            kegiatanMonevPage = service.findAllWithCache(pageable, baseUrl);
        }
        
        return ResponseEntity.ok(kegiatanMonevPage);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan Monev by keyword")
    public ResponseEntity<KegiatanMonevPageDTO> searchKegiatanMonev(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanMonevPageDTO kegiatanMonevPage = 
            service.searchWithCache(kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        
        return ResponseEntity.ok(kegiatanMonevPage);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get Kegiatan Monev by ID")
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
    @Operation(summary = "Create a new Kegiatan Monev")
    public ResponseEntity<KegiatanMonev> createKegiatanMonev(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = true) String statusId,
            @RequestPart(required = true) String nomor,
            @RequestPart(required = true) String tanggal,
            @RequestPart(required = false) String deskripsi) {
        
        try {
            KegiatanMonev newKegiatanMonev = new KegiatanMonev();
            
            // Set kegiatan
            Long kegiatanIdLong = Long.parseLong(kegiatanId);
            Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
            newKegiatanMonev.setKegiatan(kegiatan);
            
            // Set status
            Long statusIdLong = Long.parseLong(statusId);
            Lov status = lovService.findById(statusIdLong);
            newKegiatanMonev.setStatusId(status);
            
            // Set nomor
            newKegiatanMonev.setNomor(nomor);
            
            // Set tanggal
            if (tanggal != null && !tanggal.isEmpty()) {
                // Assuming date is in ISO format (yyyy-MM-dd)
                newKegiatanMonev.setTanggal(LocalDate.parse(tanggal));
            }
            
            // Set deskripsi
            newKegiatanMonev.setDeskripsi(deskripsi);
            
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
    @Operation(summary = "Update an existing Kegiatan Monev")
    public ResponseEntity<KegiatanMonev> updateKegiatanMonev(
            @PathVariable Long id,
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = true) String statusId,
            @RequestPart(required = true) String nomor,
            @RequestPart(required = true) String tanggal,
            @RequestPart(required = false) String deskripsi) {
        
        try {
            KegiatanMonev existingKegiatanMonev = service.findById(id);
            
            // Update kegiatan
            Long kegiatanIdLong = Long.parseLong(kegiatanId);
            Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
            existingKegiatanMonev.setKegiatan(kegiatan);
            
            // Update status
            Long statusIdLong = Long.parseLong(statusId);
            Lov status = lovService.findById(statusIdLong);
            existingKegiatanMonev.setStatusId(status);
            
            // Update nomor
            existingKegiatanMonev.setNomor(nomor);
            
            // Update tanggal
            if (tanggal != null && !tanggal.isEmpty()) {
                existingKegiatanMonev.setTanggal(LocalDate.parse(tanggal));
            }
            
            // Update deskripsi
            existingKegiatanMonev.setDeskripsi(deskripsi);
            
            KegiatanMonev updatedKegiatanMonev = service.update(id, existingKegiatanMonev);
            return ResponseEntity.ok(updatedKegiatanMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Kegiatan Monev")
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
    @Operation(summary = "Upload PDF files for Kegiatan Monev")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "pdfs", required = false) List<MultipartFile> pdfs) {
        try {
            if (pdfs != null && !pdfs.isEmpty()) {
                service.uploadKegiatanMonevPdf(id, pdfs);
            }

            KegiatanMonev kegiatanMonev = service.findById(id);
            return ResponseEntity.ok(kegiatanMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file PDF: " + e.getMessage());
        }
    }
    
    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete PDF files from Kegiatan Monev")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody List<String> pdfIds) {
        try {
            if (pdfIds != null && !pdfIds.isEmpty()) {
                service.deleteKegiatanMonevPdf(id, pdfIds);
            }

            // Fetch and return the updated Kegiatan Monev entity
            KegiatanMonev kegiatanMonev = service.findById(id);
            return ResponseEntity.ok(kegiatanMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file PDF: " + e.getMessage());
        }
    }
}