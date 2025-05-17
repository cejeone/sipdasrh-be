package com.kehutanan.rh.program.controller;

import com.kehutanan.rh.program.model.JenisBibit;
import com.kehutanan.rh.program.service.JenisBibitService;
import com.kehutanan.rh.program.dto.JenisBibitDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jenis-bibit")
@Tag(name = "Jenis Bibit", description = "API untuk manajemen Jenis Bibit")
@RequiredArgsConstructor
public class JenisBibitController {
    
    private final JenisBibitService jenisBibitService;

    @GetMapping
    @Operation(summary = "Get all jenis bibit")
    public ResponseEntity<List<JenisBibit>> findAll() {
        return ResponseEntity.ok(jenisBibitService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get jenis bibit by ID")
    public ResponseEntity<JenisBibit> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(jenisBibitService.findById(id));
    }

    @GetMapping("/program/{programId}")
    @Operation(summary = "Get jenis bibit by program ID")
    public ResponseEntity<List<JenisBibit>> findByProgramId(@PathVariable UUID programId) {
        return ResponseEntity.ok(jenisBibitService.findByProgramId(programId));
    }

    @PostMapping
    @Operation(summary = "Create new jenis bibit")
    public ResponseEntity<JenisBibit> create(@RequestBody JenisBibitDTO dto) {
        return ResponseEntity.ok(jenisBibitService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing jenis bibit")
    public ResponseEntity<JenisBibit> update(@PathVariable UUID id, @RequestBody JenisBibitDTO dto) {
        return ResponseEntity.ok(jenisBibitService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete jenis bibit")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        jenisBibitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}