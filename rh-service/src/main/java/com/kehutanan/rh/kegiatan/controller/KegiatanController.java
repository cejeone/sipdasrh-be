package com.kehutanan.rh.kegiatan.controller;

import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.rh.kegiatan.service.KegiatanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/kegiatan")
@Tag(name = "Kegiatan", description = "API untuk manajemen Kegiatan")
@RequiredArgsConstructor
public class KegiatanController {

    private final KegiatanService kegiatanService;
    private final PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary = "Mendapatkan semua kegiatan dengan pagination")
    public ResponseEntity<PagedModel<EntityModel<Kegiatan>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Kegiatan> kegiatanPage = kegiatanService.findAll(pageable);
        PagedModel<EntityModel<Kegiatan>> pagedModel = pagedResourcesAssembler.toModel(kegiatanPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data kegiatan berdasarkan ID")
    public ResponseEntity<Kegiatan> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(kegiatanService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Membuat data kegiatan baru")
    public ResponseEntity<Kegiatan> create(@Valid @RequestBody Kegiatan kegiatan) {
        return ResponseEntity.ok(kegiatanService.create(kegiatan));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data kegiatan")
    public ResponseEntity<Kegiatan> update(
            @PathVariable UUID id,
            @Valid @RequestBody Kegiatan kegiatan) {
        return ResponseEntity.ok(kegiatanService.update(id, kegiatan));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data kegiatan")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        kegiatanService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/{id}/rancangan-teknis/fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple foto rancangan teknis untuk sebuah Kegiatan")
    public ResponseEntity<?> uploadRancanganTeknisFotos(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KegiatanRancanganTeknisFoto> uploadedFotos = kegiatanService.uploadKegiatanRancanganTeknisFotos(id, files);
            return ResponseEntity.ok(uploadedFotos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload foto rancangan teknis: " + e.getMessage());
        }
    }
}