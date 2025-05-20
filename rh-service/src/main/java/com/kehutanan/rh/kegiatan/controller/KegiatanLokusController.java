package com.kehutanan.rh.kegiatan.controller;

import com.kehutanan.rh.dokumen.model.Dokumen;
import com.kehutanan.rh.kegiatan.model.KegiatanLokus;
import com.kehutanan.rh.kegiatan.model.KegiatanLokusShp;
import com.kehutanan.rh.kegiatan.repository.KegiatanRepository;
import com.kehutanan.rh.kegiatan.service.KegiatanLokusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/kegiatan-lokus")
@Tag(name = "Kegiatan Lokus", description = "API endpoints for managing kegiatan lokus")
public class KegiatanLokusController {

    private final KegiatanRepository kegiatanRepository;

    private final KegiatanLokusService kegiatanLokusService;
    private final PagedResourcesAssembler<KegiatanLokus> pagedResourcesAssembler;

    public KegiatanLokusController(KegiatanLokusService kegiatanLokusService,
            PagedResourcesAssembler<KegiatanLokus> pagedResourcesAssembler, KegiatanRepository kegiatanRepository) {
        this.kegiatanLokusService = kegiatanLokusService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.kegiatanRepository = kegiatanRepository;
    }

    @GetMapping
    @Operation(summary = "Get all kegiatan lokus", description = "Returns all kegiatan lokus with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan lokus", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = PagedModel.class)))
    })
    public ResponseEntity<PagedModel<EntityModel<KegiatanLokus>>> getAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search term for provinsi, kabupatenKota, kecamatan, or kelurahanDesa") @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        PagedModel<EntityModel<KegiatanLokus>> pagedModel = kegiatanLokusService.findAll(search, pageable,
                pagedResourcesAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find kegiatan lokus by ID", description = "Returns a single kegiatan lokus by its ID with shapefile links")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan lokus"),
            @ApiResponse(responseCode = "404", description = "Kegiatan lokus not found")
    })
    public ResponseEntity<KegiatanLokus> findById(
            @Parameter(description = "Kegiatan lokus ID") @PathVariable UUID id) {

        KegiatanLokus kegiatanLokus = kegiatanLokusService.findById(id);
        return ResponseEntity.ok(kegiatanLokus);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new kegiatan lokus", description = "Create a new kegiatan lokus with optional shapefile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kegiatan lokus created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<KegiatanLokus> create(
            @RequestPart(value = "Provinsi") @RequestParam String provinsi,
            @RequestPart(value = "Kabupaten/Kota") @RequestParam(required = false) String kabupatenKota,
            @RequestPart(value = "Kecamatan") @RequestParam(required = false) String kecamatan,
            @RequestPart(value = "Kelurahan/Desa") @RequestParam(required = false) String kelurahanDesa,
            @RequestPart(value = "Alamat") @RequestParam(required = false) String alamat,
            @RequestPart(value = "Kegiatan ID", required = true) @RequestParam UUID kegiatan_id,
            @RequestPart(value = "file", required = false) MultipartFile file)
            throws Exception {

        // Create KegiatanLokus object from parameters
        KegiatanLokus kegiatanLokus = new KegiatanLokus();
        kegiatanLokus.setProvinsi(provinsi);
        kegiatanLokus.setKabupatenKota(kabupatenKota);
        kegiatanLokus.setKecamatan(kecamatan);
        kegiatanLokus.setKelurahanDesa(kelurahanDesa);
        kegiatanLokus.setAlamat(alamat);
        kegiatanLokus.setKegiatan(kegiatanRepository.findById(kegiatan_id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with ID: " + kegiatan_id)));

        // The service will need to handle setting the Kegiatan entity based on the ID
        KegiatanLokus created = kegiatanLokusService.create(kegiatanLokus, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update kegiatan lokus fields", description = "Update fields for an existing kegiatan lokus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kegiatan lokus updated successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan lokus not found")
    })
    public ResponseEntity<KegiatanLokus> update(
            @Parameter(description = "Kegiatan lokus ID") @PathVariable UUID id,
            @Parameter(description = "Provinsi") @RequestPart(value = "provinsi", required = true) String provinsi,
            @Parameter(description = "Kabupaten/Kota") @RequestPart(value = "kabupatenKota", required = false) String kabupatenKota,
            @Parameter(description = "Kecamatan") @RequestPart(value = "kecamatan", required = false) String kecamatan,
            @Parameter(description = "Kelurahan/Desa") @RequestPart(value = "kelurahanDesa", required = false) String kelurahanDesa,
            @Parameter(description = "Alamat") @RequestPart(value = "alamat", required = false) String alamat)
            throws Exception {
        return ResponseEntity.ok(
                kegiatanLokusService.update(
                        id,
                        provinsi,
                        kabupatenKota,
                        kecamatan,
                        kelurahanDesa,
                        alamat));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a kegiatan lokus", description = "Delete a kegiatan lokus and all associated shapefiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kegiatan lokus deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan lokus not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Kegiatan lokus ID") @PathVariable UUID id) {

        kegiatanLokusService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{Id}/files/{fileId}/download")
    public ResponseEntity<?> downloadFile(@PathVariable UUID Id, @PathVariable UUID fileId) {
        try {
            return kegiatanLokusService.downloadFile(Id, fileId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengunduh file: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Menambahkan file ke Lokus yang sudah ada")
    public ResponseEntity<?> addFiles(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) throws Exception {
        return ResponseEntity.ok(kegiatanLokusService.addFiles(id, files));
    }

    @DeleteMapping("/{id}/files")
    @Operation(summary = "Menghapus file-file tertentu dari lokus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kegiatan lokus deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan lokus not found")
    })
    public ResponseEntity<?> deleteFiles(
            @PathVariable UUID id,
            @RequestBody List<UUID> fileIds) throws Exception {
        return ResponseEntity.ok(kegiatanLokusService.deleteFiles(id, fileIds));
    }

}