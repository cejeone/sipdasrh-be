package com.kehutanan.rh.dokumen.controller;

import com.kehutanan.rh.dokumen.model.Dokumen;
import com.kehutanan.rh.dokumen.service.DokumenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/dokumen")
@Tag(name = "Dokumen", description = "API untuk manajemen dokumen")
public class DokumenController {

    private final DokumenService dokumenService;

    @Autowired
    public DokumenController(DokumenService dokumenService) {
        this.dokumenService = dokumenService;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua dokumen dengan pagination")
    public ResponseEntity<Page<Dokumen>> getAll(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(dokumenService.findAll(search, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan dokumen berdasarkan ID")
    public ResponseEntity<Dokumen> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dokumenService.findById(id));
    }

    @GetMapping("/{dokumenId}/files/{fileId}/url")
    @Operation(summary = "Mendapatkan URL file dokumen")
    public ResponseEntity<String> getFileUrl(
            @PathVariable Long dokumenId,
            @PathVariable Long fileId) throws Exception {
        return ResponseEntity.ok(dokumenService.getFileUrl(dokumenId, fileId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Membuat dokumen baru dengan multiple file")
    public ResponseEntity<Dokumen> create(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("tipe") String tipe,
            @RequestParam("namaDokumen") String namaDokumen,
            @RequestParam("status") String status,
            @RequestParam(value = "keterangan", required = false) String keterangan
    ) throws Exception {
        return ResponseEntity.ok(
            dokumenService.create(files, tipe, namaDokumen, status, keterangan)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui dokumen dan file-filenya")
    public ResponseEntity<Dokumen> update(
            @PathVariable Long id,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds,
            @RequestParam("tipe") String tipe,
            @RequestParam("namaDokumen") String namaDokumen,
            @RequestParam("status") String status,
            @RequestParam(value = "keterangan", required = false) String keterangan
    ) throws Exception {
        return ResponseEntity.ok(
            dokumenService.update(id, files, deleteFileIds, tipe, namaDokumen, status, keterangan)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus dokumen beserta file-filenya")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        dokumenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}