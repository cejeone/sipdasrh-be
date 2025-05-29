package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.KonfigurasiService;
import com.kehutanan.superadmin.master.model.Konfigurasi;
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

@RestController
@RequestMapping("/api/konfigurasi")
public class KonfigurasiController {

    private final KonfigurasiService service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Konfigurasi> pagedResourcesAssembler;

    @Autowired
    public KonfigurasiController(KonfigurasiService service,
                                LovService lovService,
                                PagedResourcesAssembler<Konfigurasi> pagedResourcesAssembler) {
        this.service = service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Konfigurasi>>> getAllKonfigurasi(
            @RequestParam(required = false) Integer key,
            @RequestParam(required = false) String value,
            @RequestParam(required = false) String deskripsi,
            @RequestParam(required = false) Long tipeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Konfigurasi> konfigurasiPage;
        
        // Check if any filter is provided
        if (key != null || 
            (value != null && !value.isEmpty()) || 
            (deskripsi != null && !deskripsi.isEmpty()) ||
            tipeId != null) {
            konfigurasiPage = service.findByFilters(key, value, deskripsi, tipeId, pageable);
        } else {
            konfigurasiPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Konfigurasi>> pagedModel = pagedResourcesAssembler.toModel(konfigurasiPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Konfigurasi> getKonfigurasiById(@PathVariable Long id) {
        try {
            Konfigurasi konfigurasi = service.findById(id);
            return ResponseEntity.ok(konfigurasi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Konfigurasi> createKonfigurasi(
            @RequestPart String tipeId,
            @RequestPart(required = false) String key,
            @RequestPart String value,
            @RequestPart(required = false) String deskripsi) {

        try {
            Long tipeIdLong = Long.parseLong(tipeId);
            Lov tipe = lovService.findById(tipeIdLong);
            
            Konfigurasi newKonfigurasi = new Konfigurasi();
            newKonfigurasi.setTipe(tipe);
            newKonfigurasi.setKey(key != null ? Integer.parseInt(key) : null);
            newKonfigurasi.setValue(value);
            newKonfigurasi.setDeskripsi(deskripsi);

            Konfigurasi savedKonfigurasi = service.save(newKonfigurasi);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKonfigurasi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Konfigurasi> updateKonfigurasi(
            @PathVariable Long id,
            @RequestPart String tipeId,
            @RequestPart(required = false) String key,
            @RequestPart String value,
            @RequestPart(required = false) String deskripsi) {

        try {
            Long tipeIdLong = Long.parseLong(tipeId);
            Konfigurasi existingKonfigurasi = service.findById(id);
            Lov tipe = lovService.findById(tipeIdLong);
            
            // Update the existing konfigurasi with new values
            existingKonfigurasi.setTipe(tipe);
            existingKonfigurasi.setKey(key != null ? Integer.parseInt(key) : null);
            existingKonfigurasi.setValue(value);
            existingKonfigurasi.setDeskripsi(deskripsi);

            Konfigurasi updatedKonfigurasi = service.update(id, existingKonfigurasi);
            return ResponseEntity.ok(updatedKonfigurasi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKonfigurasi(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}