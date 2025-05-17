package com.kehutanan.rh.monev.controller;

import com.kehutanan.rh.monev.model.Monev;
import com.kehutanan.rh.monev.service.MonevService;
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

@RestController
@RequestMapping("/api/monev")
@Tag(name = "Monev", description = "API untuk manajemen Monitoring dan Evaluasi")
public class MonevController {

    private final MonevService monevService;
    private final PagedResourcesAssembler<Monev> pagedResourcesAssembler;

    @Autowired
    public MonevController(MonevService monevService,PagedResourcesAssembler<Monev> pagedResourcesAssembler) {
        this.monevService = monevService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua data monev dengan pagination")
    public ResponseEntity<PagedModel<EntityModel<Monev>>> getAll(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Monev> monevPage = monevService.findAll(pageable);
        PagedModel<EntityModel<Monev>> pagedModel = pagedResourcesAssembler.toModel(monevPage);
        
        return ResponseEntity.ok(pagedModel);
    }

    // @GetMapping
    // @Operation(summary = "Mendapatkan semua data monev")
    // public List<Monev> getAll() {
    //     return monevService.findAll();
    // }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data monev berdasarkan ID")
    public ResponseEntity<Monev> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(monevService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Membuat data monev baru")
    public ResponseEntity<Monev> create(@Valid @RequestBody Monev monev) {
        return ResponseEntity.ok(monevService.create(monev));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data monev")
    public ResponseEntity<Monev> update(
            @PathVariable UUID id,
            @Valid @RequestBody Monev monev) {
        return ResponseEntity.ok(monevService.update(id, monev));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data monev")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        monevService.delete(id);
        return ResponseEntity.noContent().build();
    }
}