package com.kehutanan.tktrh.ppkh.kegiatan.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanRiwayatSkPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRiwayatSk;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanRiwayatSkDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanRiwayatSkService;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController("ppkhKegiatanRiwayatSkController") // Add unique name
@RequestMapping("/api/ppkh/kegiatan-riwayat-sk")
public class KegiatanRiwayatSkController {

    private final KegiatanRiwayatSkService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;

    @Autowired
    public KegiatanRiwayatSkController(
            KegiatanRiwayatSkService service,
            KegiatanService kegiatanService,
            LovService lovService) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
    }

    @GetMapping
    public ResponseEntity<KegiatanRiwayatSkPageDTO> getAllKegiatanRiwayatSk(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String jenisPerubahan,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanRiwayatSkPageDTO kegiatanRiwayatSkPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if (kegiatanId != null || 
            (jenisPerubahan != null && !jenisPerubahan.isEmpty()) ||
            (keterangan != null && !keterangan.isEmpty()) ||
            (status != null && !status.isEmpty())) {
            kegiatanRiwayatSkPage = service.findByFiltersWithCache(
                kegiatanId, jenisPerubahan, keterangan, status, pageable, baseUrl);
        } else {
            kegiatanRiwayatSkPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanRiwayatSkPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanRiwayatSk by keyword")
    public ResponseEntity<KegiatanRiwayatSkPageDTO> searchKegiatanRiwayatSk(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanRiwayatSkPageDTO kegiatanRiwayatSkPage = service.searchWithCache(
            kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanRiwayatSkPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanRiwayatSkDTO> getKegiatanRiwayatSkById(@PathVariable Long id) {
        try {
            KegiatanRiwayatSkDTO kegiatanRiwayatSkDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanRiwayatSkDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanRiwayatSk> createKegiatanRiwayatSk(
            @RequestPart String kegiatanId,
            @RequestPart String jenisPerubahanId,
            @RequestPart(required = false) String nomorSk,
            @RequestPart(required = false) String tanggalSk,
            @RequestPart(required = false) String tanggalBerakhirSk,
            @RequestPart(required = false) String luasSkHa,
            @RequestPart(required = false) String keterangan,
            @RequestPart String statusId) {

        try {
            KegiatanRiwayatSk newKegiatanRiwayatSk = new KegiatanRiwayatSk();

            // Set kegiatan
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatanRiwayatSk.setKegiatan(kegiatan);
            }

            // Set jenis perubahan
            if (jenisPerubahanId != null && !jenisPerubahanId.isEmpty()) {
                Long jenisPerubahanIdLong = Long.parseLong(jenisPerubahanId);
                Lov jenisPerubahan = lovService.findById(jenisPerubahanIdLong);
                newKegiatanRiwayatSk.setJenisPerubahan(jenisPerubahan);
            }

            newKegiatanRiwayatSk.setNomorSk(nomorSk);

            // Set tanggal SK if provided
            if (tanggalSk != null && !tanggalSk.isEmpty()) {
                LocalDate parsedTanggalSk = LocalDate.parse(tanggalSk);
                newKegiatanRiwayatSk.setTanggalSk(parsedTanggalSk);
            }

            // Set tanggal berakhir SK if provided
            if (tanggalBerakhirSk != null && !tanggalBerakhirSk.isEmpty()) {
                LocalDate parsedTanggalBerakhirSk = LocalDate.parse(tanggalBerakhirSk);
                newKegiatanRiwayatSk.setTanggalBerakhirSk(parsedTanggalBerakhirSk);
            }

            // Set luas SK in hectares if provided
            if (luasSkHa != null && !luasSkHa.isEmpty()) {
                newKegiatanRiwayatSk.setLuasSkHa(Double.parseDouble(luasSkHa));
            }

            newKegiatanRiwayatSk.setKeterangan(keterangan);

            // Set status
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newKegiatanRiwayatSk.setStatus(status);
            }

            KegiatanRiwayatSk savedKegiatanRiwayatSk = service.save(newKegiatanRiwayatSk);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatanRiwayatSk);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanRiwayatSk> updateKegiatanRiwayatSk(
            @PathVariable Long id,
            @RequestPart String kegiatanId,
            @RequestPart String jenisPerubahanId,
            @RequestPart(required = false) String nomorSk,
            @RequestPart(required = false) String tanggalSk,
            @RequestPart(required = false) String tanggalBerakhirSk,
            @RequestPart(required = false) String luasSkHa,
            @RequestPart(required = false) String keterangan,
            @RequestPart String statusId) {

        try {
            KegiatanRiwayatSk existingKegiatanRiwayatSk = service.findById(id);

            // Set kegiatan
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatanRiwayatSk.setKegiatan(kegiatan);
            }

            // Set jenis perubahan
            if (jenisPerubahanId != null && !jenisPerubahanId.isEmpty()) {
                Long jenisPerubahanIdLong = Long.parseLong(jenisPerubahanId);
                Lov jenisPerubahan = lovService.findById(jenisPerubahanIdLong);
                existingKegiatanRiwayatSk.setJenisPerubahan(jenisPerubahan);
            }

            existingKegiatanRiwayatSk.setNomorSk(nomorSk);

            // Set tanggal SK if provided
            if (tanggalSk != null && !tanggalSk.isEmpty()) {
                LocalDate parsedTanggalSk = LocalDate.parse(tanggalSk);
                existingKegiatanRiwayatSk.setTanggalSk(parsedTanggalSk);
            }

            // Set tanggal berakhir SK if provided
            if (tanggalBerakhirSk != null && !tanggalBerakhirSk.isEmpty()) {
                LocalDate parsedTanggalBerakhirSk = LocalDate.parse(tanggalBerakhirSk);
                existingKegiatanRiwayatSk.setTanggalBerakhirSk(parsedTanggalBerakhirSk);
            }

            // Set luas SK in hectares if provided
            if (luasSkHa != null && !luasSkHa.isEmpty()) {
                existingKegiatanRiwayatSk.setLuasSkHa(Double.parseDouble(luasSkHa));
            }

            existingKegiatanRiwayatSk.setKeterangan(keterangan);

            // Set status
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingKegiatanRiwayatSk.setStatus(status);
            }

            KegiatanRiwayatSk updatedKegiatanRiwayatSk = service.update(id, existingKegiatanRiwayatSk);
            return ResponseEntity.ok(updatedKegiatanRiwayatSk);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanRiwayatSk(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/shp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload SHP files for KegiatanRiwayatSk")
    public ResponseEntity<?> uploadShpFiles(
            @PathVariable Long id,
            @RequestPart(value = "shp") List<MultipartFile> shpFiles) {
        try {
            KegiatanRiwayatSk kegiatanRiwayatSk = service.uploadKegiatanRiwayatSkShp(id, shpFiles);
            return ResponseEntity.ok(kegiatanRiwayatSk);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload SHP: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/shp")
    @Operation(summary = "Delete SHP files for KegiatanRiwayatSk")
    public ResponseEntity<?> deleteShpFiles(
            @PathVariable Long id,
            @RequestParam List<String> uuidShp) {
        try {
            KegiatanRiwayatSk kegiatanRiwayatSk = service.deleteKegiatanRiwayatSkShp(id, uuidShp);
            return ResponseEntity.ok(kegiatanRiwayatSk);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus SHP: " + e.getMessage());
        }
    }
}