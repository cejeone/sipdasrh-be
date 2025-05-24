package com.kehutanan.pepdas.program.controller;

import com.kehutanan.pepdas.program.dto.ProgramDto;
import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/programs")
@Tag(name = "Program", description = "API untuk manajemen Program RH")
public class ProgramController {

    private final ProgramService programService;
    private final PagedResourcesAssembler<Program> pagedResourcesAssembler;

    public ProgramController(ProgramService programService,
            PagedResourcesAssembler<Program> pagedResourcesAssembler) {
        this.programService = programService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua data program dengan pagination dan filter dinamis")
    public ResponseEntity<PagedModel<EntityModel<Program>>> findAll(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String totalAnggaran,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Program> programPage;
        
        // Check if any filter is provided
        if ((nama != null && !nama.isEmpty()) || 
            (totalAnggaran != null && !totalAnggaran.isEmpty()) ||
            (status != null && !status.isEmpty())) {
            programPage = programService.findByFilters(nama, totalAnggaran, status, pageable);
        } else {
            programPage = programService.findAll(pageable);
        }
        
        PagedModel<EntityModel<Program>> pagedModel = pagedResourcesAssembler.toModel(programPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(programService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Program> create(@RequestBody ProgramDto programDto) {
        return ResponseEntity.ok(programService.save(programDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Program> update(@PathVariable UUID id, @RequestBody ProgramDto programDto) {
        return ResponseEntity.ok(programService.update(id, programDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        programService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}