package com.kehutanan.tktrh.ppkh.kegiatan.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanBastReboRehabPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastReboRehab;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanBastReboRehabDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanBastReboRehabService;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-bast-rebo-rehab")
public class KegiatanBastReboRehabController {

    private final KegiatanBastReboRehabService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanBastReboRehab> pagedResourcesAssembler;

    @Autowired
    public KegiatanBastReboRehabController(
            KegiatanBastReboRehabService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanBastReboRehab> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanBastReboRehabPageDTO> getAllKegiatanBastReboRehab(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String tahun,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> statusList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanBastReboRehabPageDTO kegiatanPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if (kegiatanId != null || 
            (tahun != null && !tahun.isEmpty()) || 
            (keterangan != null && !keterangan.isEmpty()) || 
            (statusList != null && !statusList.isEmpty())) {
            kegiatanPage = service.findByFiltersWithCache(kegiatanId, tahun, keterangan, statusList, pageable, baseUrl);
        } else {
            kegiatanPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan Bast Rebo Rehab")
    public ResponseEntity<KegiatanBastReboRehabPageDTO> searchKegiatanBastReboRehab(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanBastReboRehabPageDTO kegiatanPage = service.searchWithCache(kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanBastReboRehabDTO> getKegiatanBastReboRehabById(@PathVariable Long id) {
        try {
            KegiatanBastReboRehabDTO kegiatanDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanBastReboRehab> createKegiatanBastReboRehab(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = true) String tahunId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusSerahTerimaId) {

        try {
            KegiatanBastReboRehab newKegiatan = new KegiatanBastReboRehab();
            
            // Set kegiatan
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatan.setKegiatan(kegiatan);
            } else {
                return ResponseEntity.badRequest().body(null);
            }

            // Set tahun
            if (tahunId != null && !tahunId.isEmpty()) {
                newKegiatan.setTahunId(Integer.parseInt(tahunId));
            } else {
                return ResponseEntity.badRequest().body(null);
            }

            // Set target luas
            if (targetLuas != null && !targetLuas.isEmpty()) {
                newKegiatan.setTargetLuas(Double.parseDouble(targetLuas));
            }

            // Set keterangan
            newKegiatan.setKeterangan(keterangan);

            // Set status serah terima
            if (statusSerahTerimaId != null && !statusSerahTerimaId.isEmpty()) {
                Long statusId = Long.parseLong(statusSerahTerimaId);
                Lov status = lovService.findById(statusId);
                newKegiatan.setStatusSerahTerima(status);
            }

            KegiatanBastReboRehab savedKegiatan = service.save(newKegiatan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanBastReboRehab> updateKegiatanBastReboRehab(
            @PathVariable Long id,
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = true) String tahunId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusSerahTerimaId) {

        try {
            KegiatanBastReboRehab existingKegiatan = service.findById(id);
            
            // Set kegiatan
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatan.setKegiatan(kegiatan);
            }

            // Set tahun
            if (tahunId != null && !tahunId.isEmpty()) {
                existingKegiatan.setTahunId(Integer.parseInt(tahunId));
            }

            // Set target luas
            if (targetLuas != null && !targetLuas.isEmpty()) {
                existingKegiatan.setTargetLuas(Double.parseDouble(targetLuas));
            }

            // Set keterangan
            if (keterangan != null) {
                existingKegiatan.setKeterangan(keterangan);
            }

            // Set status serah terima
            if (statusSerahTerimaId != null && !statusSerahTerimaId.isEmpty()) {
                Long statusId = Long.parseLong(statusSerahTerimaId);
                Lov status = lovService.findById(statusId);
                existingKegiatan.setStatusSerahTerima(status);
            }

            KegiatanBastReboRehab updatedKegiatan = service.update(id, existingKegiatan);
            return ResponseEntity.ok(updatedKegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanBastReboRehab(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}