package com.kehutanan.pepdas.pemantauandas.controller;

import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasRequestDto;
import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasMapper;
import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasResponseDto;
import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;
import com.kehutanan.pepdas.pemantauandas.service.PemantauanDasService;
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
@RequestMapping("/api/pemantauan-das")
@Tag(name = "Pemantauan DAS", description = "API untuk manajemen data pemantauan DAS")
public class PemantauanDasController {

    private final PemantauanDasService pemantauanDasService;
    private final PagedResourcesAssembler<PemantauanDas> pagedResourcesAssembler;

    @Autowired
    public PemantauanDasController(PemantauanDasService pemantauanDasService,
            PagedResourcesAssembler<PemantauanDas> pagedResourcesAssembler) {
        this.pemantauanDasService = pemantauanDasService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua data pemantauan DAS dengan pagination dan filter")
    public ResponseEntity<PagedModel<EntityModel<PemantauanDas>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String bpdas,
            @RequestParam(required = false) String das) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PemantauanDas> pemantauanDasPage;

        if ((bpdas == null || bpdas.isEmpty()) &&
                (das == null || das.isEmpty())) {
            // No filters provided
            pemantauanDasPage = pemantauanDasService.findAll(pageable);
        } else {
            // Apply filters
            pemantauanDasPage = pemantauanDasService.findByFilters(bpdas, das, pageable);
        }

        PagedModel<EntityModel<PemantauanDas>> pagedModel = pagedResourcesAssembler.toModel(pemantauanDasPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data pemantauan DAS berdasarkan ID")
    public ResponseEntity<PemantauanDas> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(pemantauanDasService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Membuat data pemantauan DAS baru")
    public ResponseEntity<PemantauanDas> create(@Valid @RequestBody PemantauanDasRequestDto pemantauanDasDto) {
        return ResponseEntity.ok(pemantauanDasService.create(pemantauanDasDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data pemantauan DAS")
    public ResponseEntity<PemantauanDas> update(
            @PathVariable UUID id,
            @Valid @RequestBody PemantauanDasRequestDto pemantauanDasDto) {
        return ResponseEntity.ok(pemantauanDasService.update(id, pemantauanDasDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data pemantauan DAS")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        pemantauanDasService.delete(id);
        return ResponseEntity.noContent().build();
    }
}