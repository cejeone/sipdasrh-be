package com.kehutanan.rh.program.controller;

import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.program.service.ProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
public class ProgramController {
    
    private final ProgramService programService;

    @GetMapping
    public ResponseEntity<List<Program>> findAll() {
        return ResponseEntity.ok(programService.findAll());
    }

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