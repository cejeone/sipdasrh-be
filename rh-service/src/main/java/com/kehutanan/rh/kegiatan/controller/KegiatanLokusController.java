package com.kehutanan.rh.kegiatan.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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

import com.kehutanan.rh.kegiatan.dto.KegiatanLokusDTO;
import com.kehutanan.rh.kegiatan.dto.KegiatanLokusDeleteFilesRequest;
import com.kehutanan.rh.kegiatan.dto.KegiatanLokusPageDTO;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanLokus;
import com.kehutanan.rh.kegiatan.service.KegiatanLokusService;
import com.kehutanan.rh.kegiatan.service.KegiatanService;
import com.kehutanan.rh.master.model.KabupatenKota;
import com.kehutanan.rh.master.model.Kecamatan;
import com.kehutanan.rh.master.model.KelurahanDesa;
import com.kehutanan.rh.master.model.Provinsi;
import com.kehutanan.rh.master.service.KabupatenKotaService;
import com.kehutanan.rh.master.service.KecamatanService;
import com.kehutanan.rh.master.service.KelurahanDesaService;
import com.kehutanan.rh.master.service.ProvinsiService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-locus")
public class KegiatanLokusController {

    private final KegiatanLokusService service;
    private final KegiatanService kegiatanService;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final KelurahanDesaService kelurahanDesaService;
    private final PagedResourcesAssembler<KegiatanLokus> pagedResourcesAssembler;

    @Autowired
    public KegiatanLokusController(
            KegiatanLokusService service,
            KegiatanService kegiatanService,
            ProvinsiService provinsiService,
            KabupatenKotaService kabupatenKotaService,
            KecamatanService kecamatanService,
            KelurahanDesaService kelurahanDesaService,
            PagedResourcesAssembler<KegiatanLokus> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.kelurahanDesaService = kelurahanDesaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanLokusPageDTO> getAllKegiatanLokus(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> provinsi,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanLokusPageDTO kegiatanLokusPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) || 
            (keterangan != null && !keterangan.isEmpty()) || 
            (provinsi != null && !provinsi.isEmpty())) {
            kegiatanLokusPage = service.findByFiltersWithCache(kegiatanId, keterangan, provinsi, pageable, baseUrl);
        } else {
            kegiatanLokusPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanLokusPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanLokus by keyword")
    public ResponseEntity<KegiatanLokusPageDTO> searchKegiatanLokus(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanLokusPageDTO kegiatanLokusPage = service.searchWithCache(kegiatanId, keyWord, pageable, 
                request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanLokusPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanLokusDTO> getKegiatanLokusById(@PathVariable Long id) {
        try {
            KegiatanLokusDTO kegiatanLokusDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanLokusDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanLokus> createKegiatanLokus(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId,
            @RequestPart(required = false) String alamat
            ) {

        try {
            KegiatanLokus newKegiatanLokus = new KegiatanLokus();

            // Set relations if IDs are provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatanLokus.setKegiatan(kegiatan);
            }

            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                newKegiatanLokus.setProvinsi(provinsi);
            }

            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                newKegiatanLokus.setKabupatenKota(kabupatenKota);
            }

            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                newKegiatanLokus.setKecamatan(kecamatan);
            }

            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                newKegiatanLokus.setKelurahanDesa(kelurahanDesa);
            }

            if (alamat != null && !alamat.isEmpty()) {
                newKegiatanLokus.setAlamat(alamat);
            }



            KegiatanLokus savedKegiatanLokus = service.save(newKegiatanLokus);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanLokus> updateKegiatanLokus(
            @PathVariable Long id,
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId,
            @RequestPart(required = false) String alamat) {

        try {
            KegiatanLokus existingKegiatanLokus = service.findById(id);


            // Update relations if IDs are provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatanLokus.setKegiatan(kegiatan);
            }

            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                existingKegiatanLokus.setProvinsi(provinsi);
            }

            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                existingKegiatanLokus.setKabupatenKota(kabupatenKota);
            }

            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                existingKegiatanLokus.setKecamatan(kecamatan);
            }

            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                existingKegiatanLokus.setKelurahanDesa(kelurahanDesa);
            }
            if (alamat != null && !alamat.isEmpty()) {
                existingKegiatanLokus.setAlamat(alamat);
            }

            KegiatanLokus updatedKegiatanLokus = service.update(id, existingKegiatanLokus);
            return ResponseEntity.ok(updatedKegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanLokus(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/shp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload SHP files for KegiatanLokus")
    public ResponseEntity<?> uploadShpFiles(
            @PathVariable Long id,
            @RequestPart(value = "shpFiles") List<MultipartFile> shpFiles) {
        try {
            KegiatanLokus updatedKegiatanLokus = service.uploadKegiatanLokusShp(id, shpFiles);
            return ResponseEntity.ok(updatedKegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload SHP files: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/shp")
    @Operation(summary = "Delete SHP files for KegiatanLokus")
    public ResponseEntity<?> deleteShpFiles(
            @PathVariable Long id,
            @RequestBody KegiatanLokusDeleteFilesRequest filesRequest) {
        try {
            if (filesRequest.getLokusShpIds() != null && !filesRequest.getLokusShpIds().isEmpty()) {
                KegiatanLokus updatedKegiatanLokus = service.deleteKegiatanLokusShp(id, filesRequest.getLokusShpIds());
                return ResponseEntity.ok(updatedKegiatanLokus);
            } else {
                return ResponseEntity.badRequest().body("No SHP file IDs provided for deletion");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete SHP files: " + e.getMessage());
        }
    }
}