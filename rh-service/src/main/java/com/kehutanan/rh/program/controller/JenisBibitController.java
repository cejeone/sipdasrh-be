package com.kehutanan.rh.program.controller;

import com.kehutanan.rh.program.model.JenisBibit;
import com.kehutanan.rh.program.service.JenisBibitService;
import com.kehutanan.rh.program.dto.JenisBibitDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/jenis-bibit")
@Tag(name = "Program -> Jenis Bibit", description = "API untuk manajemen Jenis Bibit")
@RequiredArgsConstructor
public class JenisBibitController {
    
    private final JenisBibitService jenisBibitService;
    PagedResourcesAssembler<JenisBibit> pagedResourcesAssembler;

@GetMapping
@Operation(summary = "Get all jenis bibit with pagination")
public ResponseEntity<PagedModel<EntityModel<JenisBibit>>> findAll(
        @Parameter(description = "Id Program") @RequestParam String programId,
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Filter by nama bibit dan keterangan") @RequestParam(required = false) String search) {

    Pageable pageable = PageRequest.of(page, size);
    PagedModel<EntityModel<JenisBibit>> pagedModel = jenisBibitService.findAll(programId, search,
            pageable, pagedResourcesAssembler);

    return ResponseEntity.ok(pagedModel);
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
    public ResponseEntity<JenisBibit> create(@RequestBody JenisBibitDto dto) {
        return ResponseEntity.ok(jenisBibitService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing jenis bibit")
    public ResponseEntity<JenisBibit> update(@PathVariable UUID id, @RequestBody JenisBibitDto dto) {
        return ResponseEntity.ok(jenisBibitService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete jenis bibit")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        jenisBibitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}