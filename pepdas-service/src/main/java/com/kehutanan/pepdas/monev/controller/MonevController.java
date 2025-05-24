package com.kehutanan.pepdas.monev.controller;

import com.kehutanan.pepdas.monev.dto.MonevDto;
import com.kehutanan.pepdas.monev.model.Monev;
import com.kehutanan.pepdas.monev.service.MonevService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@RestController
@RequestMapping("/api/monev")
@Tag(name = "Monev", description = "API untuk manajemen Monitoring dan Evaluasi")
public class MonevController {

    private final MonevService monevService;
    private final PagedResourcesAssembler<Monev> pagedResourcesAssembler;

    @Autowired
    public MonevController(MonevService monevService, PagedResourcesAssembler<Monev> pagedResourcesAssembler) {
        this.monevService = monevService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua data monev dengan pagination dan filter")
    public ResponseEntity<PagedModel<EntityModel<Monev>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nomor,
            @RequestParam(required = false) String ref_kegiatan,
            @RequestParam(required = false) List<String> pelaksana) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Monev> monevPage;

        if ((nomor == null || nomor.isEmpty()) && (ref_kegiatan == null || ref_kegiatan.isEmpty()) && (pelaksana == null || pelaksana.isEmpty())) {
            // No filters provided
            monevPage = monevService.findAll(pageable);
        } else {
            // Apply filters
            monevPage = monevService.findByFilters(nomor, ref_kegiatan, pelaksana, pageable);
        }

        PagedModel<EntityModel<Monev>> pagedModel = pagedResourcesAssembler.toModel(monevPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data monev berdasarkan ID")
    public ResponseEntity<Monev> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(monevService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Membuat data monev baru")
    public ResponseEntity<Monev> create(@Valid @RequestBody MonevDto monevDto) {
        return ResponseEntity.ok(monevService.create(monevDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data monev")
    public ResponseEntity<Monev> update(
            @PathVariable UUID id,
            @Valid @RequestBody MonevDto monevDto) {
        return ResponseEntity.ok(monevService.update(id, monevDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data monev")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        monevService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
    @PostMapping(value = "/{id}/pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Unggah multiple PDFs Untuk Monev")
    public ResponseEntity<?> uploadPdf(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(monevService.addFilesPdf(id, files));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/pdfs")
    @Operation(summary = "Menghapus PDF-PDF tertentu dari Monev")
    public ResponseEntity<?> deletePdf(
            @PathVariable UUID id,
            @RequestBody List<UUID> pdfIds) throws Exception {
        return ResponseEntity.ok(monevService.deleteFilesPdf(id, pdfIds));
    }

    @GetMapping("/{kegiatanId}/pdf/{pdfId}/download")
    @Operation(summary = "Download PDF Monev")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable UUID kegiatanId, @PathVariable UUID pdfId)
            throws Exception {
        return monevService.downloadFilePdf(kegiatanId, pdfId);

    }


}