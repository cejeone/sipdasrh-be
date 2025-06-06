package com.kehutanan.tktrh.tmkh.kegiatan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
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
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanFungsiKawasanRehabPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanRehab;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanFungsiKawasanRehabDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanFungsiKawasanRehabService;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-fungsi-kawasan-rehab")
public class KegiatanFungsiKawasanRehabController {

    private final KegiatanFungsiKawasanRehabService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanFungsiKawasanRehab> pagedResourcesAssembler;

    @Autowired
    public KegiatanFungsiKawasanRehabController(
            KegiatanFungsiKawasanRehabService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanFungsiKawasanRehab> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanFungsiKawasanRehabPageDTO> getAllKegiatanFungsiKawasanRehab(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String fungsiKawasan,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> statusList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanFungsiKawasanRehabPageDTO kegiatanFungsiKawasanRehabPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if (kegiatanId != null || 
            (fungsiKawasan != null && !fungsiKawasan.isEmpty()) || 
            (keterangan != null && !keterangan.isEmpty()) || 
            (statusList != null && !statusList.isEmpty())) {
            
            kegiatanFungsiKawasanRehabPage = service.findByFiltersWithCache(
                kegiatanId, fungsiKawasan, keterangan, statusList, pageable, baseUrl);
        } else {
            kegiatanFungsiKawasanRehabPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanFungsiKawasanRehabPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanFungsiKawasanRehab by keywords")
    public ResponseEntity<KegiatanFungsiKawasanRehabPageDTO> searchKegiatanFungsiKawasanRehab(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanFungsiKawasanRehabPageDTO result = service.searchWithCache(
            kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanFungsiKawasanRehabDTO> getKegiatanFungsiKawasanRehabById(@PathVariable Long id) {
        try {
            KegiatanFungsiKawasanRehabDTO dto = service.findDTOById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanFungsiKawasanRehab> createKegiatanFungsiKawasanRehab(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = true) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusId) {

        try {
            KegiatanFungsiKawasanRehab newEntity = new KegiatanFungsiKawasanRehab();
            
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newEntity.setKegiatan(kegiatan);
            }
            
            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long fungsiKawasanIdLong = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(fungsiKawasanIdLong);
                newEntity.setFungsiKawasanId(fungsiKawasan);
            }
            
            if (targetLuas != null && !targetLuas.isEmpty()) {
                newEntity.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                newEntity.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            if (tahunId != null && !tahunId.isEmpty()) {
                newEntity.setTahunId(Integer.parseInt(tahunId));
            }
            
            newEntity.setKeterangan(keterangan);
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newEntity.setStatusId(status);
            }
            
            KegiatanFungsiKawasanRehab savedEntity = service.save(newEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<KegiatanFungsiKawasanRehab> updateKegiatanFungsiKawasanRehab(
            @PathVariable Long id,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusId) {

        try {
            KegiatanFungsiKawasanRehab existingEntity = service.findById(id);
            
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingEntity.setKegiatan(kegiatan);
            }
            
            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long fungsiKawasanIdLong = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(fungsiKawasanIdLong);
                existingEntity.setFungsiKawasanId(fungsiKawasan);
            }
            
            if (targetLuas != null && !targetLuas.isEmpty()) {
                existingEntity.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                existingEntity.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            if (tahunId != null && !tahunId.isEmpty()) {
                existingEntity.setTahunId(Integer.parseInt(tahunId));
            }
            
            if (keterangan != null) {
                existingEntity.setKeterangan(keterangan);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingEntity.setStatusId(status);
            }
            
            KegiatanFungsiKawasanRehab updatedEntity = service.update(id, existingEntity);
            return ResponseEntity.ok(updatedEntity);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanFungsiKawasanRehab(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}