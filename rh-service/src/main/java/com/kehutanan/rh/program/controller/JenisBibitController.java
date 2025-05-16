package com.kehutanan.rh.program.controller;

import com.kehutanan.rh.program.model.JenisBibit;
import com.kehutanan.rh.program.service.JenisBibitService;
import com.kehutanan.rh.program.dto.JenisBibitDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jenis-bibit")
@Tag(name = "Jenis Bibit", description = "API untuk manajemen Jenis Bibit")
public class JenisBibitController {

    private final JenisBibitService jenisBibitService;

    @Autowired
    public JenisBibitController(JenisBibitService jenisBibitService) {
        this.jenisBibitService = jenisBibitService;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua jenis bibit")
    public List<JenisBibit> getAll() {
        return jenisBibitService.findAll();
    }

    @GetMapping("/program/{programId}")
    @Operation(summary = "Mendapatkan jenis bibit berdasarkan Program ID")
    public List<JenisBibit> getByProgramId(@PathVariable UUID programId) {
        return jenisBibitService.findByProgramId(programId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan jenis bibit berdasarkan ID")
    public ResponseEntity<JenisBibit> getById(@PathVariable UUID id) {
        return jenisBibitService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Membuat jenis bibit baru")
    public ResponseEntity<JenisBibit> create(@Valid @RequestBody JenisBibitDto request) {
        return ResponseEntity.ok(jenisBibitService.createFromRequest(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui jenis bibit")
    public ResponseEntity<JenisBibit> update(
        @PathVariable UUID id,
        @Valid @RequestBody JenisBibitDto request) {
        
        JenisBibit updated = jenisBibitService.update(id, 
            request.getProgramId(),
            request.getKategori(),
            request.getNamaBibit(),
            request.getSumberBibit(),
            request.getJumlah(),
            request.getStatus(),
            request.getKeterangan());
        
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus jenis bibit")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        jenisBibitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}