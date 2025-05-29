package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.IntegrasiService;
import com.kehutanan.superadmin.master.model.Integrasi;
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
@RequestMapping("/api/integrasi")
public class IntegrasiController {

    private final IntegrasiService service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Integrasi> pagedResourcesAssembler;

    @Autowired
    public IntegrasiController(IntegrasiService service,
                             LovService lovService,
                             PagedResourcesAssembler<Integrasi> pagedResourcesAssembler) {
        this.service = service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Integrasi>>> getAllIntegrasi(
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String tipe,
            @RequestParam(required = false) String deskripsi,
            @RequestParam(required = false) Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Integrasi> integrasiPage;
        
        // Check if any filter is provided
        if ((url != null && !url.isEmpty()) || 
            (tipe != null && !tipe.isEmpty()) ||
            (deskripsi != null && !deskripsi.isEmpty()) ||
            (statusId != null)) {
            integrasiPage = service.findByFilters(url, tipe, deskripsi, statusId, pageable);
        } else {
            integrasiPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Integrasi>> pagedModel = pagedResourcesAssembler.toModel(integrasiPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Integrasi> getIntegrasiById(@PathVariable Long id) {
        try {
            Integrasi integrasi = service.findById(id);
            return ResponseEntity.ok(integrasi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Integrasi> createIntegrasi(
            @RequestPart String url,
            @RequestPart(required = false) String apiKey,
            @RequestPart String tipe,
            @RequestPart(required = false) String deskripsi,
            @RequestPart String statusId) {

        try {
            Long statusIdLong = Long.parseLong(statusId);
            Lov status = lovService.findById(statusIdLong);
            
            Integrasi newIntegrasi = new Integrasi();
            newIntegrasi.setUrl(url);
            newIntegrasi.setApiKey(apiKey);
            newIntegrasi.setTipe(tipe);
            newIntegrasi.setDeskripsi(deskripsi);
            newIntegrasi.setStatus(status);

            Integrasi savedIntegrasi = service.save(newIntegrasi);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedIntegrasi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integrasi> updateIntegrasi(
            @PathVariable Long id,
            @RequestPart String url,
            @RequestPart(required = false) String apiKey,
            @RequestPart String tipe,
            @RequestPart(required = false) String deskripsi,
            @RequestPart String statusId) {

        try {
            Long statusIdLong = Long.parseLong(statusId);
            Integrasi existingIntegrasi = service.findById(id);
            Lov status = lovService.findById(statusIdLong);
            
            // Update the existing integrasi with new values
            existingIntegrasi.setUrl(url);
            existingIntegrasi.setApiKey(apiKey);
            existingIntegrasi.setTipe(tipe);
            existingIntegrasi.setDeskripsi(deskripsi);
            existingIntegrasi.setStatus(status);

            Integrasi updatedIntegrasi = service.update(id, existingIntegrasi);
            return ResponseEntity.ok(updatedIntegrasi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntegrasi(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}