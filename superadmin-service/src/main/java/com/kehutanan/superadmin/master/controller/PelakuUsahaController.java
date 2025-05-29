package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.PelakuUsahaService;
import com.kehutanan.superadmin.master.model.PelakuUsaha;
import com.kehutanan.superadmin.master.service.ProvinsiService;
import com.kehutanan.superadmin.master.model.Provinsi;
import com.kehutanan.superadmin.master.service.KabupatenKotaService;
import com.kehutanan.superadmin.master.model.KabupatenKota;
import com.kehutanan.superadmin.master.service.KecamatanService;
import com.kehutanan.superadmin.master.model.Kecamatan;
import com.kehutanan.superadmin.master.service.KelurahanDesaService;
import com.kehutanan.superadmin.master.model.KelurahanDesa;
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
@RequestMapping("/api/pelaku-usaha")
public class PelakuUsahaController {

    private final PelakuUsahaService service;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final KelurahanDesaService kelurahanDesaService;
    private final LovService lovService;
    private final PagedResourcesAssembler<PelakuUsaha> pagedResourcesAssembler;

    @Autowired
    public PelakuUsahaController(
            PelakuUsahaService service,
            ProvinsiService provinsiService,
            KabupatenKotaService kabupatenKotaService,
            KecamatanService kecamatanService,
            KelurahanDesaService kelurahanDesaService,
            LovService lovService,
            PagedResourcesAssembler<PelakuUsaha> pagedResourcesAssembler) {
        this.service = service;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.kelurahanDesaService = kelurahanDesaService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PelakuUsaha>>> getAllPelakuUsaha(
            @RequestParam(required = false) String namaBadanUsaha,
            @RequestParam(required = false) String nomorIndukBerusahaNib,
            @RequestParam(required = false) String ruangLingkupUsaha,
            @RequestParam(required = false) String namaDirektur,
            @RequestParam(required = false) Long kategoriPelakuUsahaId,
            @RequestParam(required = false) Long provinsiId,
            @RequestParam(required = false) Long kabupatenKotaId,
            @RequestParam(required = false) Long kecamatanId,
            @RequestParam(required = false) Long kelurahanDesaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PelakuUsaha> pelakuUsahaPage;
        
        // Check if any filter is provided
        if (namaBadanUsaha != null || 
            nomorIndukBerusahaNib != null || 
            ruangLingkupUsaha != null ||
            namaDirektur != null ||
            kategoriPelakuUsahaId != null ||
            provinsiId != null || 
            kabupatenKotaId != null ||
            kecamatanId != null ||
            kelurahanDesaId != null) {
            
            pelakuUsahaPage = service.findByFilters(
                namaBadanUsaha, 
                nomorIndukBerusahaNib, 
                ruangLingkupUsaha, 
                namaDirektur,
                kategoriPelakuUsahaId,
                provinsiId, 
                kabupatenKotaId, 
                kecamatanId, 
                kelurahanDesaId,
                pageable);
        } else {
            pelakuUsahaPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<PelakuUsaha>> pagedModel = pagedResourcesAssembler.toModel(pelakuUsahaPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PelakuUsaha> getPelakuUsahaById(@PathVariable Long id) {
        try {
            PelakuUsaha pelakuUsaha = service.findById(id);
            return ResponseEntity.ok(pelakuUsaha);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PelakuUsaha> createPelakuUsaha(
            @RequestPart String namaBadanUsaha,
            @RequestPart(required = false) String nomorIndukBerusahaNib,
            @RequestPart(required = false) String nomorSertifikatStandar,
            @RequestPart(required = false) String ruangLingkupUsaha,
            @RequestPart(required = false) String namaDirektur,
            @RequestPart(required = false) String nomorHpDirektur,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String kategoriPelakuUsahaId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            PelakuUsaha newPelakuUsaha = new PelakuUsaha();
            newPelakuUsaha.setNamaBadanUsaha(namaBadanUsaha);
            newPelakuUsaha.setNomorIndukBerusahaNib(nomorIndukBerusahaNib);
            newPelakuUsaha.setNomorSertifikatStandar(nomorSertifikatStandar);
            newPelakuUsaha.setRuangLingkupUsaha(ruangLingkupUsaha);
            newPelakuUsaha.setNamaDirektur(namaDirektur);
            newPelakuUsaha.setNomorHpDirektur(nomorHpDirektur);
            newPelakuUsaha.setAlamat(alamat);
            
            if (kategoriPelakuUsahaId != null && !kategoriPelakuUsahaId.isEmpty()) {
                Long lovId = Long.parseLong(kategoriPelakuUsahaId);
                Lov kategoriPelakuUsaha = lovService.findById(lovId);
                newPelakuUsaha.setKategoriPelakuUsaha(kategoriPelakuUsaha);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provId = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provId);
                newPelakuUsaha.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabKotaId = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabKotaId);
                newPelakuUsaha.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecId = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecId);
                newPelakuUsaha.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelDesaId = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelDesaId);
                newPelakuUsaha.setKelurahanDesa(kelurahanDesa);
            }

            PelakuUsaha savedPelakuUsaha = service.save(newPelakuUsaha);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPelakuUsaha);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PelakuUsaha> updatePelakuUsaha(
            @PathVariable Long id,
            @RequestPart String namaBadanUsaha,
            @RequestPart(required = false) String nomorIndukBerusahaNib,
            @RequestPart(required = false) String nomorSertifikatStandar,
            @RequestPart(required = false) String ruangLingkupUsaha,
            @RequestPart(required = false) String namaDirektur,
            @RequestPart(required = false) String nomorHpDirektur,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String kategoriPelakuUsahaId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            PelakuUsaha existingPelakuUsaha = service.findById(id);
            
            // Update the existing pelakuUsaha with new values
            existingPelakuUsaha.setNamaBadanUsaha(namaBadanUsaha);
            existingPelakuUsaha.setNomorIndukBerusahaNib(nomorIndukBerusahaNib);
            existingPelakuUsaha.setNomorSertifikatStandar(nomorSertifikatStandar);
            existingPelakuUsaha.setRuangLingkupUsaha(ruangLingkupUsaha);
            existingPelakuUsaha.setNamaDirektur(namaDirektur);
            existingPelakuUsaha.setNomorHpDirektur(nomorHpDirektur);
            existingPelakuUsaha.setAlamat(alamat);
            
            if (kategoriPelakuUsahaId != null && !kategoriPelakuUsahaId.isEmpty()) {
                Long lovId = Long.parseLong(kategoriPelakuUsahaId);
                Lov kategoriPelakuUsaha = lovService.findById(lovId);
                existingPelakuUsaha.setKategoriPelakuUsaha(kategoriPelakuUsaha);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provId = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provId);
                existingPelakuUsaha.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabKotaId = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabKotaId);
                existingPelakuUsaha.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecId = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecId);
                existingPelakuUsaha.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelDesaId = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelDesaId);
                existingPelakuUsaha.setKelurahanDesa(kelurahanDesa);
            }

            PelakuUsaha updatedPelakuUsaha = service.update(id, existingPelakuUsaha);
            return ResponseEntity.ok(updatedPelakuUsaha);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePelakuUsaha(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}