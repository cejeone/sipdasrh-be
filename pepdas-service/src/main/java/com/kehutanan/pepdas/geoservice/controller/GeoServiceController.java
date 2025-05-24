package com.kehutanan.pepdas.geoservice.controller;

import com.kehutanan.pepdas.geoservice.dto.GeoServiceRequestDto;
import com.kehutanan.pepdas.geoservice.dto.GeoServiceMapper;
import com.kehutanan.pepdas.geoservice.dto.GeoServiceResponseDto;
import com.kehutanan.pepdas.geoservice.model.GeoService;
import com.kehutanan.pepdas.geoservice.service.GeoServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@RestController
@RequestMapping("/api/geoservices")
@Tag(name = "GeoService", description = "API untuk manajemen layanan geospasial")
public class GeoServiceController {

    private final GeoServiceService geoServiceService;
    private final PagedResourcesAssembler<GeoService> pagedResourcesAssembler;

    @Autowired
    public GeoServiceController(GeoServiceService geoServiceService,
            PagedResourcesAssembler<GeoService> pagedResourcesAssembler) {
        this.geoServiceService = geoServiceService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua data layanan geospasial dengan pagination dan filter")
    public ResponseEntity<PagedModel<EntityModel<GeoService>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String direktorat,
            @RequestParam(required = false) String service) {

        Pageable pageable = PageRequest.of(page, size);
        Page<GeoService> geoServicePage;

        if ((direktorat == null || direktorat.isEmpty()) &&
                (service == null || service.isEmpty())) {
            // No filters provided
            geoServicePage = geoServiceService.findAll(pageable);
        } else {
            // Apply filters
            geoServicePage = geoServiceService.findByFilters(direktorat, service, pageable);
        }

        PagedModel<EntityModel<GeoService>> pagedModel = pagedResourcesAssembler.toModel(geoServicePage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data layanan geospasial berdasarkan ID")
    public ResponseEntity<GeoService> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(geoServiceService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Membuat layanan geospasial baru")
    public ResponseEntity<GeoService> create(@Valid @RequestBody GeoServiceRequestDto geoServiceDto) {
        return ResponseEntity.ok(geoServiceService.create(geoServiceDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data layanan geospasial")
    public ResponseEntity<GeoService> update(
            @PathVariable UUID id,
            @Valid @RequestBody GeoServiceRequestDto geoServiceDto) {
        return ResponseEntity.ok(geoServiceService.update(id, geoServiceDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus layanan geospasial")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        geoServiceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}