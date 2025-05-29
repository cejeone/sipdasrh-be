package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.Eselon2Service;
import com.kehutanan.superadmin.master.model.Eselon2;
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
@RequestMapping("/api/eselon2")
public class Eselon2Controller {

    private final Eselon2Service service;
    private final Eselon1Service eselon1Service;
    private final PagedResourcesAssembler<Eselon2> pagedResourcesAssembler;

    @Autowired
    public Eselon2Controller(Eselon2Service service,
                              Eselon1Service eselon1Service,
                              PagedResourcesAssembler<Eselon2> pagedResourcesAssembler) {
        this.service = service;
        this.eselon1Service = eselon1Service;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Eselon2>>> getAllEselon2(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String pejabat,
            @RequestParam(required = false) Long eselon1Id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Eselon2> eselon2Page;
        
        // Check if any filter is provided
        if ((nama != null && !nama.isEmpty()) || 
            (pejabat != null && !pejabat.isEmpty()) ||
            (eselon1Id != null)) {
            eselon2Page = service.findByFilters(nama, pejabat, eselon1Id, pageable);
        } else {
            eselon2Page = service.findAll(pageable);
        }

        PagedModel<EntityModel<Eselon2>> pagedModel = pagedResourcesAssembler.toModel(eselon2Page);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Eselon2> getEselon2ById(@PathVariable Long id) {
        try {
            Eselon2 eselon2 = service.findById(id);
            return ResponseEntity.ok(eselon2);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Eselon2> createEselon2(
            @RequestPart String eselon1Id,
            @RequestPart String nama,
            @RequestPart(required = false) String pejabat,
            @RequestPart(required = false) String tugasDanFungsi,
            @RequestPart(required = false) String keterangan) {

        try {
            Long eselon1IdLong = Long.parseLong(eselon1Id);
            Eselon1 eselon1 = eselon1Service.findById(eselon1IdLong);
            
            Eselon2 newEselon2 = new Eselon2();
            newEselon2.setEselon1(eselon1);
            newEselon2.setNama(nama);
            newEselon2.setPejabat(pejabat);
            newEselon2.setTugasDanFungsi(tugasDanFungsi);
            newEselon2.setKeterangan(keterangan);

            Eselon2 savedEselon2 = service.save(newEselon2);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEselon2);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Eselon2> updateEselon2(
            @PathVariable Long id,
            @RequestPart String eselon1Id,
            @RequestPart String nama,
            @RequestPart(required = false) String pejabat,
            @RequestPart(required = false) String tugasDanFungsi,
            @RequestPart(required = false) String keterangan) {

        try {
            Long eselon1IdLong = Long.parseLong(eselon1Id);
            Eselon2 existingEselon2 = service.findById(id);
            Eselon1 eselon1 = eselon1Service.findById(eselon1IdLong);
            
            // Update the existing eselon2 with new values
            existingEselon2.setEselon1(eselon1);
            existingEselon2.setNama(nama);
            existingEselon2.setPejabat(pejabat);
            existingEselon2.setTugasDanFungsi(tugasDanFungsi);
            existingEselon2.setKeterangan(keterangan);

            Eselon2 updatedEselon2 = service.update(id, existingEselon2);
            return ResponseEntity.ok(updatedEselon2);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEselon2(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}