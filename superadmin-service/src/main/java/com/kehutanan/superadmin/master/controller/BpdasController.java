package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.BpdasService;
import com.kehutanan.superadmin.master.model.Bpdas;
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
@RequestMapping("/api/bpdas")
public class BpdasController {

    private final BpdasService service;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final KelurahanDesaService kelurahanDesaService;
    private final PagedResourcesAssembler<Bpdas> pagedResourcesAssembler;

    @Autowired
    public BpdasController(BpdasService service,
                          ProvinsiService provinsiService,
                          KabupatenKotaService kabupatenKotaService,
                          KecamatanService kecamatanService,
                          KelurahanDesaService kelurahanDesaService,
                          PagedResourcesAssembler<Bpdas> pagedResourcesAssembler) {
        this.service = service;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.kelurahanDesaService = kelurahanDesaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Bpdas>>> getAllBpdas(
            @RequestParam(required = false) String namaBpdas,
            @RequestParam(required = false) Long provinsiId,
            @RequestParam(required = false) Long kabupatenKotaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bpdas> bpdasPage;
        
        // Check if any filter is provided
        if ((namaBpdas != null && !namaBpdas.isEmpty()) || 
            provinsiId != null || 
            kabupatenKotaId != null) {
            bpdasPage = service.findByFilters(namaBpdas, provinsiId, kabupatenKotaId, pageable);
        } else {
            bpdasPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Bpdas>> pagedModel = pagedResourcesAssembler.toModel(bpdasPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bpdas> getBpdasById(@PathVariable Long id) {
        try {
            Bpdas bpdas = service.findById(id);
            return ResponseEntity.ok(bpdas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Bpdas> createBpdas(
            @RequestPart String namaBpdas,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String telepon,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            Bpdas newBpdas = new Bpdas();
            newBpdas.setNamaBpdas(namaBpdas);
            newBpdas.setAlamat(alamat);
            newBpdas.setTelepon(telepon);
            
            // Set location details if provided
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                newBpdas.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                newBpdas.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                newBpdas.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                newBpdas.setKelurahanDesa(kelurahanDesa);
            }

            Bpdas savedBpdas = service.save(newBpdas);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBpdas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Bpdas> updateBpdas(
            @PathVariable Long id,
            @RequestPart String namaBpdas,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String telepon,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            Bpdas existingBpdas = service.findById(id);
            
            // Update the existing bpdas with new values
            existingBpdas.setNamaBpdas(namaBpdas);
            existingBpdas.setAlamat(alamat);
            existingBpdas.setTelepon(telepon);
            
            // Update location details if provided
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                existingBpdas.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                existingBpdas.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                existingBpdas.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                existingBpdas.setKelurahanDesa(kelurahanDesa);
            }

            Bpdas updatedBpdas = service.update(id, existingBpdas);
            return ResponseEntity.ok(updatedBpdas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBpdas(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}