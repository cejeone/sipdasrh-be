package com.kehutanan.superadmin.master.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kehutanan.superadmin.master.model.Spas;
import com.kehutanan.superadmin.master.service.BpdasService;
import com.kehutanan.superadmin.master.service.DasService;
import com.kehutanan.superadmin.master.service.KabupatenKotaService;
import com.kehutanan.superadmin.master.service.KecamatanService;
import com.kehutanan.superadmin.master.service.KelurahanDesaService;
import com.kehutanan.superadmin.master.service.LovService;
import com.kehutanan.superadmin.master.service.ProvinsiService;
import com.kehutanan.superadmin.master.service.SpasService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/spas")
public class SpasController {

    private final SpasService service;
    private final BpdasService bpdasService;
    private final DasService dasService;
    private final LovService lovService;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final KelurahanDesaService kelurahanDesaService;
    private final PagedResourcesAssembler<Spas> pagedResourcesAssembler;

    @Autowired
    public SpasController(SpasService service,
                          BpdasService bpdasService,
                          DasService dasService,
                          LovService lovService,
                          ProvinsiService provinsiService,
                          KabupatenKotaService kabupatenKotaService,
                          KecamatanService kecamatanService,
                          KelurahanDesaService kelurahanDesaService,
                          PagedResourcesAssembler<Spas> pagedResourcesAssembler) {
        this.service = service;
        this.bpdasService = bpdasService;
        this.dasService = dasService;
        this.lovService = lovService;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.kelurahanDesaService = kelurahanDesaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Spas>>> getAllSpas(
            @RequestParam(required = false) String spas,
            @RequestParam(required = false) Long bpdasId,
            @RequestParam(required = false) Long dasId,
            @RequestParam(required = false) Long provinsiId,
            @RequestParam(required = false) Long kabupatenKotaId,
            @RequestParam(required = false) Long tipeSpasId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spas> spasPage;
        
        // Check if any filter is provided
        if (spas != null || bpdasId != null || dasId != null || provinsiId != null || 
            kabupatenKotaId != null || tipeSpasId != null || statusId != null) {
            spasPage = service.findByFilters(spas, bpdasId, dasId, provinsiId, 
                                           kabupatenKotaId, tipeSpasId, statusId, pageable);
        } else {
            spasPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Spas>> pagedModel = pagedResourcesAssembler.toModel(spasPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Spas> getSpasById(@PathVariable Long id) {
        try {
            Spas spas = service.findById(id);
            return ResponseEntity.ok(spas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Spas> createSpas(
            @RequestPart String spas,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String lintang,
            @RequestPart(required = false) String bujur,
            @RequestPart String bpdasId,
            @RequestPart String dasId,
            @RequestPart String tipeSpasId,
            @RequestPart(required = false) String frekuensiPengirimanDataId,
            @RequestPart(required = false) String kanalDataId,
            @RequestPart String statusId,
            @RequestPart String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            Spas newSpas = new Spas();
            newSpas.setSpas(spas);
            newSpas.setKeterangan(keterangan);
            newSpas.setAlamat(alamat);
            
            if (lintang != null && !lintang.isEmpty()) {
                newSpas.setLintang(lintang);
            }
            
            if (bujur != null && !bujur.isEmpty()) {
                newSpas.setBujur(bujur);
            }
            
            // Set related entities
            newSpas.setBpdas(bpdasService.findById(Long.parseLong(bpdasId)));
            newSpas.setDas(dasService.findById(Long.parseLong(dasId)));
            newSpas.setTipeSpas(lovService.findById(Long.parseLong(tipeSpasId)));
            newSpas.setStatus(lovService.findById(Long.parseLong(statusId)));
            newSpas.setProvinsi(provinsiService.findById(Long.parseLong(provinsiId)));
            
            if (frekuensiPengirimanDataId != null && !frekuensiPengirimanDataId.isEmpty()) {
                newSpas.setFrekuensiPengirimanData(lovService.findById(Long.parseLong(frekuensiPengirimanDataId)));
            }
            
            if (kanalDataId != null && !kanalDataId.isEmpty()) {
                newSpas.setKanalData(lovService.findById(Long.parseLong(kanalDataId)));
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                newSpas.setKabupatenKota(kabupatenKotaService.findById(Long.parseLong(kabupatenKotaId)));
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                newSpas.setKecamatan(kecamatanService.findById(Long.parseLong(kecamatanId)));
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                newSpas.setKelurahanDesa(kelurahanDesaService.findById(Long.parseLong(kelurahanDesaId)));
            }

            Spas savedSpas = service.save(newSpas);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSpas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Spas> updateSpas(
            @PathVariable Long id,
            @RequestPart String spas,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String lintang,
            @RequestPart(required = false) String bujur,
            @RequestPart String bpdasId,
            @RequestPart String dasId,
            @RequestPart String tipeSpasId,
            @RequestPart(required = false) String frekuensiPengirimanDataId,
            @RequestPart(required = false) String kanalDataId,
            @RequestPart String statusId,
            @RequestPart String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            Spas existingSpas = service.findById(id);
            
            existingSpas.setSpas(spas);
            existingSpas.setKeterangan(keterangan);
            existingSpas.setAlamat(alamat);
            
            if (lintang != null && !lintang.isEmpty()) {
                existingSpas.setLintang(lintang);
            } else {
                existingSpas.setLintang(null);
            }
            
            if (bujur != null && !bujur.isEmpty()) {
                existingSpas.setBujur(bujur);
            } else {
                existingSpas.setBujur(null);
            }
            
            // Set related entities
            existingSpas.setBpdas(bpdasService.findById(Long.parseLong(bpdasId)));
            existingSpas.setDas(dasService.findById(Long.parseLong(dasId)));
            existingSpas.setTipeSpas(lovService.findById(Long.parseLong(tipeSpasId)));
            existingSpas.setStatus(lovService.findById(Long.parseLong(statusId)));
            existingSpas.setProvinsi(provinsiService.findById(Long.parseLong(provinsiId)));
            
            if (frekuensiPengirimanDataId != null && !frekuensiPengirimanDataId.isEmpty()) {
                existingSpas.setFrekuensiPengirimanData(lovService.findById(Long.parseLong(frekuensiPengirimanDataId)));
            } else {
                existingSpas.setFrekuensiPengirimanData(null);
            }
            
            if (kanalDataId != null && !kanalDataId.isEmpty()) {
                existingSpas.setKanalData(lovService.findById(Long.parseLong(kanalDataId)));
            } else {
                existingSpas.setKanalData(null);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                existingSpas.setKabupatenKota(kabupatenKotaService.findById(Long.parseLong(kabupatenKotaId)));
            } else {
                existingSpas.setKabupatenKota(null);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                existingSpas.setKecamatan(kecamatanService.findById(Long.parseLong(kecamatanId)));
            } else {
                existingSpas.setKecamatan(null);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                existingSpas.setKelurahanDesa(kelurahanDesaService.findById(Long.parseLong(kelurahanDesaId)));
            } else {
                existingSpas.setKelurahanDesa(null);
            }

            Spas updatedSpas = service.update(id, existingSpas);
            return ResponseEntity.ok(updatedSpas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpas(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}