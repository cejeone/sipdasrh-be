package com.kehutanan.rh.dokumen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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

import com.kehutanan.rh.dokumen.dto.DokumenDTO;
import com.kehutanan.rh.dokumen.model.Dokumen;
import com.kehutanan.rh.dokumen.service.DokumenService;
import com.kehutanan.rh.master.model.Lov;
import com.kehutanan.rh.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/dokumen")
public class DokumenController {

    private final DokumenService service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Dokumen> pagedResourcesAssembler;

    @Autowired
    public DokumenController(
            DokumenService service,
            LovService lovService,
            PagedResourcesAssembler<Dokumen> pagedResourcesAssembler) {
        this.service = service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Dokumen>>> getAllDokumen(
            @RequestParam(required = false) String namaDokumen,
            @RequestParam(required = false) Long tipeId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dokumen> dokumenPage;

        // Check if any filter is provided
        if ((namaDokumen != null && !namaDokumen.isEmpty()) ||
                (tipeId != null) || (statusId != null)) {
            dokumenPage = service.findByFilters(namaDokumen, tipeId, statusId, pageable);
        } else {
            dokumenPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Dokumen>> pagedModel = pagedResourcesAssembler.toModel(dokumenPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DokumenDTO> getDokumenById(@PathVariable Long id) {
        try {
            DokumenDTO dokumenDto = service.findDTOById(id);
            return ResponseEntity.ok(dokumenDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Dokumen> createDokumen(
            @RequestPart String namaDokumen,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String tipeId,
            @RequestPart(required = false) String statusId) {

        try {
            Dokumen newDokumen = new Dokumen();
            newDokumen.setNamaDokumen(namaDokumen);
             newDokumen.setUkuranDokumen(Double.valueOf(0.0));
            
            newDokumen.setKeterangan(keterangan);

            // Set relations if IDs are provided
            if (tipeId != null && !tipeId.isEmpty()) {
                Long tipeIdLong = Long.parseLong(tipeId);
                Lov tipe = lovService.findById(tipeIdLong);
                newDokumen.setTipe(tipe);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newDokumen.setStatus(status);
            }

            Dokumen savedDokumen = service.save(newDokumen);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDokumen);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Dokumen> updateDokumen(
            @PathVariable Long id,
            @RequestPart String namaDokumen,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String tipeId,
            @RequestPart(required = false) String statusId) {

        try {
            Dokumen existingDokumen = service.findById(id);

            existingDokumen.setNamaDokumen(namaDokumen);
            

            existingDokumen.setKeterangan(keterangan);

            // Update relations if IDs are provided
            if (tipeId != null && !tipeId.isEmpty()) {
                Long tipeIdLong = Long.parseLong(tipeId);
                Lov tipe = lovService.findById(tipeIdLong);
                existingDokumen.setTipe(tipe);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingDokumen.setStatus(status);
            }

            Dokumen updatedDokumen = service.update(id, existingDokumen);
            return ResponseEntity.ok(updatedDokumen);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDokumen(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload files for Dokumen")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            if (files != null) {
                service.uploadDokumenFiles(id, files);
            }

            Dokumen dokumen = service.findById(id);
            return ResponseEntity.ok(dokumen);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete files for Dokumen")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody List<String> fileIds) {
        try {
            if (fileIds != null && !fileIds.isEmpty()) {
                service.deleteDokumenFiles(id, fileIds);
            }

            // Fetch and return the updated Dokumen entity
            Dokumen dokumen = service.findById(id);
            return ResponseEntity.ok(dokumen);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }
}