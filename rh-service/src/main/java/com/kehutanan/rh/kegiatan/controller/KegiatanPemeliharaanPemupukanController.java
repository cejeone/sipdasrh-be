package com.kehutanan.rh.kegiatan.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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

import com.kehutanan.rh.kegiatan.dto.KegiatanPemeliharaanPemupukanDTO;
import com.kehutanan.rh.kegiatan.dto.KegiatanPemeliharaanPemupukanPageDTO;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanPemupukan;
import com.kehutanan.rh.kegiatan.service.KegiatanPemeliharaanPemupukanService;
import com.kehutanan.rh.kegiatan.service.KegiatanService;
import com.kehutanan.rh.master.model.Lov;
import com.kehutanan.rh.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-pemeliharaan-pemupukan")
public class KegiatanPemeliharaanPemupukanController {

    private final KegiatanPemeliharaanPemupukanService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanPemeliharaanPemupukan> pagedResourcesAssembler;

    @Autowired
    public KegiatanPemeliharaanPemupukanController(
            KegiatanPemeliharaanPemupukanService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanPemeliharaanPemupukan> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanPemeliharaanPemupukanPageDTO> getAllPemupukan(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> jenis,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPemeliharaanPemupukanPageDTO pemupukanPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) || 
            (keterangan != null && !keterangan.isEmpty()) || 
            (jenis != null && !jenis.isEmpty())) {
            pemupukanPage = service.findByFiltersWithCache(kegiatanId, keterangan, jenis, pageable, baseUrl);
        } else {
            pemupukanPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(pemupukanPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan Pemeliharaan Pemupukan")
    public ResponseEntity<KegiatanPemeliharaanPemupukanPageDTO> searchPemupukan(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPemeliharaanPemupukanPageDTO pemupukanPage = service.searchWithCache(
                kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(pemupukanPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanPemeliharaanPemupukanDTO> getPemupukanById(@PathVariable Long id) {
        try {
            KegiatanPemeliharaanPemupukanDTO pemupukanDTO = service.findDTOById(id);
            return ResponseEntity.ok(pemupukanDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanPemeliharaanPemupukan> createPemupukan(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = false) String jenisId,
            @RequestPart(required = false) String satuanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String waktuPemupukan,
            @RequestPart(required = false) String jumlahPupuk,
            @RequestPart(required = false) String keterangan) {

        try {
            KegiatanPemeliharaanPemupukan newPemupukan = new KegiatanPemeliharaanPemupukan();
            
            // Set related entities
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newPemupukan.setKegiatan(kegiatan);
            }

            if (jenisId != null && !jenisId.isEmpty()) {
                Long jenisIdLong = Long.parseLong(jenisId);
                Lov jenis = lovService.findById(jenisIdLong);
                newPemupukan.setJenisId(jenis);
            }

            if (satuanId != null && !satuanId.isEmpty()) {
                Long satuanIdLong = Long.parseLong(satuanId);
                Lov satuan = lovService.findById(satuanIdLong);
                newPemupukan.setSatuanId(satuan);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newPemupukan.setStatusId(status);
            }

            // Set other fields
            if (waktuPemupukan != null && !waktuPemupukan.isEmpty()) {
                newPemupukan.setWaktuPemupukan(LocalDate.parse(waktuPemupukan));
            }
            
            newPemupukan.setJumlahPupuk(jumlahPupuk);
            newPemupukan.setKeterangan(keterangan);

            KegiatanPemeliharaanPemupukan savedPemupukan = service.save(newPemupukan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPemupukan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanPemeliharaanPemupukan> updatePemupukan(
            @PathVariable Long id,
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = false) String jenisId,
            @RequestPart(required = false) String satuanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String waktuPemupukan,
            @RequestPart(required = false) String jumlahPupuk,
            @RequestPart(required = false) String keterangan) {

        try {
            KegiatanPemeliharaanPemupukan existingPemupukan = service.findById(id);
            
            // Update related entities
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingPemupukan.setKegiatan(kegiatan);
            }

            if (jenisId != null && !jenisId.isEmpty()) {
                Long jenisIdLong = Long.parseLong(jenisId);
                Lov jenis = lovService.findById(jenisIdLong);
                existingPemupukan.setJenisId(jenis);
            }

            if (satuanId != null && !satuanId.isEmpty()) {
                Long satuanIdLong = Long.parseLong(satuanId);
                Lov satuan = lovService.findById(satuanIdLong);
                existingPemupukan.setSatuanId(satuan);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingPemupukan.setStatusId(status);
            }

            // Update other fields
            if (waktuPemupukan != null && !waktuPemupukan.isEmpty()) {
                existingPemupukan.setWaktuPemupukan(LocalDate.parse(waktuPemupukan));
            }
            
            if (jumlahPupuk != null) {
                existingPemupukan.setJumlahPupuk(jumlahPupuk);
            }
            
            if (keterangan != null) {
                existingPemupukan.setKeterangan(keterangan);
            }

            KegiatanPemeliharaanPemupukan updatedPemupukan = service.update(id, existingPemupukan);
            return ResponseEntity.ok(updatedPemupukan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePemupukan(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}