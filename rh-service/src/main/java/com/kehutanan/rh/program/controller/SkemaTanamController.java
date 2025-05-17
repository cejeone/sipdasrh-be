package com.kehutanan.rh.program.controller;

import com.kehutanan.rh.program.model.SkemaTanam;
import com.kehutanan.rh.program.service.SkemaTanamService;
import com.kehutanan.rh.program.dto.SkemaTanamDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skema-tanam")
@Tag(name = "Skema Tanam", description = "API untuk manajemen Skema Tanam")
@RequiredArgsConstructor
public class SkemaTanamController {
    
    private final SkemaTanamService skemaTanamService;

    @GetMapping
    @Operation(summary = "Get all skema tanam")
    public ResponseEntity<List<SkemaTanam>> findAll() {
        return ResponseEntity.ok(skemaTanamService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get skema tanam by ID")
    public ResponseEntity<SkemaTanam> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(skemaTanamService.findById(id));
    }

    @GetMapping("/program/{programId}")
    @Operation(summary = "Get skema tanam by program ID")
    public ResponseEntity<List<SkemaTanam>> findByProgramId(@PathVariable UUID programId) {
        return ResponseEntity.ok(skemaTanamService.findByProgramId(programId));
    }

    @PostMapping
    @Operation(summary = "Create new skema tanam")
    public ResponseEntity<SkemaTanam> create(@RequestBody SkemaTanamDto dto) {
        return ResponseEntity.ok(skemaTanamService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing skema tanam")
    public ResponseEntity<SkemaTanam> update(@PathVariable UUID id, @RequestBody SkemaTanamDto dto) {
        return ResponseEntity.ok(skemaTanamService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete skema tanam")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        skemaTanamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}