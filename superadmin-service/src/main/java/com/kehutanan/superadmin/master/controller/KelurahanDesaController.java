package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.KelurahanDesaService;
import com.kehutanan.superadmin.master.model.KelurahanDesa;
import com.kehutanan.superadmin.master.service.KecamatanService;
import com.kehutanan.superadmin.master.model.Kecamatan;
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
@RequestMapping("/api/kelurahan-desa")
public class KelurahanDesaController {

    private final KelurahanDesaService service;
    private final KecamatanService kecamatanService;
    private final PagedResourcesAssembler<KelurahanDesa> pagedResourcesAssembler;

    @Autowired
    public KelurahanDesaController(KelurahanDesaService service,
                                  KecamatanService kecamatanService,
                                  PagedResourcesAssembler<KelurahanDesa> pagedResourcesAssembler) {
        this.service = service;
        this.kecamatanService = kecamatanService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<KelurahanDesa>>> getAllKelurahanDesa(
            @RequestParam(required = false) String kelurahan,
            @RequestParam(required = false) Long kecamatanId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KelurahanDesa> kelurahanDesaPage;
        
        // Check if any filter is provided
        if ((kelurahan != null && !kelurahan.isEmpty()) || 
            (kecamatanId != null)) {
            kelurahanDesaPage = service.findByFilters(kelurahan, kecamatanId, pageable);
        } else {
            kelurahanDesaPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<KelurahanDesa>> pagedModel = pagedResourcesAssembler.toModel(kelurahanDesaPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KelurahanDesa> getKelurahanDesaById(@PathVariable Long id) {
        try {
            KelurahanDesa kelurahanDesa = service.findById(id);
            return ResponseEntity.ok(kelurahanDesa);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KelurahanDesa> createKelurahanDesa(
            @RequestPart String kecamatanId,
            @RequestPart String kelurahan) {

        try {
            Long kecamatanIdLong = Long.parseLong(kecamatanId);
            Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
            
            KelurahanDesa newKelurahanDesa = new KelurahanDesa();
            newKelurahanDesa.setKecamatan(kecamatan);
            newKelurahanDesa.setKelurahan(kelurahan);

            KelurahanDesa savedKelurahanDesa = service.save(newKelurahanDesa);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKelurahanDesa);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KelurahanDesa> updateKelurahanDesa(
            @PathVariable Long id,
            @RequestPart String kecamatanId,
            @RequestPart String kelurahan) {

        try {
            Long kecamatanIdLong = Long.parseLong(kecamatanId);
            KelurahanDesa existingKelurahanDesa = service.findById(id);
            Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
            
            // Update the existing kelurahanDesa with new values
            existingKelurahanDesa.setKecamatan(kecamatan);
            existingKelurahanDesa.setKelurahan(kelurahan);

            KelurahanDesa updatedKelurahanDesa = service.update(id, existingKelurahanDesa);
            return ResponseEntity.ok(updatedKelurahanDesa);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKelurahanDesa(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}