package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.InstitusiService;
import com.kehutanan.superadmin.master.model.Institusi;
import com.kehutanan.superadmin.master.service.ProvinsiService;
import com.kehutanan.superadmin.master.service.KabupatenKotaService;
import com.kehutanan.superadmin.master.service.KecamatanService;
import com.kehutanan.superadmin.master.service.LovService;
import com.kehutanan.superadmin.master.model.Provinsi;
import com.kehutanan.superadmin.master.model.KabupatenKota;
import com.kehutanan.superadmin.master.model.Kecamatan;
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
@RequestMapping("/api/institusi")
public class InstitusiController {

    private final InstitusiService service;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<Institusi> pagedResourcesAssembler;

    @Autowired
    public InstitusiController(InstitusiService service,
                              ProvinsiService provinsiService,
                              KabupatenKotaService kabupatenKotaService,
                              KecamatanService kecamatanService,
                              LovService lovService,
                              PagedResourcesAssembler<Institusi> pagedResourcesAssembler) {
        this.service = service;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Institusi>>> getAllInstitusi(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long tipeInstitusiId,
            @RequestParam(required = false) Long provinsiId,
            @RequestParam(required = false) Long kabupatenKotaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Institusi> institusiPage;
        
        // Check if any filter is provided
        if ((nama != null && !nama.isEmpty()) || 
            (email != null && !email.isEmpty()) ||
            (tipeInstitusiId != null) ||
            (provinsiId != null) ||
            (kabupatenKotaId != null)) {
            institusiPage = service.findByFilters(nama, email, tipeInstitusiId, provinsiId, kabupatenKotaId, pageable);
        } else {
            institusiPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Institusi>> pagedModel = pagedResourcesAssembler.toModel(institusiPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institusi> getInstitusiById(@PathVariable Long id) {
        try {
            Institusi institusi = service.findById(id);
            return ResponseEntity.ok(institusi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Institusi> createInstitusi(
            @RequestPart String nama,
            @RequestPart(required = false) String email,
            @RequestPart(required = false) String nomorTelepon,
            @RequestPart(required = false) String website,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String kodePos,
            @RequestPart(required = false) String tipeInstitusiId,
            @RequestPart(required = false) String tipeAkreditasiId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId) {

        try {
            Institusi newInstitusi = new Institusi();
            newInstitusi.setNama(nama);
            newInstitusi.setEmail(email);
            newInstitusi.setNomorTelepon(nomorTelepon);
            newInstitusi.setWebsite(website);
            newInstitusi.setAlamat(alamat);
            newInstitusi.setKodePos(kodePos);
            
            if (tipeInstitusiId != null && !tipeInstitusiId.isEmpty()) {
                Lov tipeInstitusi = lovService.findById(Long.parseLong(tipeInstitusiId));
                newInstitusi.setTipeInstitusi(tipeInstitusi);
            }
            
            if (tipeAkreditasiId != null && !tipeAkreditasiId.isEmpty()) {
                Lov tipeAkreditasi = lovService.findById(Long.parseLong(tipeAkreditasiId));
                newInstitusi.setTipeAkreditasi(tipeAkreditasi);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                newInstitusi.setStatus(status);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Provinsi provinsi = provinsiService.findById(Long.parseLong(provinsiId));
                newInstitusi.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(Long.parseLong(kabupatenKotaId));
                newInstitusi.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Kecamatan kecamatan = kecamatanService.findById(Long.parseLong(kecamatanId));
                newInstitusi.setKecamatan(kecamatan);
            }

            Institusi savedInstitusi = service.save(newInstitusi);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInstitusi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Institusi> updateInstitusi(
            @PathVariable Long id,
            @RequestPart String nama,
            @RequestPart(required = false) String email,
            @RequestPart(required = false) String nomorTelepon,
            @RequestPart(required = false) String website,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String kodePos,
            @RequestPart(required = false) String tipeInstitusiId,
            @RequestPart(required = false) String tipeAkreditasiId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId) {

        try {
            Institusi existingInstitusi = service.findById(id);
            
            existingInstitusi.setNama(nama);
            existingInstitusi.setEmail(email);
            existingInstitusi.setNomorTelepon(nomorTelepon);
            existingInstitusi.setWebsite(website);
            existingInstitusi.setAlamat(alamat);
            existingInstitusi.setKodePos(kodePos);
            
            if (tipeInstitusiId != null && !tipeInstitusiId.isEmpty()) {
                Lov tipeInstitusi = lovService.findById(Long.parseLong(tipeInstitusiId));
                existingInstitusi.setTipeInstitusi(tipeInstitusi);
            }
            
            if (tipeAkreditasiId != null && !tipeAkreditasiId.isEmpty()) {
                Lov tipeAkreditasi = lovService.findById(Long.parseLong(tipeAkreditasiId));
                existingInstitusi.setTipeAkreditasi(tipeAkreditasi);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                existingInstitusi.setStatus(status);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Provinsi provinsi = provinsiService.findById(Long.parseLong(provinsiId));
                existingInstitusi.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(Long.parseLong(kabupatenKotaId));
                existingInstitusi.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Kecamatan kecamatan = kecamatanService.findById(Long.parseLong(kecamatanId));
                existingInstitusi.setKecamatan(kecamatan);
            }

            Institusi updatedInstitusi = service.update(id, existingInstitusi);
            return ResponseEntity.ok(updatedInstitusi);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstitusi(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}