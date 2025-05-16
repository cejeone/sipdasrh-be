package com.kehutanan.rh.controller;

import com.kehutanan.rh.model.Program;
import com.kehutanan.rh.dto.ProgramDto;
import com.kehutanan.rh.service.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/program")
@Tag(name = "Program", description = "API untuk manajemen Program")
public class ProgramController {

    private final ProgramService programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua program")
    public List<Program> getAll() {
        return programService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan program dan data terkait berdasarkan ID")
    public ResponseEntity<ProgramDto> getById(@PathVariable UUID id) {
        try {
            ProgramDto response = programService.getProgramWithDetails(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Membuat program baru")
    public ResponseEntity<Program> create(@Valid @RequestBody Program program) {
        return ResponseEntity.ok(programService.save(program));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui program")
    public ResponseEntity<Program> update(@PathVariable UUID id, 
                                        @Valid @RequestBody Program program) {
        Program updated = programService.update(id, program);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus program")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        programService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}