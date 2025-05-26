package com.kehutanan.pepdas.serahterima.controller;

import com.kehutanan.pepdas.monev.model.Monev;
import com.kehutanan.pepdas.monev.service.MonevService;
import com.kehutanan.pepdas.serahterima.dto.SerahTerimaDto;
import com.kehutanan.pepdas.serahterima.model.SerahTerima;
import com.kehutanan.pepdas.serahterima.service.SerahTerimaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/serah-terima")
@Tag(name = "Serah Terima", description = "API pada RH untuk manajemen Serah Terima")
public class SerahTerimaController {

    @Autowired
    private SerahTerimaService serahTerimaService;
    private final PagedResourcesAssembler<SerahTerima> pagedResourcesAssembler;

    @Autowired
    public SerahTerimaController(SerahTerimaService serahTerimaService,
            PagedResourcesAssembler<SerahTerima> pagedResourcesAssembler) {
        this.serahTerimaService = serahTerimaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua data SerahTerima dengan pagination dan filter")
    public ResponseEntity<PagedModel<EntityModel<SerahTerima>>> getAllSerahTerima(
            @RequestParam(required = false) String nomor,
            @RequestParam(required = false) String kontrak,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SerahTerima> serahTerimaPage;

        if ((nomor == null || nomor.isEmpty()) && (kontrak == null || kontrak.isEmpty())) {
            serahTerimaPage = serahTerimaService.findAll(pageable);
        } else {
            serahTerimaPage = serahTerimaService.findByFilters(nomor, kontrak, pageable);
        }

        PagedModel<EntityModel<SerahTerima>> pagedModel = pagedResourcesAssembler.toModel(serahTerimaPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data SerahTerima berdasarkan ID")
    public ResponseEntity<SerahTerima> getSerahTerimaById(@PathVariable UUID id) {
        return serahTerimaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Membuat data SerahTerima baru")
    public SerahTerima createSerahTerima(@RequestBody SerahTerimaDto serahTerimaDto) {
        return serahTerimaService.save(serahTerimaDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mengupdate data SerahTerima berdasarkan ID")
    public ResponseEntity<SerahTerima> updateSerahTerima(@PathVariable UUID id,
            @RequestBody SerahTerimaDto serahTerimaDto) {
        SerahTerima updatedSerahTerima = serahTerimaService.update(id, serahTerimaDto);
        return ResponseEntity.ok(updatedSerahTerima);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data SerahTerima berdasarkan ID")
    public ResponseEntity<Void> deleteSerahTerima(@PathVariable UUID id) {
        return serahTerimaService.findById(id)
                .map(serahTerima -> {
                    serahTerimaService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    
    
    @PostMapping(value = "/{id}/pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Unggah multiple PDFs Untuk Serah Terima")
    public ResponseEntity<?> uploadPdf(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(serahTerimaService.addFilesPdf(id, files));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/pdfs")
    @Operation(summary = "Menghapus PDF-PDF tertentu dari Serah Terima")
    public ResponseEntity<?> deletePdf(
            @PathVariable UUID id,
            @RequestBody List<UUID> pdfIds) throws Exception {
        return ResponseEntity.ok(serahTerimaService.deleteFilesPdf(id, pdfIds));
    }

    @GetMapping("/{serahTerimaId}/pdf/{pdfId}/download")
    @Operation(summary = "Download PDF Serah Terima")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable UUID serahTerimaId, @PathVariable UUID pdfId)
            throws Exception {
        return serahTerimaService.downloadFilePdf(serahTerimaId, pdfId);

    }
}