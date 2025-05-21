package com.kehutanan.rh.kegiatan.controller;

import com.kehutanan.rh.kegiatan.dto.KegiatanMonevKriteriaEvaluasiDto;
import com.kehutanan.rh.kegiatan.model.KegiatanMonev;
import com.kehutanan.rh.kegiatan.model.KegiatanMonevKriteriaEvaluasi;
import com.kehutanan.rh.kegiatan.repository.KegiatanMonevRepository;
import com.kehutanan.rh.kegiatan.service.KegiatanMonevKriteriaEvaluasiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/kegiatan-monev-kriteria-evaluasi")
@Tag(name = "Kegiatan Monev Kriteria Evaluasi", description = "API for managing kriteria evaluasi for monitoring and evaluation")
@RequiredArgsConstructor
public class KegiatanMonevKriteriaEvaluasiController {

    private final KegiatanMonevKriteriaEvaluasiService kegiatanMonevKriteriaEvaluasiService;
    private final PagedResourcesAssembler<KegiatanMonevKriteriaEvaluasi> pagedResourcesAssembler;
    private final KegiatanMonevRepository kegiatanMonevRepository;

    @GetMapping
    @Operation(summary = "Get all kriteria evaluasi", description = "Returns all kriteria evaluasi with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kriteria evaluasi", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = PagedModel.class)))
    })
    public ResponseEntity<PagedModel<EntityModel<KegiatanMonevKriteriaEvaluasi>>> getAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search term for aktivitas or catatan") @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        PagedModel<EntityModel<KegiatanMonevKriteriaEvaluasi>> pagedModel = kegiatanMonevKriteriaEvaluasiService.findAll(search,
                pageable, pagedResourcesAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find kriteria evaluasi by ID", description = "Returns a single kriteria evaluasi by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kriteria evaluasi"),
            @ApiResponse(responseCode = "404", description = "Kriteria evaluasi not found")
    })
    public ResponseEntity<KegiatanMonevKriteriaEvaluasi> findById(
            @Parameter(description = "Kriteria evaluasi ID") @PathVariable UUID id) {

        KegiatanMonevKriteriaEvaluasi kriteriaEvaluasi = kegiatanMonevKriteriaEvaluasiService.findById(id);
        return ResponseEntity.ok(kriteriaEvaluasi);
    }

    @PostMapping
    @Operation(summary = "Create a new kriteria evaluasi", description = "Create a new kriteria evaluasi with form data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kriteria evaluasi created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<KegiatanMonevKriteriaEvaluasi> createWithFormData(
            @RequestBody KegiatanMonevKriteriaEvaluasiDto kriteriaEvaluasiDto)
            throws Exception {

        // Create KegiatanMonevKriteriaEvaluasi object from parameters
        KegiatanMonevKriteriaEvaluasi kriteriaEvaluasi = new KegiatanMonevKriteriaEvaluasi();
        kriteriaEvaluasi.setAktivitas(kriteriaEvaluasiDto.getAktivitas());
        kriteriaEvaluasi.setTarget(kriteriaEvaluasiDto.getTarget());
        kriteriaEvaluasi.setRealisasi(kriteriaEvaluasiDto.getRealisasi());
        kriteriaEvaluasi.setCatatan(kriteriaEvaluasiDto.getCatatan());
        kriteriaEvaluasi.setKegiatanMonev(kegiatanMonevRepository.findById(kriteriaEvaluasiDto.getKegiatanMonevId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "KegiatanMonev not found with ID: " + kriteriaEvaluasiDto.getKegiatanMonevId())));

        KegiatanMonevKriteriaEvaluasi created = kegiatanMonevKriteriaEvaluasiService.create(kriteriaEvaluasi);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update kriteria evaluasi", description = "Update an existing kriteria evaluasi entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kriteria evaluasi updated successfully"),
            @ApiResponse(responseCode = "404", description = "Kriteria evaluasi not found")
    })
    public ResponseEntity<KegiatanMonevKriteriaEvaluasi> update(
            @Parameter(description = "Kriteria evaluasi ID") @PathVariable UUID id,
            @RequestBody KegiatanMonevKriteriaEvaluasiDto kriteriaEvaluasiDto) {

        KegiatanMonevKriteriaEvaluasi updated = kegiatanMonevKriteriaEvaluasiService.update(id, kriteriaEvaluasiDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a kriteria evaluasi", description = "Delete a kriteria evaluasi entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kriteria evaluasi deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Kriteria evaluasi not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Kriteria evaluasi ID") @PathVariable UUID id) {

        kegiatanMonevKriteriaEvaluasiService.delete(id);
        return ResponseEntity.noContent().build();
    }
}