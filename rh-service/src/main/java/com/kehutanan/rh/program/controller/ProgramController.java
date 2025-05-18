package com.kehutanan.rh.program.controller;

import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.program.service.ProgramService;

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
    @Operation(summary = "Mendapatkan semua data program dengan pagination")
    public ResponseEntity<PagedModel<EntityModel<Program>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Program> programPage = programService.findAll(pageable);
        PagedModel<EntityModel<Program>> pagedModel = pagedResourcesAssembler.toModel(programPage);
        return ResponseEntity.ok(pagedModel);
    }

    // @GetMapping
    // public ResponseEntity<List<Program>> findAll() {
    // return ResponseEntity.ok(programService.findAll());
    // }

    @GetMapping("/{id}")
    public ResponseEntity<Program> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(programService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Program> create(@RequestBody Program program) {
        return ResponseEntity.ok(programService.save(program));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Program> update(@PathVariable UUID id, @RequestBody Program program) {
        return ResponseEntity.ok(programService.update(id, program));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        programService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}