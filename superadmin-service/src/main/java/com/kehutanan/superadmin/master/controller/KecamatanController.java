package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.KecamatanService;
import com.kehutanan.superadmin.master.model.Kecamatan;
import com.kehutanan.superadmin.master.service.KabupatenKotaService;
import com.kehutanan.superadmin.master.model.KabupatenKota;
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
@RequestMapping("/api/kecamatan")
public class KecamatanController {

    private final KecamatanService service;
    private final KabupatenKotaService kabupatenKotaService;
    private final PagedResourcesAssembler<Kecamatan> pagedResourcesAssembler;

    @Autowired
    public KecamatanController(KecamatanService service,
                              KabupatenKotaService kabupatenKotaService,
                              PagedResourcesAssembler<Kecamatan> pagedResourcesAssembler) {
        this.service = service;
        this.kabupatenKotaService = kabupatenKotaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Kecamatan>>> getAllKecamatan(
            @RequestParam(required = false) String kecamatan,
            @RequestParam(required = false) String kodeDepdagri,
            @RequestParam(required = false) Long kabupatenKotaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Kecamatan> kecamatanPage;
        
        // Check if any filter is provided
        if ((kecamatan != null && !kecamatan.isEmpty()) || 
            (kodeDepdagri != null && !kodeDepdagri.isEmpty()) ||
            (kabupatenKotaId != null)) {
            kecamatanPage = service.findByFilters(kecamatan, kodeDepdagri, kabupatenKotaId, pageable);
        } else {
            kecamatanPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Kecamatan>> pagedModel = pagedResourcesAssembler.toModel(kecamatanPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kecamatan> getKecamatanById(@PathVariable Long id) {
        try {
            Kecamatan kecamatan = service.findById(id);
            return ResponseEntity.ok(kecamatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Kecamatan> createKecamatan(
            @RequestPart String kabupatenKotaId,
            @RequestPart String kecamatan,
            @RequestPart(required = false) String kodeDepdagri) {

        try {
            Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
            KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
            
            Kecamatan newKecamatan = new Kecamatan();
            newKecamatan.setKabupatenKota(kabupatenKota);
            newKecamatan.setKecamatan(kecamatan);
            newKecamatan.setKodeDepdagri(kodeDepdagri);

            Kecamatan savedKecamatan = service.save(newKecamatan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKecamatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Kecamatan> updateKecamatan(
            @PathVariable Long id,
            @RequestPart String kabupatenKotaId,
            @RequestPart String kecamatan,
            @RequestPart(required = false) String kodeDepdagri) {

        try {
            Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
            Kecamatan existingKecamatan = service.findById(id);
            KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
            
            // Update the existing kecamatan with new values
            existingKecamatan.setKabupatenKota(kabupatenKota);
            existingKecamatan.setKecamatan(kecamatan);
            existingKecamatan.setKodeDepdagri(kodeDepdagri);

            Kecamatan updatedKecamatan = service.update(id, existingKecamatan);
            return ResponseEntity.ok(updatedKecamatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKecamatan(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}