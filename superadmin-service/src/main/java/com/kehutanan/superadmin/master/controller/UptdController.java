package com.kehutanan.superadmin.master.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.superadmin.master.dto.UptdDTO;
import com.kehutanan.superadmin.master.dto.UptdDeleteFilesRequest;
import com.kehutanan.superadmin.master.model.Bpdas;
import com.kehutanan.superadmin.master.model.KabupatenKota;
import com.kehutanan.superadmin.master.model.Kecamatan;
import com.kehutanan.superadmin.master.model.KelurahanDesa;
import com.kehutanan.superadmin.master.model.Provinsi;
import com.kehutanan.superadmin.master.model.Uptd;
import com.kehutanan.superadmin.master.service.BpdasService;
import com.kehutanan.superadmin.master.service.KabupatenKotaService;
import com.kehutanan.superadmin.master.service.KecamatanService;
import com.kehutanan.superadmin.master.service.KelurahanDesaService;
import com.kehutanan.superadmin.master.service.ProvinsiService;
import com.kehutanan.superadmin.master.service.UptdService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/uptd")
public class UptdController {

    private final UptdService service;
    private final BpdasService bpdasService;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final KelurahanDesaService kelurahanDesaService;
    private final PagedResourcesAssembler<Uptd> pagedResourcesAssembler;

