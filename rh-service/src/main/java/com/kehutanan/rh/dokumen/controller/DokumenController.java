package com.kehutanan.rh.dokumen.controller;

import com.kehutanan.rh.dokumen.model.Dokumen;
import com.kehutanan.rh.dokumen.service.DokumenService;
import com.kehutanan.rh.program.model.Program;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

import javax.swing.text.html.parser.Entity;
import com.kehutanan.rh.dokumen.dto.DokumenDto;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/dokumen")
@Tag(name = "Dokumen", description = "API untuk manajemen dokumen")
public class DokumenController {

    private final DokumenService dokumenService;
    private final PagedResourcesAssembler<Dokumen> pagedResourcesAssembler;

    @Autowired
    public DokumenController(DokumenService dokumenService,
            PagedResourcesAssembler<Dokumen> pagedResourcesAssembler) {
        this.dokumenService = dokumenService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua dokumen dengan pagination dan filter")
    public ResponseEntity<PagedModel<EntityModel<Dokumen>>> getAll(
            @RequestParam(required = false) String namaDokumen,
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Dokumen> dokumenPage;
        
        if ((namaDokumen != null && !namaDokumen.isEmpty()) || (bpdas != null && !bpdas.isEmpty())) {
            dokumenPage = dokumenService.findByFilters(namaDokumen, bpdas, pageable);
        } else {
            dokumenPage = dokumenService.findAll(pageable);
        }
        
        PagedModel<EntityModel<Dokumen>> pagedModel = pagedResourcesAssembler.toModel(dokumenPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan dokumen berdasarkan ID")
    public ResponseEntity<Dokumen> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(dokumenService.findById(id));
    }

    // @GetMapping("/{dokumenId}/files/{fileId}/url")
    // @Operation(summary = "Mendapatkan URL file dokumen")
    // public ResponseEntity<String> getFileUrl(
    //         @PathVariable UUID dokumenId,
    //         @PathVariable UUID fileId) throws Exception {
    //     return ResponseEntity.ok(dokumenService.getFileUrl(dokumenId, fileId));
    // }

    @GetMapping("/{dokumenId}/files/{fileId}/download")
    public ResponseEntity<?> downloadFile(@PathVariable UUID dokumenId, @PathVariable UUID fileId) {
        try {
            return dokumenService.downloadFile(dokumenId, fileId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengunduh file: " + e.getMessage());
        }
    }

    // @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // @Operation(summary = "Membuat dokumen baru dengan multiple file")
    // public ResponseEntity<Dokumen> create(
    // @RequestParam("files") List<MultipartFile> files,
    // @RequestParam("tipe") String tipe,
    // @RequestParam("namaDokumen") String namaDokumen,
    // @RequestParam("status") String status,
    // @RequestParam(value = "keterangan", required = false) String keterangan
    // ) throws Exception {
    // return ResponseEntity.ok(
    // dokumenService.create(files, tipe, namaDokumen, status, keterangan)
    // );
    // }

    // Controller implementation
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Membuat dokumen baru dengan multiple file")
    public ResponseEntity<Dokumen> create(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("tipe") String tipe,
            @RequestPart("namaDokumen") String namaDokumen,
            @RequestPart("status") String status,
            @RequestPart(value = "keterangan", required = false) String keterangan) throws Exception {

        return ResponseEntity.ok(
                dokumenService.create(
                        files,
                        tipe,
                        namaDokumen,
                        status,
                        keterangan));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Memperbarui field di tabel Dokumen")
    public ResponseEntity<Dokumen> update(
            @PathVariable UUID id,
            @RequestPart(value = "tipe", required = true) String tipe,
            @RequestPart(value = "namaDokumen", required = true) String namaDokumen,
            @RequestPart(value = "status", required = true) String status,
            @RequestPart(value = "keterangan", required = false) String keterangan) throws Exception {
        return ResponseEntity.ok(
                dokumenService.update(
                        id,
                        tipe,
                        namaDokumen,
                        status,
                        keterangan));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus dokumen beserta file-filenya")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws Exception {
        dokumenService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Menambahkan file ke dokumen yang sudah ada")
    public ResponseEntity<Dokumen> addFiles(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) throws Exception {
        return ResponseEntity.ok(dokumenService.addFiles(id, files));
    }

    @DeleteMapping("/{id}/files")
    @Operation(summary = "Menghapus file-file tertentu dari dokumen")
    public ResponseEntity<Dokumen> deleteFiles(
            @PathVariable UUID id,
            @RequestBody List<UUID> fileIds) throws Exception {
        return ResponseEntity.ok(dokumenService.deleteFiles(id, fileIds));
    }

}