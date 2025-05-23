package com.kehutanan.pepdas.kegiatan.controller;

import com.kehutanan.pepdas.kegiatan.dto.KegiatanFungsiKawasanDto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanRepository;
import com.kehutanan.pepdas.kegiatan.service.KegiatanFungsiKawasanService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/kegiatan-fungsi-kawasan")
@Tag(name = "Kegiatan Fungsi Kawasan", description = "API endpoints for managing kegiatan fungsi kawasan")
public class KegiatanFungsiKawasanController {

    private final KegiatanFungsiKawasanService kegiatanFungsiKawasanService;
    private final PagedResourcesAssembler<KegiatanFungsiKawasan> pagedResourcesAssembler;

    private final KegiatanRepository kegiatanRepository;

    public KegiatanFungsiKawasanController(KegiatanFungsiKawasanService kegiatanFungsiKawasanService,
            PagedResourcesAssembler<KegiatanFungsiKawasan> pagedResourcesAssembler,
            KegiatanRepository kegiatanRepository) {
        this.kegiatanFungsiKawasanService = kegiatanFungsiKawasanService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.kegiatanRepository = kegiatanRepository;
    }

@GetMapping
@Operation(summary = "Get all kegiatan fungsi kawasan", description = "Returns all kegiatan fungsi kawasan with pagination")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan fungsi kawasan", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = PagedModel.class)))
})
public ResponseEntity<PagedModel<EntityModel<KegiatanFungsiKawasan>>> getAll(
        @Parameter(description = "Id Kegiatan") @RequestParam(required = false) String KegiatanId,
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Search term for fungsiKawasan or keterangan") @RequestParam(required = false) String search) {

    Pageable pageable = PageRequest.of(page, size);
    PagedModel<EntityModel<KegiatanFungsiKawasan>> pagedModel = kegiatanFungsiKawasanService.findAll(KegiatanId, search,
            pageable, pagedResourcesAssembler);

    return ResponseEntity.ok(pagedModel);
}

    @GetMapping("/{id}")
    @Operation(summary = "Find kegiatan fungsi kawasan by ID", description = "Returns a single kegiatan fungsi kawasan by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan fungsi kawasan"),
            @ApiResponse(responseCode = "404", description = "Kegiatan fungsi kawasan not found")
    })
    public ResponseEntity<KegiatanFungsiKawasan> findById(
            @Parameter(description = "Kegiatan fungsi kawasan ID") @PathVariable UUID id) {

        KegiatanFungsiKawasan kegiatanFungsiKawasan = kegiatanFungsiKawasanService.findById(id);
        return ResponseEntity.ok(kegiatanFungsiKawasan);
    }

    @PostMapping
    @Operation(summary = "Create a new kegiatan fungsi kawasan", description = "Create a new kegiatan fungsi kawasan with form data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kegiatan fungsi kawasan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<KegiatanFungsiKawasan> createWithFormData(
            @RequestBody KegiatanFungsiKawasanDto kegiatanFungsiKawasanDto)
            throws Exception {

        // Create KegiatanFungsiKawasan object from parameters
        KegiatanFungsiKawasan kegiatanFungsiKawasan = new KegiatanFungsiKawasan();
        kegiatanFungsiKawasan.setFungsiKawasan(kegiatanFungsiKawasanDto.getFungsiKawasan());
        kegiatanFungsiKawasan.setTargetLuasHa(kegiatanFungsiKawasanDto.getTargetLuasHa());
        kegiatanFungsiKawasan.setRealisasiLuas(kegiatanFungsiKawasanDto.getRealisasiLuas());
        kegiatanFungsiKawasan.setTahun(kegiatanFungsiKawasanDto.getTahun());
        kegiatanFungsiKawasan.setKeterangan(kegiatanFungsiKawasanDto.getKeterangan());
        kegiatanFungsiKawasan.setStatus(kegiatanFungsiKawasanDto.getStatus());
        kegiatanFungsiKawasan.setKegiatan(kegiatanRepository.findById(kegiatanFungsiKawasanDto.getKegiatanId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Kegiatan not found with ID: " + kegiatanFungsiKawasanDto.getKegiatanId())));

        // The service will need to handle setting the Kegiatan entity based on the ID
        // and processing the file if needed
        KegiatanFungsiKawasan created = kegiatanFungsiKawasanService.create(kegiatanFungsiKawasan);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update kegiatan fungsi kawasan", description = "Update an existing kegiatan fungsi kawasan entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kegiatan fungsi kawasan updated successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan fungsi kawasan not found")
    })
    public ResponseEntity<KegiatanFungsiKawasan> update(
            @Parameter(description = "Kegiatan fungsi kawasan ID") @PathVariable UUID id,
            @RequestBody KegiatanFungsiKawasanDto kegiatanFungsiKawasanDto) {

        KegiatanFungsiKawasan updated = kegiatanFungsiKawasanService.update(id, kegiatanFungsiKawasanDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a kegiatan fungsi kawasan", description = "Delete a kegiatan fungsi kawasan entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kegiatan fungsi kawasan deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan fungsi kawasan not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Kegiatan fungsi kawasan ID") @PathVariable UUID id) {

        kegiatanFungsiKawasanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}