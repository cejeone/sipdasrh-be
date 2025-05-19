package com.kehutanan.rh.serahterima.controller;

import com.kehutanan.rh.monev.model.Monev;
import com.kehutanan.rh.monev.service.MonevService;
import com.kehutanan.rh.serahterima.model.SerahTerima;
import com.kehutanan.rh.serahterima.service.SerahTerimaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/serah-terima")
@Tag(name = "Serah Terima", description = "API pada RH untuk manajemen Serah Terima")
public class SerahTerimaController {

    @Autowired
    private SerahTerimaService serahTerimaService;
    private final PagedResourcesAssembler<SerahTerima> pagedResourcesAssembler;

    @Autowired
    public SerahTerimaController(SerahTerimaService serahTerimaService,
            PagedResourcesAssembler<SerahTerima> pagedResourcesAssembler) {
        this.serahTerimaService = serahTerimaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua data SerahTerima dengan pagination dan filter")
    public ResponseEntity<PagedModel<EntityModel<SerahTerima>>> getAllSerahTerima(
            @RequestParam(required = false) String program,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SerahTerima> serahTerimaPage;
        
        if (program != null && !program.isEmpty()) {
            serahTerimaPage = serahTerimaService.findByFilters(program, pageable);
        } else {
            serahTerimaPage = serahTerimaService.findAll(pageable);
        }
        
        PagedModel<EntityModel<SerahTerima>> pagedModel = pagedResourcesAssembler.toModel(serahTerimaPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data SerahTerima berdasarkan ID")
    public ResponseEntity<SerahTerima> getSerahTerimaById(@PathVariable UUID id) {
        return serahTerimaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Membuat data SerahTerima baru")
    public SerahTerima createSerahTerima(@RequestBody SerahTerima serahTerima) {
        return serahTerimaService.save(serahTerima);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mengupdate data SerahTerima berdasarkan ID")
    public ResponseEntity<SerahTerima> updateSerahTerima(@PathVariable UUID id, @RequestBody SerahTerima serahTerima) {
        return serahTerimaService.findById(id)
                .map(existingSerahTerima -> {
                    serahTerima.setId(id);
                    return ResponseEntity.ok(serahTerimaService.save(serahTerima));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data SerahTerima berdasarkan ID")
    public ResponseEntity<Void> deleteSerahTerima(@PathVariable UUID id) {
        return serahTerimaService.findById(id)
                .map(serahTerima -> {
                    serahTerimaService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}