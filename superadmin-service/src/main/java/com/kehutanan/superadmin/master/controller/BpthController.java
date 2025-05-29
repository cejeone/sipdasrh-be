package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.BpthService;
import com.kehutanan.superadmin.master.model.Bpth;
import com.kehutanan.superadmin.master.service.ProvinsiService;
import com.kehutanan.superadmin.master.model.Provinsi;
import com.kehutanan.superadmin.master.service.KabupatenKotaService;
import com.kehutanan.superadmin.master.model.KabupatenKota;
import com.kehutanan.superadmin.master.service.KecamatanService;
import com.kehutanan.superadmin.master.model.Kecamatan;
import com.kehutanan.superadmin.master.service.KelurahanDesaService;
import com.kehutanan.superadmin.master.model.KelurahanDesa;

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
@RequestMapping("/api/bpth")
public class BpthController {

    private final BpthService service;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final KelurahanDesaService kelurahanDesaService;
    private final PagedResourcesAssembler<Bpth> pagedResourcesAssembler;

    @Autowired
    public BpthController(BpthService service,
                         ProvinsiService provinsiService,
                         KabupatenKotaService kabupatenKotaService,
                         KecamatanService kecamatanService,
                         KelurahanDesaService kelurahanDesaService,
                         PagedResourcesAssembler<Bpth> pagedResourcesAssembler) {
        this.service = service;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.kelurahanDesaService = kelurahanDesaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Bpth>>> getAllBpth(
            @RequestParam(required = false) String namaBpth,
            @RequestParam(required = false) Long provinsiId,
            @RequestParam(required = false) Long kabupatenKotaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bpth> bpthPage;
        
        // Check if any filter is provided
        if ((namaBpth != null && !namaBpth.isEmpty()) || 
            (provinsiId != null) ||
            (kabupatenKotaId != null)) {
            bpthPage = service.findByFilters(namaBpth, provinsiId, kabupatenKotaId, pageable);
        } else {
            bpthPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Bpth>> pagedModel = pagedResourcesAssembler.toModel(bpthPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bpth> getBpthById(@PathVariable Long id) {
        try {
            Bpth bpth = service.findById(id);
            return ResponseEntity.ok(bpth);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Bpth> createBpth(
            @RequestPart String namaBpth,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String telepon,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            Bpth newBpth = new Bpth();
            newBpth.setNamaBpth(namaBpth);
            newBpth.setAlamat(alamat);
            newBpth.setTelepon(telepon);
            
            // Set related entities if provided
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                newBpth.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                newBpth.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                newBpth.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                newBpth.setKelurahanDesa(kelurahanDesa);
            }

            Bpth savedBpth = service.save(newBpth);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBpth);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Bpth> updateBpth(
            @PathVariable Long id,
            @RequestPart String namaBpth,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String telepon,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            Bpth existingBpth = service.findById(id);
            
            // Update the existing bpth with new values
            existingBpth.setNamaBpth(namaBpth);
            existingBpth.setAlamat(alamat);
            existingBpth.setTelepon(telepon);
            
            // Update related entities if provided
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                existingBpth.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                existingBpth.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                existingBpth.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                existingBpth.setKelurahanDesa(kelurahanDesa);
            }

            Bpth updatedBpth = service.update(id, existingBpth);
            return ResponseEntity.ok(updatedBpth);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBpth(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}