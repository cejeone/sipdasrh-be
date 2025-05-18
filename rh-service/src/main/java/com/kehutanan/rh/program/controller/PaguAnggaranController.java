package com.kehutanan.rh.program.controller;

import com.kehutanan.rh.program.model.PaguAnggaran;
import com.kehutanan.rh.program.service.PaguAnggaranService;
import com.kehutanan.rh.program.dto.PaguAnggaranDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pagu-anggaran")
@Tag(name = "Program -> Pagu Anggaran", description = "API untuk manajemen Pagu Anggaran")
@RequiredArgsConstructor
public class PaguAnggaranController {
    
    private final PaguAnggaranService paguAnggaranService;

    @GetMapping
    @Operation(summary = "Get all pagu anggaran")
    public ResponseEntity<List<PaguAnggaran>> findAll() {
        return ResponseEntity.ok(paguAnggaranService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pagu anggaran by ID")
    public ResponseEntity<PaguAnggaran> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(paguAnggaranService.findById(id));
    }

    @GetMapping("/program/{programId}")
    @Operation(summary = "Get pagu anggaran by program ID")
    public ResponseEntity<List<PaguAnggaran>> findByProgramId(@PathVariable UUID programId) {
        return ResponseEntity.ok(paguAnggaranService.findByProgramId(programId));
    }

    @PostMapping
    @Operation(summary = "Create new pagu anggaran")
    public ResponseEntity<PaguAnggaran> create(@RequestBody PaguAnggaranDto dto) {
        return ResponseEntity.ok(paguAnggaranService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing pagu anggaran")
    public ResponseEntity<PaguAnggaran> update(@PathVariable UUID id, @RequestBody PaguAnggaranDto dto) {
        return ResponseEntity.ok(paguAnggaranService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pagu anggaran")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        paguAnggaranService.delete(id);
        return ResponseEntity.noContent().build();
    }
}