package com.kehutanan.rh.kegiatan.controller;

import com.kehutanan.rh.kegiatan.dto.KegiatanPemeliharaanSulamanDto;
import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanSulaman;
import com.kehutanan.rh.kegiatan.repository.KegiatanRepository;
import com.kehutanan.rh.kegiatan.service.KegiatanPemeliharaanSulamanService;
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
@RequestMapping("/api/kegiatan-pemeliharaan-sulaman")
@Tag(name = "Kegiatan Pemeliharaan Sulaman", description = "API endpoints for managing kegiatan pemeliharaan sulaman")
public class KegiatanPemeliharaanSulamanController {

    private final KegiatanPemeliharaanSulamanService kegiatanPemeliharaanSulamanService;
    private final PagedResourcesAssembler<KegiatanPemeliharaanSulaman> pagedResourcesAssembler;
    private final KegiatanRepository kegiatanRepository;

    public KegiatanPemeliharaanSulamanController(KegiatanPemeliharaanSulamanService kegiatanPemeliharaanSulamanService,
            PagedResourcesAssembler<KegiatanPemeliharaanSulaman> pagedResourcesAssembler,
            KegiatanRepository kegiatanRepository) {
        this.kegiatanPemeliharaanSulamanService = kegiatanPemeliharaanSulamanService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.kegiatanRepository = kegiatanRepository;
    }

    @GetMapping
    @Operation(summary = "Get all kegiatan pemeliharaan sulaman", description = "Returns all kegiatan pemeliharaan sulaman with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan pemeliharaan sulaman", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = PagedModel.class)))
    })
    public ResponseEntity<PagedModel<EntityModel<KegiatanPemeliharaanSulaman>>> getAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search term for kategori or keterangan or kondisiTanaman or namaBibit") @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        PagedModel<EntityModel<KegiatanPemeliharaanSulaman>> pagedModel = kegiatanPemeliharaanSulamanService.findAll(search,
                pageable, pagedResourcesAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find kegiatan pemeliharaan sulaman by ID", description = "Returns a single kegiatan pemeliharaan sulaman by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan pemeliharaan sulaman"),
            @ApiResponse(responseCode = "404", description = "Kegiatan pemeliharaan sulaman not found")
    })
    public ResponseEntity<KegiatanPemeliharaanSulaman> findById(
            @Parameter(description = "Kegiatan pemeliharaan sulaman ID") @PathVariable UUID id) {

        KegiatanPemeliharaanSulaman kegiatanPemeliharaanSulaman = kegiatanPemeliharaanSulamanService.findById(id);
        return ResponseEntity.ok(kegiatanPemeliharaanSulaman);
    }

    @PostMapping
    @Operation(summary = "Create a new kegiatan pemeliharaan sulaman", description = "Create a new kegiatan pemeliharaan sulaman with form data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kegiatan pemeliharaan sulaman created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<KegiatanPemeliharaanSulaman> createWithFormData(
            @RequestBody KegiatanPemeliharaanSulamanDto kegiatanPemeliharaanSulamanDto)
            throws Exception {

        // Create KegiatanPemeliharaanSulaman object from parameters
        KegiatanPemeliharaanSulaman kegiatanPemeliharaanSulaman = new KegiatanPemeliharaanSulaman();
        kegiatanPemeliharaanSulaman.setKategori(kegiatanPemeliharaanSulamanDto.getKategori());
        kegiatanPemeliharaanSulaman.setWaktuPenyulaman(kegiatanPemeliharaanSulamanDto.getWaktuPenyulaman());
        kegiatanPemeliharaanSulaman.setNamaBibit(kegiatanPemeliharaanSulamanDto.getNamaBibit());
        kegiatanPemeliharaanSulaman.setJumlahBibit(kegiatanPemeliharaanSulamanDto.getJumlahBibit());
        kegiatanPemeliharaanSulaman.setKondisiTanaman(kegiatanPemeliharaanSulamanDto.getKondisiTanaman());
        kegiatanPemeliharaanSulaman.setJumlahTanamanHidup(kegiatanPemeliharaanSulamanDto.getJumlahTanamanHidup());
        kegiatanPemeliharaanSulaman.setJumlahHokPerempuan(kegiatanPemeliharaanSulamanDto.getJumlahHokPerempuan());
        kegiatanPemeliharaanSulaman.setJumlahHokLakiLaki(kegiatanPemeliharaanSulamanDto.getJumlahHokLakiLaki());
        kegiatanPemeliharaanSulaman.setKeterangan(kegiatanPemeliharaanSulamanDto.getKeterangan());
        kegiatanPemeliharaanSulaman.setStatus(kegiatanPemeliharaanSulamanDto.getStatus());
        kegiatanPemeliharaanSulaman.setKegiatan(kegiatanRepository.findById(kegiatanPemeliharaanSulamanDto.getKegiatanId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Kegiatan not found with ID: " + kegiatanPemeliharaanSulamanDto.getKegiatanId())));

        KegiatanPemeliharaanSulaman created = kegiatanPemeliharaanSulamanService.create(kegiatanPemeliharaanSulaman);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update kegiatan pemeliharaan sulaman", description = "Update an existing kegiatan pemeliharaan sulaman entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kegiatan pemeliharaan sulaman updated successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan pemeliharaan sulaman not found")
    })
    public ResponseEntity<KegiatanPemeliharaanSulaman> update(
            @Parameter(description = "Kegiatan pemeliharaan sulaman ID") @PathVariable UUID id,
            @RequestBody KegiatanPemeliharaanSulamanDto kegiatanPemeliharaanSulamanDto) {

        KegiatanPemeliharaanSulaman updated = kegiatanPemeliharaanSulamanService.update(id, kegiatanPemeliharaanSulamanDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a kegiatan pemeliharaan sulaman", description = "Delete a kegiatan pemeliharaan sulaman entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kegiatan pemeliharaan sulaman deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan pemeliharaan sulaman not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Kegiatan pemeliharaan sulaman ID") @PathVariable UUID id) {

        kegiatanPemeliharaanSulamanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}