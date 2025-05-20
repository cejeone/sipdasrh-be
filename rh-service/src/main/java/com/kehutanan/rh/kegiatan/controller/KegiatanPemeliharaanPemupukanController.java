package com.kehutanan.rh.kegiatan.controller;

import com.kehutanan.rh.kegiatan.dto.KegiatanPemeliharaanPemupukanDto;
import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanPemupukan;
import com.kehutanan.rh.kegiatan.repository.KegiatanRepository;
import com.kehutanan.rh.kegiatan.service.KegiatanPemeliharaanPemupukanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/kegiatan-pemeliharaan-pemupukan")
@Tag(name = "Kegiatan Pemeliharaan Pemupukan", description = "API endpoints for managing kegiatan pemeliharaan pemupukan")
public class KegiatanPemeliharaanPemupukanController {

    private final KegiatanPemeliharaanPemupukanService kegiatanPemeliharaanPemupukanService;
    private final PagedResourcesAssembler<KegiatanPemeliharaanPemupukan> pagedResourcesAssembler;
    private final KegiatanRepository kegiatanRepository;

    public KegiatanPemeliharaanPemupukanController(KegiatanPemeliharaanPemupukanService kegiatanPemeliharaanPemupukanService,
            PagedResourcesAssembler<KegiatanPemeliharaanPemupukan> pagedResourcesAssembler,
            KegiatanRepository kegiatanRepository) {
        this.kegiatanPemeliharaanPemupukanService = kegiatanPemeliharaanPemupukanService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.kegiatanRepository = kegiatanRepository;
    }

    @GetMapping
    @Operation(summary = "Get all kegiatan pemeliharaan pemupukan", description = "Returns all kegiatan pemeliharaan pemupukan with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan pemeliharaan pemupukan", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = PagedModel.class)))
    })
    public ResponseEntity<PagedModel<EntityModel<KegiatanPemeliharaanPemupukan>>> getAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search term for jenis or keterangan") @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        PagedModel<EntityModel<KegiatanPemeliharaanPemupukan>> pagedModel = kegiatanPemeliharaanPemupukanService.findAll(search,
                pageable, pagedResourcesAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find kegiatan pemeliharaan pemupukan by ID", description = "Returns a single kegiatan pemeliharaan pemupukan by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan pemeliharaan pemupukan"),
            @ApiResponse(responseCode = "404", description = "Kegiatan pemeliharaan pemupukan not found")
    })
    public ResponseEntity<KegiatanPemeliharaanPemupukan> findById(
            @Parameter(description = "Kegiatan pemeliharaan pemupukan ID") @PathVariable UUID id) {

        KegiatanPemeliharaanPemupukan kegiatanPemeliharaanPemupukan = kegiatanPemeliharaanPemupukanService.findById(id);
        return ResponseEntity.ok(kegiatanPemeliharaanPemupukan);
    }

    @PostMapping
    @Operation(summary = "Create a new kegiatan pemeliharaan pemupukan", description = "Create a new kegiatan pemeliharaan pemupukan with form data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kegiatan pemeliharaan pemupukan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<KegiatanPemeliharaanPemupukan> createWithFormData(
            @RequestBody KegiatanPemeliharaanPemupukanDto kegiatanPemeliharaanPemupukanDto)
            throws Exception {

        // Create KegiatanPemeliharaanPemupukan object from parameters
        KegiatanPemeliharaanPemupukan kegiatanPemeliharaanPemupukan = new KegiatanPemeliharaanPemupukan();
        kegiatanPemeliharaanPemupukan.setJenis(kegiatanPemeliharaanPemupukanDto.getJenis());
        kegiatanPemeliharaanPemupukan.setWaktuPemupukan(kegiatanPemeliharaanPemupukanDto.getWaktuPemupukan());
        kegiatanPemeliharaanPemupukan.setJumlahPupuk(kegiatanPemeliharaanPemupukanDto.getJumlahPupuk());
        kegiatanPemeliharaanPemupukan.setSatuan(kegiatanPemeliharaanPemupukanDto.getSatuan());
        kegiatanPemeliharaanPemupukan.setJumlahHokPerempuan(kegiatanPemeliharaanPemupukanDto.getJumlahHokPerempuan());
        kegiatanPemeliharaanPemupukan.setJumlahHokLakiLaki(kegiatanPemeliharaanPemupukanDto.getJumlahHokLakiLaki());
        kegiatanPemeliharaanPemupukan.setKeterangan(kegiatanPemeliharaanPemupukanDto.getKeterangan());
        kegiatanPemeliharaanPemupukan.setStatus(kegiatanPemeliharaanPemupukanDto.getStatus());
        kegiatanPemeliharaanPemupukan.setKegiatan(kegiatanRepository.findById(kegiatanPemeliharaanPemupukanDto.getKegiatanId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Kegiatan not found with ID: " + kegiatanPemeliharaanPemupukanDto.getKegiatanId())));

        KegiatanPemeliharaanPemupukan created = kegiatanPemeliharaanPemupukanService.create(kegiatanPemeliharaanPemupukan);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update kegiatan pemeliharaan pemupukan", description = "Update an existing kegiatan pemeliharaan pemupukan entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kegiatan pemeliharaan pemupukan updated successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan pemeliharaan pemupukan not found")
    })
    public ResponseEntity<KegiatanPemeliharaanPemupukan> update(
            @Parameter(description = "Kegiatan pemeliharaan pemupukan ID") @PathVariable UUID id,
            @RequestBody KegiatanPemeliharaanPemupukanDto kegiatanPemeliharaanPemupukanDto) {

        KegiatanPemeliharaanPemupukan updated = kegiatanPemeliharaanPemupukanService.update(id, kegiatanPemeliharaanPemupukanDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a kegiatan pemeliharaan pemupukan", description = "Delete a kegiatan pemeliharaan pemupukan entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kegiatan pemeliharaan pemupukan deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan pemeliharaan pemupukan not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Kegiatan pemeliharaan pemupukan ID") @PathVariable UUID id) {

        kegiatanPemeliharaanPemupukanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}