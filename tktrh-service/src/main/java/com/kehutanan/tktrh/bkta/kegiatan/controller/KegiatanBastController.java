package com.kehutanan.tktrh.bkta.kegiatan.controller;

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

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanBastDeleteFilesRequest;
import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanBastPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanBast;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanBastDTO;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanBastService;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bkta/kegiatan-bast")
public class KegiatanBastController {

    private final KegiatanBastService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanBast> pagedResourcesAssembler;

    @Autowired
    public KegiatanBastController(
            KegiatanBastService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanBast> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanBastPageDTO> getAllKegiatanBast(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String identitasBkta,
            @RequestParam(required = false) String jenisBkta,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanBastPageDTO kegiatanBastPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) || 
            (identitasBkta != null && !identitasBkta.isEmpty()) || 
            (jenisBkta != null && !jenisBkta.isEmpty()) || 
            (status != null && !status.isEmpty())) {
            kegiatanBastPage = service.findByFiltersWithCache(kegiatanId, identitasBkta, jenisBkta, status, pageable, baseUrl);
        } else {
            kegiatanBastPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanBastPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanBast by keyword")
    public ResponseEntity<KegiatanBastPageDTO> searchKegiatanBast(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanBastPageDTO kegiatanBastPage = service.searchWithCache(kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanBastPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanBastDTO> getKegiatanBastById(@PathVariable Long id) {
        try {
            KegiatanBastDTO kegiatanBastDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanBastDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanBast> createKegiatanBast(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = false) String tahun,
            @RequestPart(required = false) String identitasBktaId,
            @RequestPart(required = false) String jenisBktaId,
            @RequestPart(required = false) String kelompokMasyarakatId,
            @RequestPart(required = false) String statusId) {
        
        try {
            KegiatanBast newKegiatanBast = new KegiatanBast();
            
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatanBast.setKegiatan(kegiatan);
            }
            
            if (tahun != null && !tahun.isEmpty()) {
                newKegiatanBast.setTahun(Integer.parseInt(tahun));
            }
            
            if (identitasBktaId != null && !identitasBktaId.isEmpty()) {
                Long identitasBktaIdLong = Long.parseLong(identitasBktaId);
                Lov identitasBkta = lovService.findById(identitasBktaIdLong);
                newKegiatanBast.setIdentitasBkta(identitasBkta);
            }
            
            if (jenisBktaId != null && !jenisBktaId.isEmpty()) {
                Long jenisBktaIdLong = Long.parseLong(jenisBktaId);
                Lov jenisBkta = lovService.findById(jenisBktaIdLong);
                newKegiatanBast.setJenisBkta(jenisBkta);
            }
            
            if (kelompokMasyarakatId != null && !kelompokMasyarakatId.isEmpty()) {
                Long kelompokMasyarakatIdLong = Long.parseLong(kelompokMasyarakatId);
                Lov kelompokMasyarakat = lovService.findById(kelompokMasyarakatIdLong);
                newKegiatanBast.setKelompokMasyarakat(kelompokMasyarakat);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newKegiatanBast.setStatus(status);
            }
            
            KegiatanBast savedKegiatanBast = service.save(newKegiatanBast);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatanBast);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanBast> updateKegiatanBast(
            @PathVariable Long id,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String tahun,
            @RequestPart(required = false) String identitasBktaId,
            @RequestPart(required = false) String jenisBktaId,
            @RequestPart(required = false) String kelompokMasyarakatId,
            @RequestPart(required = false) String statusId) {
        
        try {
            KegiatanBast existingKegiatanBast = service.findById(id);
            
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatanBast.setKegiatan(kegiatan);
            }
            
            if (tahun != null && !tahun.isEmpty()) {
                existingKegiatanBast.setTahun(Integer.parseInt(tahun));
            }
            
            if (identitasBktaId != null && !identitasBktaId.isEmpty()) {
                Long identitasBktaIdLong = Long.parseLong(identitasBktaId);
                Lov identitasBkta = lovService.findById(identitasBktaIdLong);
                existingKegiatanBast.setIdentitasBkta(identitasBkta);
            }
            
            if (jenisBktaId != null && !jenisBktaId.isEmpty()) {
                Long jenisBktaIdLong = Long.parseLong(jenisBktaId);
                Lov jenisBkta = lovService.findById(jenisBktaIdLong);
                existingKegiatanBast.setJenisBkta(jenisBkta);
            }
            
            if (kelompokMasyarakatId != null && !kelompokMasyarakatId.isEmpty()) {
                Long kelompokMasyarakatIdLong = Long.parseLong(kelompokMasyarakatId);
                Lov kelompokMasyarakat = lovService.findById(kelompokMasyarakatIdLong);
                existingKegiatanBast.setKelompokMasyarakat(kelompokMasyarakat);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingKegiatanBast.setStatus(status);
            }
            
            KegiatanBast updatedKegiatanBast = service.update(id, existingKegiatanBast);
            return ResponseEntity.ok(updatedKegiatanBast);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanBast(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PDF files for KegiatanBast")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "pdf", required = false) List<MultipartFile> pdf) {
        try {
            if (pdf != null) {
                service.uploadKegiatanBastPdf(id, pdf);
            }

            KegiatanBast kegiatanBast = service.findById(id);
            return ResponseEntity.ok(kegiatanBast);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete PDF files for KegiatanBast")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody(required = false) KegiatanBastDeleteFilesRequest filesRequest) {
        try {
            // Handle PDF file list if provided
            if (filesRequest.getKegiatanBastPdfIds() != null && !filesRequest.getKegiatanBastPdfIds().isEmpty()) {
                service.deleteKegiatanBastPdf(id, filesRequest.getKegiatanBastPdfIds());
            }

            // Fetch and return the updated KegiatanBast entity
            KegiatanBast kegiatanBast = service.findById(id);
            return ResponseEntity.ok(kegiatanBast);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }
}