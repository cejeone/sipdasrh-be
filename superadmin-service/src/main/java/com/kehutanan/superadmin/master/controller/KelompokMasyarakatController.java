package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.KelompokMasyarakatService;
import com.kehutanan.superadmin.master.model.KelompokMasyarakat;
import com.kehutanan.superadmin.master.service.ProvinsiService;
import com.kehutanan.superadmin.master.service.KabupatenKotaService;
import com.kehutanan.superadmin.master.service.KecamatanService;
import com.kehutanan.superadmin.master.service.KelurahanDesaService;
import com.kehutanan.superadmin.master.model.Provinsi;
import com.kehutanan.superadmin.master.model.KabupatenKota;
import com.kehutanan.superadmin.master.model.Kecamatan;
import com.kehutanan.superadmin.master.model.KelurahanDesa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PagedResourcesAssembler;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/kelompok-masyarakat")
public class KelompokMasyarakatController {

    private final KelompokMasyarakatService service;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final KelurahanDesaService kelurahanDesaService;
    private final PagedResourcesAssembler<KelompokMasyarakat> pagedResourcesAssembler;

    @Autowired
    public KelompokMasyarakatController(KelompokMasyarakatService service,
                                       ProvinsiService provinsiService,
                                       KabupatenKotaService kabupatenKotaService,
                                       KecamatanService kecamatanService,
                                       KelurahanDesaService kelurahanDesaService,
                                       PagedResourcesAssembler<KelompokMasyarakat> pagedResourcesAssembler) {
        this.service = service;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.kelurahanDesaService = kelurahanDesaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<KelompokMasyarakat>>> getAllKelompokMasyarakat(
            @RequestParam(required = false) String namaKelompokMasyarakat,
            @RequestParam(required = false) String nomorSkPenetapan,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggalSkPenetapan,
            @RequestParam(required = false) Long provinsiId,
            @RequestParam(required = false) Long kabupatenKotaId,
            @RequestParam(required = false) Long kecamatanId,
            @RequestParam(required = false) Long kelurahanDesaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KelompokMasyarakat> kelompokMasyarakatPage;
        
        // Check if any filter is provided
        if ((namaKelompokMasyarakat != null && !namaKelompokMasyarakat.isEmpty()) || 
            (nomorSkPenetapan != null && !nomorSkPenetapan.isEmpty()) ||
            tanggalSkPenetapan != null ||
            provinsiId != null ||
            kabupatenKotaId != null ||
            kecamatanId != null ||
            kelurahanDesaId != null) {
            kelompokMasyarakatPage = service.findByFilters(
                namaKelompokMasyarakat, 
                nomorSkPenetapan, 
                tanggalSkPenetapan,
                provinsiId,
                kabupatenKotaId,
                kecamatanId,
                kelurahanDesaId,
                pageable
            );
        } else {
            kelompokMasyarakatPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<KelompokMasyarakat>> pagedModel = pagedResourcesAssembler.toModel(kelompokMasyarakatPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KelompokMasyarakat> getKelompokMasyarakatById(@PathVariable Long id) {
        try {
            KelompokMasyarakat kelompokMasyarakat = service.findById(id);
            return ResponseEntity.ok(kelompokMasyarakat);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KelompokMasyarakat> createKelompokMasyarakat(
            @RequestPart String namaKelompokMasyarakat,
            @RequestPart(required = false) String nomorSkPenetapan,
            @RequestPart(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String tanggalSkPenetapan,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String pic,
            @RequestPart(required = false) String telepon,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            KelompokMasyarakat newKelompokMasyarakat = new KelompokMasyarakat();
            newKelompokMasyarakat.setNamaKelompokMasyarakat(namaKelompokMasyarakat);
            newKelompokMasyarakat.setNomorSkPenetapan(nomorSkPenetapan);
            
            if (tanggalSkPenetapan != null && !tanggalSkPenetapan.isEmpty()) {
                newKelompokMasyarakat.setTanggalSkPenetapan(LocalDate.parse(tanggalSkPenetapan));
            }
            
            newKelompokMasyarakat.setAlamat(alamat);
            newKelompokMasyarakat.setPic(pic);
            newKelompokMasyarakat.setTelepon(telepon);
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                newKelompokMasyarakat.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                newKelompokMasyarakat.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                newKelompokMasyarakat.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                newKelompokMasyarakat.setKelurahanDesa(kelurahanDesa);
            }

            KelompokMasyarakat savedKelompokMasyarakat = service.save(newKelompokMasyarakat);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKelompokMasyarakat);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KelompokMasyarakat> updateKelompokMasyarakat(
            @PathVariable Long id,
            @RequestPart String namaKelompokMasyarakat,
            @RequestPart(required = false) String nomorSkPenetapan,
            @RequestPart(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String tanggalSkPenetapan,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String pic,
            @RequestPart(required = false) String telepon,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            KelompokMasyarakat existingKelompokMasyarakat = service.findById(id);
            
            // Update the existing kelompokMasyarakat with new values
            existingKelompokMasyarakat.setNamaKelompokMasyarakat(namaKelompokMasyarakat);
            existingKelompokMasyarakat.setNomorSkPenetapan(nomorSkPenetapan);
            
            if (tanggalSkPenetapan != null && !tanggalSkPenetapan.isEmpty()) {
                existingKelompokMasyarakat.setTanggalSkPenetapan(LocalDate.parse(tanggalSkPenetapan));
            }
            
            existingKelompokMasyarakat.setAlamat(alamat);
            existingKelompokMasyarakat.setPic(pic);
            existingKelompokMasyarakat.setTelepon(telepon);
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                existingKelompokMasyarakat.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                existingKelompokMasyarakat.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                existingKelompokMasyarakat.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                existingKelompokMasyarakat.setKelurahanDesa(kelurahanDesa);
            }

            KelompokMasyarakat updatedKelompokMasyarakat = service.update(id, existingKelompokMasyarakat);
            return ResponseEntity.ok(updatedKelompokMasyarakat);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKelompokMasyarakat(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}