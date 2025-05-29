package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.Eselon3Service;
import com.kehutanan.superadmin.master.model.Eselon3;
import com.kehutanan.superadmin.master.service.Eselon2Service;
import com.kehutanan.superadmin.master.model.Eselon2;
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
@RequestMapping("/api/eselon3")
public class Eselon3Controller {

    private final Eselon3Service service;
    private final Eselon2Service eselon2Service;
    private final PagedResourcesAssembler<Eselon3> pagedResourcesAssembler;

    @Autowired
    public Eselon3Controller(Eselon3Service service,
                             Eselon2Service eselon2Service,
                             PagedResourcesAssembler<Eselon3> pagedResourcesAssembler) {
        this.service = service;
        this.eselon2Service = eselon2Service;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Eselon3>>> getAllEselon3(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String pejabat,
            @RequestParam(required = false) Long eselon2Id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Eselon3> eselon3Page;
        
        // Check if any filter is provided
        if ((nama != null && !nama.isEmpty()) || 
            (pejabat != null && !pejabat.isEmpty()) ||
            (eselon2Id != null)) {
            eselon3Page = service.findByFilters(nama, pejabat, eselon2Id, pageable);
        } else {
            eselon3Page = service.findAll(pageable);
        }

        PagedModel<EntityModel<Eselon3>> pagedModel = pagedResourcesAssembler.toModel(eselon3Page);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Eselon3> getEselon3ById(@PathVariable Long id) {
        try {
            Eselon3 eselon3 = service.findById(id);
            return ResponseEntity.ok(eselon3);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Eselon3> createEselon3(
            @RequestPart String eselon2Id,
            @RequestPart String nama,
            @RequestPart(required = false) String pejabat,
            @RequestPart(required = false) String tugasDanFungsi,
            @RequestPart(required = false) String keterangan) {

        try {
            Long eselon2IdLong = Long.parseLong(eselon2Id);
            Eselon2 eselon2 = eselon2Service.findById(eselon2IdLong);
            
            Eselon3 newEselon3 = new Eselon3();
            newEselon3.setEselon2(eselon2);
            newEselon3.setNama(nama);
            newEselon3.setPejabat(pejabat);
            newEselon3.setTugasDanFungsi(tugasDanFungsi);
            newEselon3.setKeterangan(keterangan);

            Eselon3 savedEselon3 = service.save(newEselon3);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEselon3);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Eselon3> updateEselon3(
            @PathVariable Long id,
            @RequestPart String eselon2Id,
            @RequestPart String nama,
            @RequestPart(required = false) String pejabat,
            @RequestPart(required = false) String tugasDanFungsi,
            @RequestPart(required = false) String keterangan) {

        try {
            Long eselon2IdLong = Long.parseLong(eselon2Id);
            Eselon3 existingEselon3 = service.findById(id);
            Eselon2 eselon2 = eselon2Service.findById(eselon2IdLong);
            
            // Update the existing eselon3 with new values
            existingEselon3.setEselon2(eselon2);
            existingEselon3.setNama(nama);
            existingEselon3.setPejabat(pejabat);
            existingEselon3.setTugasDanFungsi(tugasDanFungsi);
            existingEselon3.setKeterangan(keterangan);

            Eselon3 updatedEselon3 = service.update(id, existingEselon3);
            return ResponseEntity.ok(updatedEselon3);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEselon3(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}