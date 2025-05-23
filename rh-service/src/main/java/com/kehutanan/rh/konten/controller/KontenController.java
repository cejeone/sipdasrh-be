package com.kehutanan.rh.konten.controller;

import com.kehutanan.rh.bimtek.model.BimtekFoto;
import com.kehutanan.rh.konten.dto.KontenDto;
import com.kehutanan.rh.konten.model.Konten;
import com.kehutanan.rh.konten.model.KontenGambar;
import com.kehutanan.rh.konten.model.KontenGambarUtama;
import com.kehutanan.rh.konten.model.Konten;
import com.kehutanan.rh.konten.service.KontenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

@RestController
@RequestMapping("/api/konten")
@Tag(name = "Konten", description = "API untuk manajemen Konten")
@RequiredArgsConstructor
public class KontenController {

    private final KontenService kontenService;
    private final PagedResourcesAssembler<Konten> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary = "Mendapatkan semua konten dengan pagination dan filter")
    public ResponseEntity<PagedModel<EntityModel<Konten>>> getAll(
            @RequestParam(required = false) String judul,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Konten> kontenPage;
        
        if (judul != null && !judul.isEmpty()) {
            kontenPage = kontenService.findByFilters(judul, pageable);
        } else {
            kontenPage = kontenService.findAll(pageable);
        }
        
        PagedModel<EntityModel<Konten>> pagedModel = pagedResourcesAssembler.toModel(kontenPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data konten berdasarkan ID")
    public ResponseEntity<Konten> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(kontenService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Membuat data konten baru")
    public ResponseEntity<Konten> create(@Valid @RequestBody KontenDto kontenDto) {
        return ResponseEntity.ok(kontenService.create(kontenDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data konten")
    public ResponseEntity<Konten> update(
            @PathVariable UUID id,
            @Valid @RequestBody KontenDto kontenDto) {
        return ResponseEntity.ok(kontenService.update(id, kontenDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data konten")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        kontenService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/Gambars", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple photos for a Konten")
    public ResponseEntity<?> uploadGambars(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KontenGambar> uploadedGambars = kontenService.uploadGambars(id, files);
            return ResponseEntity.ok(uploadedGambars);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload gambar: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/Gambars")
    @Operation(summary = "Menghapus Gambar-gambar tertentu dari Konten")
    public ResponseEntity<Konten> deleteGambars(
            @PathVariable UUID id,
            @RequestBody List<UUID> gambarIds) throws Exception {
        return ResponseEntity.ok(kontenService.deleteGambars(id, gambarIds));
    }

    @GetMapping("/{kontenId}/gambar/{gambarId}/view")
    @Operation(summary = "Menampilkan gambar Konten")
    public ResponseEntity<byte[]> viewGambar(
            @PathVariable UUID kontenId, @PathVariable UUID gambarId) {

        try {
            // Dapatkan gambar dari service
            byte[] imageData = kontenService.viewGambar(kontenId, gambarId);

            // Dapatkan informasi gambar untuk contentType
            KontenGambar gambar = kontenService.getGambarById(gambarId);

            // Buat response dengan header Content-Type yang sesuai
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(gambar.getContentType()))
                    .body(imageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping(value = "/{id}/GambarUtamas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple Gambar Utama for a Konten")
    public ResponseEntity<?> uploadGambarUtamas(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KontenGambarUtama> uploadedGambars = kontenService.uploadGambarUtamas(id, files);
            return ResponseEntity.ok(uploadedGambars);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload gambar utama: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/GambarUtamas")
    @Operation(summary = "Menghapus Gambar Utama - Gambar Utama tertentu dari Konten")
    public ResponseEntity<Konten> deleteFiles(
            @PathVariable UUID id,
            @RequestBody List<UUID> gambarUtamaIds) throws Exception {
        return ResponseEntity.ok(kontenService.deleteGambarUtamas(id, gambarUtamaIds));
    }

    @GetMapping("/{kontenId}/gambarUtama/{gambarUtamaId}/view")
    @Operation(summary = "Menampilkan gambar utama Konten")
    public ResponseEntity<byte[]> viewGambarUtama(
            @PathVariable UUID kontenId, @PathVariable UUID gambarUtamaId) {

        try {
            // Dapatkan gambar dari service
            byte[] imageData = kontenService.viewGambarUtama(kontenId, gambarUtamaId);

            // Dapatkan informasi gambar untuk contentType
            KontenGambarUtama gambarUtama = kontenService.getGambarUtamaById(gambarUtamaId);

            // Buat response dengan header Content-Type yang sesuai
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(gambarUtama.getContentType()))
                    .body(imageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}