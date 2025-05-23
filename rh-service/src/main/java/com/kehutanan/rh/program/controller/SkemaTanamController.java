package com.kehutanan.rh.program.controller;

import com.kehutanan.rh.program.model.SkemaTanam;
import com.kehutanan.rh.program.service.SkemaTanamService;
import com.kehutanan.rh.program.dto.SkemaTanamDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skema-tanam")
@Tag(name = "Skema Tanam", description = "API untuk manajemen Skema Tanam")
@RequiredArgsConstructor
public class SkemaTanamController {
    
    private final SkemaTanamService skemaTanamService;

    @Autowired
    private PagedResourcesAssembler<SkemaTanam> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary = "Get all skema tanam with pagination")
    public ResponseEntity<PagedModel<EntityModel<SkemaTanam>>> findAll(
            @Parameter(description = "Id Program") @RequestParam(required = false) String programId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filter by nama skema") @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        PagedModel<EntityModel<SkemaTanam>> pagedModel = skemaTanamService.findAll(programId, search,
                pageable, pagedResourcesAssembler);

        return ResponseEntity.ok(pagedModel);
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