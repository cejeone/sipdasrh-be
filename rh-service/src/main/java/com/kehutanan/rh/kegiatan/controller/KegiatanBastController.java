package com.kehutanan.rh.kegiatan.controller;

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

import com.kehutanan.rh.kegiatan.dto.KegiatanBastDeleteFilesRequest;
import com.kehutanan.rh.kegiatan.dto.KegiatanBastPageDTO;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanBast;
import com.kehutanan.rh.kegiatan.model.dto.KegiatanBastDTO;
import com.kehutanan.rh.kegiatan.service.KegiatanBastService;
import com.kehutanan.rh.kegiatan.service.KegiatanService;
import com.kehutanan.rh.master.model.Lov;
import com.kehutanan.rh.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-bast")
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
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> jenis,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanBastPageDTO kegiatanBastPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if (kegiatanId != null || (keterangan != null && !keterangan.isEmpty()) || 
            (jenis != null && !jenis.isEmpty())) {
            kegiatanBastPage = service.findByFiltersWithCache(kegiatanId, keterangan, jenis, pageable, baseUrl);
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
        KegiatanBastPageDTO kegiatanBastPage = service.searchWithCache(kegiatanId, keyWord, pageable, 
                request.getRequestURL().toString());
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
            @RequestPart String tahun,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String jenisTanamanId,
            @RequestPart(required = false) String kelompokMasyarakatId,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String tahapId) {

        try {
            KegiatanBast newKegiatanBast = new KegiatanBast();
            newKegiatanBast.setTahun(tahun);
            newKegiatanBast.setTargetLuas(targetLuas);
            newKegiatanBast.setRealisasiLuas(realisasiLuas);
            newKegiatanBast.setJenisTanamanId(jenisTanamanId);
            newKegiatanBast.setKelompokMasyarakatId(kelompokMasyarakatId);

            // Set kegiatan relation if ID provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatanBast.setKegiatan(kegiatan);
            }

            // Set status if provided
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newKegiatanBast.setStatusId(status);
            }

            // Set tahap if provided
            if (tahapId != null && !tahapId.isEmpty()) {
                Long tahapIdLong = Long.parseLong(tahapId);
                Lov tahap = lovService.findById(tahapIdLong);
                newKegiatanBast.setTahapId(tahap);
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
            @RequestPart String tahun,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String jenisTanamanId,
            @RequestPart(required = false) String kelompokMasyarakatId,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String tahapId) {

        try {
            KegiatanBast existingKegiatanBast = service.findById(id);

            existingKegiatanBast.setTahun(tahun);
            existingKegiatanBast.setTargetLuas(targetLuas);
            existingKegiatanBast.setRealisasiLuas(realisasiLuas);
            existingKegiatanBast.setJenisTanamanId(jenisTanamanId);
            existingKegiatanBast.setKelompokMasyarakatId(kelompokMasyarakatId);

            // Update kegiatan relation if ID provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatanBast.setKegiatan(kegiatan);
            }

            // Update status if provided
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingKegiatanBast.setStatusId(status);
            }

            // Update tahap if provided
            if (tahapId != null && !tahapId.isEmpty()) {
                Long tahapIdLong = Long.parseLong(tahapId);
                Lov tahap = lovService.findById(tahapIdLong);
                existingKegiatanBast.setTahapId(tahap);
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

    @PostMapping(value = "/{id}/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PDF files for KegiatanBast")
    public ResponseEntity<?> uploadPdf(
            @PathVariable Long id,
            @RequestPart(value = "files") List<MultipartFile> files) {
        try {
            KegiatanBast kegiatanBast = service.uploadKegiatanBastPdf(id, files);
            return ResponseEntity.ok(kegiatanBast);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/pdf")
    @Operation(summary = "Delete PDF files for KegiatanBast")
    public ResponseEntity<?> deletePdf(
            @PathVariable Long id,
            @RequestBody KegiatanBastDeleteFilesRequest filesRequest) {
        try {
            if (filesRequest.getBastPdfIds() != null && !filesRequest.getBastPdfIds().isEmpty()) {
                KegiatanBast kegiatanBast = service.deleteKegiatanBastPdf(id, filesRequest.getBastPdfIds());
                return ResponseEntity.ok(kegiatanBast);
            } else {
                return ResponseEntity.badRequest().body("No PDF IDs provided for deletion");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file PDF: " + e.getMessage());
        }
    }
}