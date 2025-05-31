package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.PeranService;
import com.kehutanan.superadmin.master.model.Peran;
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
@RequestMapping("/api/peran")
public class PeranController {

    private final PeranService service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Peran> pagedResourcesAssembler;

    @Autowired
    public PeranController(PeranService service,
            LovService lovService,
            PagedResourcesAssembler<Peran> pagedResourcesAssembler) {
        this.service = service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Peran>>> getAllPeran(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String deskripsi,
            @RequestParam(required = false) Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Peran> peranPage;

        // Check if any filter is provided
        if ((nama != null && !nama.isEmpty()) ||
                (deskripsi != null && !deskripsi.isEmpty()) ||
                (statusId != null)) {
            peranPage = service.findByFilters(nama, deskripsi, statusId, pageable);
        } else {
            peranPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Peran>> pagedModel = pagedResourcesAssembler.toModel(peranPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peran> getPeranById(@PathVariable Long id) {
        try {
            Peran peran = service.findById(id);
            return ResponseEntity.ok(peran);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Peran> createPeran(
            @RequestPart String nama,
            @RequestPart(required = false) String deskripsi,
            @RequestPart String statusId) {

        try {
            Long statusIdInt = Long.parseLong(statusId);
            Lov status = lovService.findById(statusIdInt);

            Peran newPeran = new Peran();
            newPeran.setNama(nama);
            newPeran.setDeskripsi(deskripsi);
            newPeran.setStatus(status);

            Peran savedPeran = service.save(newPeran);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPeran);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Peran> updatePeran(
            @PathVariable Long id,
            @RequestPart String nama,
            @RequestPart(required = false) String deskripsi,
            @RequestPart String statusId) {

        try {
            Long statusIdInt = Long.parseLong(statusId);
            Peran existingPeran = service.findById(id);
            Lov status = lovService.findById(statusIdInt);

            // Update the existing peran with new values
            existingPeran.setNama(nama);
            existingPeran.setDeskripsi(deskripsi);
            existingPeran.setStatus(status);

            Peran updatedPeran = service.update(id, existingPeran);
            return ResponseEntity.ok(updatedPeran);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeran(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}