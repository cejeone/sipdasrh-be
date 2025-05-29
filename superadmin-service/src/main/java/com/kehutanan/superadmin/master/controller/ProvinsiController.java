package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.ProvinsiService;
import com.kehutanan.superadmin.master.model.Provinsi;
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
@RequestMapping("/api/provinsi")
public class ProvinsiController {

    private final ProvinsiService service;
    private final PagedResourcesAssembler<Provinsi> pagedResourcesAssembler;

    @Autowired
    public ProvinsiController(ProvinsiService service,
                          PagedResourcesAssembler<Provinsi> pagedResourcesAssembler) {
        this.service = service;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Provinsi>>> getAllProvinsi(
            @RequestParam(required = false) String namaProvinsi,
            @RequestParam(required = false) String kodeDepdagri,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Provinsi> provinsiPage;
        
        // Check if any filter is provided
        if ((namaProvinsi != null && !namaProvinsi.isEmpty()) || 
            (kodeDepdagri != null && !kodeDepdagri.isEmpty())) {
            provinsiPage = service.findByFilters(namaProvinsi, kodeDepdagri, pageable);
        } else {
            provinsiPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Provinsi>> pagedModel = pagedResourcesAssembler.toModel(provinsiPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Provinsi> getProvinsiById(@PathVariable Long id) {
        try {
            Provinsi provinsi = service.findById(id);
            return ResponseEntity.ok(provinsi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Provinsi> createProvinsi(
            @RequestPart String namaProvinsi,
            @RequestPart String kodeDepdagri) {

        Provinsi provinsi = new Provinsi();
        provinsi.setNamaProvinsi(namaProvinsi);
        provinsi.setKodeDepdagri(kodeDepdagri);

        Provinsi savedProvinsi = service.save(provinsi);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProvinsi);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Provinsi> updateProvinsi(
            @PathVariable Long id,
            @RequestPart String namaProvinsi,
            @RequestPart String kodeDepdagri) {

        try {
            Provinsi existingProvinsi = service.findById(id);
            
            // Update the existing provinsi with new values
            existingProvinsi.setNamaProvinsi(namaProvinsi);
            existingProvinsi.setKodeDepdagri(kodeDepdagri);

            Provinsi updatedProvinsi = service.update(id, existingProvinsi);
            return ResponseEntity.ok(updatedProvinsi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvinsi(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}