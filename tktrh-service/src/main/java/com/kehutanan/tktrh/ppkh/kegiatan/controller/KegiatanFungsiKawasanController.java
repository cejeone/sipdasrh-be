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
import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanFungsiKawasanPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanFungsiKawasanDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanFungsiKawasanService;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-fungsi-kawasan")
public class KegiatanFungsiKawasanController {

    private final KegiatanFungsiKawasanService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanFungsiKawasan> pagedResourcesAssembler;

    @Autowired
    public KegiatanFungsiKawasanController(
            KegiatanFungsiKawasanService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanFungsiKawasan> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanFungsiKawasanPageDTO> getAllKegiatanFungsiKawasan(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String fungsiKawasan,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> statusList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanFungsiKawasanPageDTO kegiatanFungsiKawasanPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatanId != null) || 
            (fungsiKawasan != null && !fungsiKawasan.isEmpty()) ||
            (keterangan != null && !keterangan.isEmpty()) ||
            (statusList != null && !statusList.isEmpty())) {
            kegiatanFungsiKawasanPage = service.findByFiltersWithCache(kegiatanId, fungsiKawasan, keterangan, 
                                                                     statusList, pageable, baseUrl);
        } else {
            kegiatanFungsiKawasanPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanFungsiKawasanPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanFungsiKawasan by keyword")
    public ResponseEntity<KegiatanFungsiKawasanPageDTO> searchKegiatanFungsiKawasan(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanFungsiKawasanPageDTO kegiatanFungsiKawasanPage = 
            service.searchWithCache(kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanFungsiKawasanPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanFungsiKawasanDTO> getKegiatanFungsiKawasanById(@PathVariable Long id) {
        try {
            KegiatanFungsiKawasanDTO kegiatanFungsiKawasanDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanFungsiKawasanDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanFungsiKawasan> createKegiatanFungsiKawasan(
            @RequestPart(required = true) String kegiatanId,
            @RequestPart(required = true) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusId) {

        try {
            KegiatanFungsiKawasan newKegiatanFungsiKawasan = new KegiatanFungsiKawasan();
            
            // Set kegiatan
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatanFungsiKawasan.setKegiatan(kegiatan);
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            // Set fungsiKawasanId
            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long fungsiKawasanIdLong = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(fungsiKawasanIdLong);
                newKegiatanFungsiKawasan.setFungsiKawasanId(fungsiKawasan);
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            // Set targetLuas
            if (targetLuas != null && !targetLuas.isEmpty()) {
                newKegiatanFungsiKawasan.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            // Set realisasiLuas
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                newKegiatanFungsiKawasan.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            // Set tahunId
            if (tahunId != null && !tahunId.isEmpty()) {
                newKegiatanFungsiKawasan.setTahunId(Integer.parseInt(tahunId));
            }
            
            // Set keterangan
            newKegiatanFungsiKawasan.setKeterangan(keterangan);
            
            // Set statusId
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newKegiatanFungsiKawasan.setStatusId(status);
            }
            
            KegiatanFungsiKawasan savedKegiatanFungsiKawasan = service.save(newKegiatanFungsiKawasan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatanFungsiKawasan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanFungsiKawasan> updateKegiatanFungsiKawasan(
            @PathVariable Long id,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String tahunId,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String statusId) {

        try {
            KegiatanFungsiKawasan existingKegiatanFungsiKawasan = service.findById(id);
            
            // Update kegiatan if provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatanFungsiKawasan.setKegiatan(kegiatan);
            }
            
            // Update fungsiKawasanId if provided
            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long fungsiKawasanIdLong = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(fungsiKawasanIdLong);
                existingKegiatanFungsiKawasan.setFungsiKawasanId(fungsiKawasan);
            }
            
            // Update targetLuas if provided
            if (targetLuas != null && !targetLuas.isEmpty()) {
                existingKegiatanFungsiKawasan.setTargetLuas(Double.parseDouble(targetLuas));
            }
            
            // Update realisasiLuas if provided
            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                existingKegiatanFungsiKawasan.setRealisasiLuas(Double.parseDouble(realisasiLuas));
            }
            
            // Update tahunId if provided
            if (tahunId != null && !tahunId.isEmpty()) {
                existingKegiatanFungsiKawasan.setTahunId(Integer.parseInt(tahunId));
            }
            
            // Update keterangan if provided
            if (keterangan != null) {
                existingKegiatanFungsiKawasan.setKeterangan(keterangan);
            }
            
            // Update statusId if provided
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingKegiatanFungsiKawasan.setStatusId(status);
            }
            
            KegiatanFungsiKawasan updatedKegiatanFungsiKawasan = service.update(id, existingKegiatanFungsiKawasan);
            return ResponseEntity.ok(updatedKegiatanFungsiKawasan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanFungsiKawasan(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}