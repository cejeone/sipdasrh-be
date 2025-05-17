package com.kehutanan.rh.monev.controller;

import com.kehutanan.rh.monev.model.Monev;
import com.kehutanan.rh.monev.service.MonevService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/monev")
@Tag(name = "Monev", description = "API untuk manajemen Monitoring dan Evaluasi")
public class MonevController {

    private final MonevService monevService;

    @Autowired
    public MonevController(MonevService monevService) {
        this.monevService = monevService;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua data monev")
    public List<Monev> getAll() {
        return monevService.findAll();
    }

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