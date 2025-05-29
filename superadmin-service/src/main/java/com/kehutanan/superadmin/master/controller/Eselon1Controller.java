package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.Eselon1Service;
import com.kehutanan.superadmin.master.model.Eselon1;
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
@RequestMapping("/api/eselon1")
public class Eselon1Controller {

    private final Eselon1Service service;
    private final PagedResourcesAssembler<Eselon1> pagedResourcesAssembler;

    @Autowired
    public Eselon1Controller(Eselon1Service service,
                            PagedResourcesAssembler<Eselon1> pagedResourcesAssembler) {
        this.service = service;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Eselon1>>> getAllEselon1(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String pejabat,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Eselon1> eselon1Page;
        
        // Check if any filter is provided
        if ((nama != null && !nama.isEmpty()) || 
            (pejabat != null && !pejabat.isEmpty())) {
            eselon1Page = service.findByFilters(nama, pejabat, pageable);
        } else {
            eselon1Page = service.findAll(pageable);
        }

        PagedModel<EntityModel<Eselon1>> pagedModel = pagedResourcesAssembler.toModel(eselon1Page);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Eselon1> getEselon1ById(@PathVariable Long id) {
        try {
            Eselon1 eselon1 = service.findById(id);
            return ResponseEntity.ok(eselon1);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Eselon1> createEselon1(
            @RequestPart String nama,
            @RequestPart(required = false) String pejabat,
            @RequestPart(required = false) String tugasDanFungsi,
            @RequestPart(required = false) String keterangan) {

        try {
            Eselon1 newEselon1 = new Eselon1();
            newEselon1.setNama(nama);
            newEselon1.setPejabat(pejabat);
            newEselon1.setTugasDanFungsi(tugasDanFungsi);
            newEselon1.setKeterangan(keterangan);

            Eselon1 savedEselon1 = service.save(newEselon1);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEselon1);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Eselon1> updateEselon1(
            @PathVariable Long id,
            @RequestPart String nama,
            @RequestPart(required = false) String pejabat,
            @RequestPart(required = false) String tugasDanFungsi,
            @RequestPart(required = false) String keterangan) {

        try {
            Eselon1 existingEselon1 = service.findById(id);
            
            // Update the existing eselon1 with new values
            existingEselon1.setNama(nama);
            existingEselon1.setPejabat(pejabat);
            existingEselon1.setTugasDanFungsi(tugasDanFungsi);
            existingEselon1.setKeterangan(keterangan);

            Eselon1 updatedEselon1 = service.update(id, existingEselon1);
            return ResponseEntity.ok(updatedEselon1);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEselon1(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}