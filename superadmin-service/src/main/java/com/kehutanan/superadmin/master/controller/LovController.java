package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.LovService;
import com.kehutanan.superadmin.master.model.Lov;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PagedResourcesAssembler;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/lovs")
public class LovController {

    private final LovService service;
     private final PagedResourcesAssembler<Lov> pagedResourcesAssembler;

    @Autowired
    public LovController(LovService service,
                         PagedResourcesAssembler<Lov> pagedResourcesAssembler) {
        this.service = service;
         this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Lov>>> getAllLovs(
            @RequestParam(required = false) String namaKategori,
            @RequestParam(required = false) String nilai,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Lov> lovPage;
        
        // Check if any filter is provided
        if ((namaKategori != null && !namaKategori.isEmpty()) || 
            (nilai != null && !nilai.isEmpty()) ||
            (status != null && !status.isEmpty())) {
            lovPage = service.findByFilters(namaKategori, nilai, status, pageable);
        } else {
            lovPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Lov>> pagedModel = pagedResourcesAssembler.toModel(lovPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lov> getLovById(@PathVariable Long id) {
        try {
            Lov lov = service.findById(id);
            return ResponseEntity.ok(lov);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Lov> createLov(
            @RequestPart String namaKategori,
            @RequestPart String nilai,
            @RequestPart(required = false) String kelas,
            @RequestPart(required = false) String deskripsi,
            @RequestPart(required = false) String status) {

        Lov lov = new Lov();
        lov.setNamaKategori(namaKategori);
        lov.setNilai(nilai);
        lov.setKelas(kelas);
        lov.setDeskripsi(deskripsi);
        lov.setStatus(status);

        Lov savedLov = service.save(lov);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLov);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Lov> updateLov(
            @PathVariable Long id,
            @RequestPart String namaKategori,
            @RequestPart String nilai,
            @RequestPart(required = false) String kelas,
            @RequestPart(required = false) String deskripsi,
            @RequestPart(required = false) String status) {

        try {
            Lov existingLov = service.findById(id);
            
            // Update the existing lov with new values
            existingLov.setNamaKategori(namaKategori);
            existingLov.setNilai(nilai);
            existingLov.setKelas(kelas);
            existingLov.setDeskripsi(deskripsi);
            existingLov.setStatus(status);

            Lov updatedLov = service.update(id,existingLov);
            return ResponseEntity.ok(updatedLov);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLov(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}