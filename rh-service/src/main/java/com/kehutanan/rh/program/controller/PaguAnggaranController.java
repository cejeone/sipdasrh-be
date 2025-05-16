package com.kehutanan.rh.program.controller;

import com.kehutanan.rh.program.model.PaguAnggaran;
import com.kehutanan.rh.program.service.PaguAnggaranService;
import com.kehutanan.rh.program.dto.PaguAnggaranDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pagu-anggaran")
@Tag(name = "Pagu Anggaran", description = "API untuk manajemen Pagu Anggaran")
public class PaguAnggaranController {

    private final PaguAnggaranService paguAnggaranService;

    @Autowired
    public PaguAnggaranController(PaguAnggaranService paguAnggaranService) {
        this.paguAnggaranService = paguAnggaranService;
    }

    @GetMapping
    @Operation(summary = "Mendapatkan semua pagu anggaran")
    public List<PaguAnggaran> getAll() {
        return paguAnggaranService.findAll();
    }

    @GetMapping("/program/{programId}")
    @Operation(summary = "Mendapatkan pagu anggaran berdasarkan Program ID")
    public List<PaguAnggaran> getByProgramId(@PathVariable UUID programId) {
        return paguAnggaranService.findByProgramId(programId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan pagu anggaran berdasarkan ID")
    public ResponseEntity<PaguAnggaran> getById(@PathVariable UUID id) {
        return paguAnggaranService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Membuat pagu anggaran baru")
    public ResponseEntity<PaguAnggaran> create(@Valid @RequestBody PaguAnggaranDto request) {
        return ResponseEntity.ok(paguAnggaranService.createFromRequest(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui pagu anggaran")
    public ResponseEntity<PaguAnggaran> update(
            @PathVariable UUID id,
            @Valid @RequestBody PaguAnggaranDto request) {
        
        PaguAnggaran updated = paguAnggaranService.update(id, 
            request.getProgramId(),
            request.getKategori(),
            request.getSumberAnggaran(),
            request.getTahunAnggaran(),
            request.getPagu(),
            request.getStatus(),
            request.getKeterangan());
        
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus pagu anggaran")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        paguAnggaranService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
