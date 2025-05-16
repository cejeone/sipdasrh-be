package com.kehutanan.rh.controller;

import com.kehutanan.rh.model.SkemaTanam;
import com.kehutanan.rh.service.SkemaTanamService;
import com.kehutanan.rh.dto.SkemaTanamDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skema-tanam")
@Tag(name = "Skema Tanam", description = "API untuk manajemen Skema Tanam")
public class SkemaTanamController {

    private final SkemaTanamService skemaTanamService;

    @Autowired
    public SkemaTanamController(SkemaTanamService skemaTanamService) {
        this.skemaTanamService = skemaTanamService;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua skema tanam")
    public List<SkemaTanam> getAll() {
        return skemaTanamService.findAll();
    }

    @GetMapping("/program/{programId}")
    @Operation(summary = "Mendapatkan skema tanam berdasarkan Program ID")
    public List<SkemaTanam> getByProgramId(@PathVariable UUID programId) {
        return skemaTanamService.findByProgramId(programId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan skema tanam berdasarkan ID")
    public ResponseEntity<SkemaTanam> getById(@PathVariable UUID id) {
        return skemaTanamService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Membuat skema tanam baru")
    public ResponseEntity<SkemaTanam> create(@Valid @RequestBody SkemaTanamDto request) {
        return ResponseEntity.ok(skemaTanamService.createFromRequest(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui skema tanam")
    public ResponseEntity<SkemaTanam> update(
            @PathVariable UUID id,
            @Valid @RequestBody SkemaTanamDto request) {
        
        SkemaTanam updated = skemaTanamService.update(id, 
            request.getProgramId(),
            request.getKategori(),
            request.getSkemaBtgHa(),
            request.getTargetLuas(),
            request.getStatus(),
            request.getKeterangan());
        
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus skema tanam")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        skemaTanamService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}