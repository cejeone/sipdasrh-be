package com.kehutanan.pepdas.program.controller;

import com.kehutanan.pepdas.program.model.PaguAnggaran;
import com.kehutanan.pepdas.program.service.PaguAnggaranService;
import com.kehutanan.pepdas.program.dto.PaguAnggaranDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pagu-anggaran")
@Tag(name = "Program -> Pagu Anggaran", description = "API untuk manajemen Pagu Anggaran")
public class PaguAnggaranController {
    
    private final PaguAnggaranService paguAnggaranService;
    private final PagedResourcesAssembler<PaguAnggaran> pagedResourcesAssembler;

    @Autowired
    public PaguAnggaranController(PaguAnggaranService paguAnggaranService, 
                                 PagedResourcesAssembler<PaguAnggaran> pagedResourcesAssembler) {
        this.paguAnggaranService = paguAnggaranService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all pagu anggaran with pagination")
    public ResponseEntity<PagedModel<EntityModel<PaguAnggaran>>> getAll(
            @Parameter(description = "Id Program") @RequestParam String ProgramId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filter by nama Sumber Anggaran dan Keterangan") @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        PagedModel<EntityModel<PaguAnggaran>> pagedModel = paguAnggaranService.findAll(ProgramId,search,
                pageable, pagedResourcesAssembler);

        return ResponseEntity.ok(pagedModel);
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