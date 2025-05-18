package com.kehutanan.rh.bimtek.controller;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekFoto;
import com.kehutanan.rh.bimtek.service.BimtekService;
import com.kehutanan.rh.dokumen.model.Dokumen;
import com.kehutanan.rh.dokumen.service.DokumenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bimtek")
@Tag(name = "Bimtek", description = "API untuk manajemen Bimbingan Teknis")
@RequiredArgsConstructor
public class BimtekController {

    private final BimtekService bimtekService;
    private final PagedResourcesAssembler<Bimtek> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary = "Mendapatkan semua bimtek dengan pagination")
    public ResponseEntity<PagedModel<EntityModel<Bimtek>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bimtek> bimtekPage = bimtekService.findAll(pageable);
        PagedModel<EntityModel<Bimtek>> pagedModel = pagedResourcesAssembler.toModel(bimtekPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data bimtek berdasarkan ID")
    public ResponseEntity<Bimtek> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(bimtekService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Membuat data bimtek baru")
    public ResponseEntity<Bimtek> create(@Valid @RequestBody Bimtek bimtek) {
        return ResponseEntity.ok(bimtekService.create(bimtek));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data bimtek")
    public ResponseEntity<Bimtek> update(
            @PathVariable UUID id,
            @Valid @RequestBody Bimtek bimtek) {
        return ResponseEntity.ok(bimtekService.update(id, bimtek));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data bimtek")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        bimtekService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple photos for a Bimtek")
    public ResponseEntity<?> uploadFotos(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<BimtekFoto> uploadedFotos = bimtekService.uploadFotos(id, files);
            return ResponseEntity.ok(uploadedFotos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/files")
    @Operation(summary = "Menghapus foto-foto tertentu dari Bimtek")
    public ResponseEntity<Bimtek> deleteFiles(
            @PathVariable UUID id,
            @RequestBody List<UUID> fotoIds) throws Exception {
        return ResponseEntity.ok(bimtekService.deleteFotos(id, fotoIds));
    }
}