    @Autowired
    public UptdController(
            UptdService service,
            BpdasService bpdasService,
            ProvinsiService provinsiService,
            KabupatenKotaService kabupatenKotaService,
            KecamatanService kecamatanService,
            KelurahanDesaService kelurahanDesaService,
            PagedResourcesAssembler<Uptd> pagedResourcesAssembler) {
        this.service = service;
        this.bpdasService = bpdasService;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.kelurahanDesaService = kelurahanDesaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Uptd>>> getAllUptd(
            @RequestParam(required = false) String namaUptd,
            @RequestParam(required = false) Long bpdasId,
            @RequestParam(required = false) Long provinsiId,
            @RequestParam(required = false) Long kabupatenKotaId,
            @RequestParam(required = false) Long kecamatanId,
            @RequestParam(required = false) Long kelurahanDesaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Uptd> uptdPage;

        // Check if any filter is provided
        if ((namaUptd != null && !namaUptd.isEmpty()) ||
                (bpdasId != null) ||
                (provinsiId != null) ||
                (kabupatenKotaId != null) ||
                (kecamatanId != null) ||
                (kelurahanDesaId != null)) {
            uptdPage = service.findByFilters(namaUptd, bpdasId, provinsiId, kabupatenKotaId, kecamatanId,
                    kelurahanDesaId, pageable);
        } else {
            uptdPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Uptd>> pagedModel = pagedResourcesAssembler.toModel(uptdPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UptdDTO> getUptdById(@PathVariable Long id) {
        try {
            UptdDTO uptdTDto = service.findDTOById(id);
            return ResponseEntity.ok(uptdTDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Uptd> createUptd(
            @RequestPart String namaUptd,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String telepon,
            @RequestPart(required = false) String sertifikasiSumberBenih,
            @RequestPart(required = false) String sertifikasiMutuBenih,
            @RequestPart(required = false) String sertifikasiMutuBibit,
            @RequestPart(required = false) String jumlahAsesorSumberBenih,
            @RequestPart(required = false) String jumlahAsesorMutuBenih,
            @RequestPart(required = false) String jumlahAsesorMutuBibit,
            @RequestPart(required = false) String nomorSkPenetapan,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String namaKontak,
            @RequestPart(required = false) String nomorTeleponKontak,
            @RequestPart(required = false) String catatanDokumen,
            @RequestPart(required = false) String catatan,
            @RequestPart(required = false) String lintang,
            @RequestPart(required = false) String bujur,
            @RequestPart(required = false) String luas,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            Uptd newUptd = new Uptd();
            newUptd.setNamaUptd(namaUptd);
            newUptd.setAlamat(alamat);
            newUptd.setTelepon(telepon);

            if (sertifikasiSumberBenih != null) {
                newUptd.setSertifikasiSumberBenih(Boolean.parseBoolean(sertifikasiSumberBenih));
            }

            if (sertifikasiMutuBenih != null) {
                newUptd.setSertifikasiMutuBenih(Boolean.parseBoolean(sertifikasiMutuBenih));
            }

            if (sertifikasiMutuBibit != null) {
                newUptd.setSertifikasiMutuBibit(Boolean.parseBoolean(sertifikasiMutuBibit));
            }

            if (jumlahAsesorSumberBenih != null && !jumlahAsesorSumberBenih.isEmpty()) {
                newUptd.setJumlahAsesorSumberBenih(Integer.parseInt(jumlahAsesorSumberBenih));
            }

            if (jumlahAsesorMutuBenih != null && !jumlahAsesorMutuBenih.isEmpty()) {
                newUptd.setJumlahAsesorMutuBenih(Integer.parseInt(jumlahAsesorMutuBenih));
            }

            if (jumlahAsesorMutuBibit != null && !jumlahAsesorMutuBibit.isEmpty()) {
                newUptd.setJumlahAsesorMutuBibit(Integer.parseInt(jumlahAsesorMutuBibit));
            }

            newUptd.setNomorSkPenetapan(nomorSkPenetapan);

            if (tanggal != null && !tanggal.isEmpty()) {
                // Assuming tanggal is in ISO format (yyyy-MM-ddTHH:mm:ss)
                newUptd.setTanggal(LocalDateTime.parse(tanggal));
            }

            newUptd.setNamaKontak(namaKontak);
            newUptd.setNomorTeleponKontak(nomorTeleponKontak);
            newUptd.setCatatanDokumen(catatanDokumen);
            newUptd.setCatatan(catatan);

            if (lintang != null && !lintang.isEmpty()) {
                BigDecimal lintangValue = new BigDecimal(lintang);
                // Latitude should be between -90 and 90
                if (lintangValue.compareTo(new BigDecimal("-90")) < 0
                        || lintangValue.compareTo(new BigDecimal("90")) > 0) {
                    throw new IllegalArgumentException("Latitude must be between -90 and 90");
                }
                newUptd.setLintang(lintangValue);
            }

            if (bujur != null && !bujur.isEmpty()) {
                BigDecimal bujurValue = new BigDecimal(bujur);
                // Longitude should be between -180 and 180
                if (bujurValue.compareTo(new BigDecimal("-180")) < 0
                        || bujurValue.compareTo(new BigDecimal("180")) > 0) {
                    throw new IllegalArgumentException("Longitude must be between -180 and 180");
                }
                newUptd.setBujur(bujurValue);
            }

            if (luas != null && !luas.isEmpty()) {
                // Add validation for area if needed
                newUptd.setLuas(new BigDecimal(luas));
            }

            // Set relations if IDs are provided
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                newUptd.setBpdas(bpdas);
            }

            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                newUptd.setProvinsi(provinsi);
            }

            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                newUptd.setKabupatenKota(kabupatenKota);
            }

            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                newUptd.setKecamatan(kecamatan);
            }

            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                newUptd.setKelurahanDesa(kelurahanDesa);
            }

            Uptd savedUptd = service.save(newUptd);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUptd);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Add detailed logging for troubleshooting
            e.printStackTrace(); // Log stack trace to console

            // Return more specific error details in response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Consider sending a structured error response instead of null
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Uptd> updateUptd(
            @PathVariable Long id,
            @RequestPart String namaUptd,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String telepon,
            @RequestPart(required = false) String sertifikasiSumberBenih,
            @RequestPart(required = false) String sertifikasiMutuBenih,
            @RequestPart(required = false) String sertifikasiMutuBibit,
            @RequestPart(required = false) String jumlahAsesorSumberBenih,
            @RequestPart(required = false) String jumlahAsesorMutuBenih,
            @RequestPart(required = false) String jumlahAsesorMutuBibit,
            @RequestPart(required = false) String nomorSkPenetapan,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String namaKontak,
            @RequestPart(required = false) String nomorTeleponKontak,
            @RequestPart(required = false) String catatanDokumen,
            @RequestPart(required = false) String catatan,
            @RequestPart(required = false) String lintang,
            @RequestPart(required = false) String bujur,
            @RequestPart(required = false) String luas,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId) {

        try {
            Uptd existingUptd = service.findById(id);

            existingUptd.setNamaUptd(namaUptd);
            existingUptd.setAlamat(alamat);
            existingUptd.setTelepon(telepon);

            if (sertifikasiSumberBenih != null) {
                existingUptd.setSertifikasiSumberBenih(Boolean.parseBoolean(sertifikasiSumberBenih));
            }

            if (sertifikasiMutuBenih != null) {
                existingUptd.setSertifikasiMutuBenih(Boolean.parseBoolean(sertifikasiMutuBenih));
            }

            if (sertifikasiMutuBibit != null) {
                existingUptd.setSertifikasiMutuBibit(Boolean.parseBoolean(sertifikasiMutuBibit));
            }

            if (jumlahAsesorSumberBenih != null && !jumlahAsesorSumberBenih.isEmpty()) {
                existingUptd.setJumlahAsesorSumberBenih(Integer.parseInt(jumlahAsesorSumberBenih));
            }

            if (jumlahAsesorMutuBenih != null && !jumlahAsesorMutuBenih.isEmpty()) {
                existingUptd.setJumlahAsesorMutuBenih(Integer.parseInt(jumlahAsesorMutuBenih));
            }

            if (jumlahAsesorMutuBibit != null && !jumlahAsesorMutuBibit.isEmpty()) {
                existingUptd.setJumlahAsesorMutuBibit(Integer.parseInt(jumlahAsesorMutuBibit));
            }

            existingUptd.setNomorSkPenetapan(nomorSkPenetapan);

            if (tanggal != null && !tanggal.isEmpty()) {
                existingUptd.setTanggal(LocalDateTime.parse(tanggal));
            }

            existingUptd.setNamaKontak(namaKontak);
            existingUptd.setNomorTeleponKontak(nomorTeleponKontak);
            existingUptd.setCatatanDokumen(catatanDokumen);
            existingUptd.setCatatan(catatan);

            if (lintang != null && !lintang.isEmpty()) {
                BigDecimal lintangValue = new BigDecimal(lintang);
                // Latitude should be between -90 and 90
                if (lintangValue.compareTo(new BigDecimal("-90")) < 0
                        || lintangValue.compareTo(new BigDecimal("90")) > 0) {
                    throw new IllegalArgumentException("Latitude must be between -90 and 90");
                }
                existingUptd.setLintang(lintangValue);
            }

            if (bujur != null && !bujur.isEmpty()) {
                BigDecimal bujurValue = new BigDecimal(bujur);
                // Longitude should be between -180 and 180
                if (bujurValue.compareTo(new BigDecimal("-180")) < 0
                        || bujurValue.compareTo(new BigDecimal("180")) > 0) {
                    throw new IllegalArgumentException("Longitude must be between -180 and 180");
                }
                existingUptd.setBujur(bujurValue);
            }

            if (luas != null && !luas.isEmpty()) {
                // Add validation for area if needed
                existingUptd.setLuas(new BigDecimal(luas));
            }

            // Update relations if IDs are provided
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                existingUptd.setBpdas(bpdas);
            }

            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                existingUptd.setProvinsi(provinsi);
            }

            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                existingUptd.setKabupatenKota(kabupatenKota);
            }

            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                existingUptd.setKecamatan(kecamatan);
            }

            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                existingUptd.setKelurahanDesa(kelurahanDesa);
            }

            Uptd updatedUptd = service.update(id, existingUptd);
            return ResponseEntity.ok(updatedUptd);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUptd(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple File for Uptd")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "rantekPdfs", required = false) List<MultipartFile> rantekPdfs,
            @RequestPart(value = "dokumentasiFotos", required = false) List<MultipartFile> dokumentasiFotos,
            @RequestPart(value = "dokumentasiVideos", required = false) List<MultipartFile> dokumentasiVideos,
            @RequestPart(value = "lokasiMapShp", required = false) List<MultipartFile> lokasiMapShp) {
        try {
            if (rantekPdfs != null) {
                service.uploadUptdPdf(id, rantekPdfs);
            }
            if (dokumentasiFotos != null) {
                service.uploadUptdFoto(id, dokumentasiFotos);
            }
            if (dokumentasiVideos != null) {
                service.uploadUptdVideo(id, dokumentasiVideos);
            }
            if (lokasiMapShp != null) {
                service.uploadUptdShp(id, lokasiMapShp);
            }

            Uptd uptd = service.findById(id);
            return ResponseEntity.ok(uptd);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload foto: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete multiple files for Uptd")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody(required = false) UptdDeleteFilesRequest filesRequest) {
        try {
            // Handle each file type list if provided
            if (filesRequest.getRantekPdfIds() != null && !filesRequest.getRantekPdfIds().isEmpty()) {
                service.deleteUptdPdf(id, filesRequest.getRantekPdfIds());
            }

            if (filesRequest.getDokumentasiFotoIds() != null && !filesRequest.getDokumentasiFotoIds().isEmpty()) {
                service.deleteUptdFoto(id, filesRequest.getDokumentasiFotoIds());
            }

            if (filesRequest.getDokumentasiVideoIds() != null && !filesRequest.getDokumentasiVideoIds().isEmpty()) {
                service.deleteUptdVideo(id, filesRequest.getDokumentasiVideoIds());
            }

            if (filesRequest.getLokasiMapShpIds() != null && !filesRequest.getLokasiMapShpIds().isEmpty()) {
                service.deleteUptdShp(id, filesRequest.getLokasiMapShpIds());
            }

            // Fetch and return the updated Uptd entity
            Uptd uptd = service.findById(id);
            return ResponseEntity.ok(uptd);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }

}