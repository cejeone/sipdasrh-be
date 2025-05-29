package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.KabupatenKotaService;
import com.kehutanan.superadmin.master.model.KabupatenKota;
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
@RequestMapping("/api/kabupaten-kota")
public class KabupatenKotaController {

    private final KabupatenKotaService service;
    private final ProvinsiService provinsiService;
    private final PagedResourcesAssembler<KabupatenKota> pagedResourcesAssembler;

    @Autowired
    public KabupatenKotaController(KabupatenKotaService service,
                                  ProvinsiService provinsiService,
                                  PagedResourcesAssembler<KabupatenKota> pagedResourcesAssembler) {
        this.service = service;
        this.provinsiService = provinsiService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<KabupatenKota>>> getAllKabupatenKota(
            @RequestParam(required = false) String kabupatenKota,
            @RequestParam(required = false) String kodeDepdagri,
            @RequestParam(required = false) Long provinsiId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KabupatenKota> kabupatenKotaPage;
        
        // Check if any filter is provided
        if ((kabupatenKota != null && !kabupatenKota.isEmpty()) || 
            (kodeDepdagri != null && !kodeDepdagri.isEmpty()) ||
            (provinsiId != null)) {
            kabupatenKotaPage = service.findByFilters(kabupatenKota, kodeDepdagri, provinsiId, pageable);
        } else {
            kabupatenKotaPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<KabupatenKota>> pagedModel = pagedResourcesAssembler.toModel(kabupatenKotaPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KabupatenKota> getKabupatenKotaById(@PathVariable Long id) {
        try {
            KabupatenKota kabupatenKota = service.findById(id);
            return ResponseEntity.ok(kabupatenKota);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KabupatenKota> createKabupatenKota(
            @RequestPart String provinsiId,
            @RequestPart String kabupatenKota,
            @RequestPart(required = false) String kodeDepdagri) {

        try {
            Long provinsiIdLong = Long.parseLong(provinsiId);
            Provinsi provinsi = provinsiService.findById(provinsiIdLong);
            
            KabupatenKota newKabupatenKota = new KabupatenKota();
            newKabupatenKota.setProvinsi(provinsi);
            newKabupatenKota.setKabupatenKota(kabupatenKota);
            newKabupatenKota.setKodeDepdagri(kodeDepdagri);

            KabupatenKota savedKabupatenKota = service.save(newKabupatenKota);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKabupatenKota);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KabupatenKota> updateKabupatenKota(
            @PathVariable Long id,
            @RequestPart String provinsiId,
            @RequestPart String kabupatenKota,
            @RequestPart(required = false) String kodeDepdagri) {

        try {

            Long provinsiIdLong = Long.parseLong(provinsiId);
            KabupatenKota existingKabupatenKota = service.findById(id);
            Provinsi provinsi = provinsiService.findById(provinsiIdLong);
            
            // Update the existing kabupatenKota with new values
            existingKabupatenKota.setProvinsi(provinsi);
            existingKabupatenKota.setKabupatenKota(kabupatenKota);
            existingKabupatenKota.setKodeDepdagri(kodeDepdagri);

            KabupatenKota updatedKabupatenKota = service.update(id, existingKabupatenKota);
            return ResponseEntity.ok(updatedKabupatenKota);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKabupatenKota(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}