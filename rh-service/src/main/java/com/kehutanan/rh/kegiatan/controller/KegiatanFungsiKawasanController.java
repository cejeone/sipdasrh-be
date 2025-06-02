package com.kehutanan.rh.kegiatan.controller;

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

import com.kehutanan.rh.kegiatan.dto.KegiatanFungsiKawasanDTO;
import com.kehutanan.rh.kegiatan.dto.KegiatanFungsiKawasanPageDTO;
import com.kehutanan.rh.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.rh.kegiatan.service.KegiatanFungsiKawasanService;
import com.kehutanan.rh.kegiatan.service.KegiatanService;
import com.kehutanan.rh.master.model.Lov;
import com.kehutanan.rh.master.service.LovService;

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
    @Operation(summary = "Get all KegiatanFungsiKawasan with optional filters")
    public ResponseEntity<KegiatanFungsiKawasanPageDTO> getAllKegiatanFungsiKawasan(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> fungsiKawasan,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        String baseUrl = request.getRequestURL().toString();
        KegiatanFungsiKawasanPageDTO resultPage;

        // Check if any filter is provided
        if (kegiatanId != null || (keterangan != null && !keterangan.isEmpty()) || 
            (fungsiKawasan != null && !fungsiKawasan.isEmpty())) {
            resultPage = service.findByFiltersWithCache(kegiatanId, keterangan, fungsiKawasan, pageable, baseUrl);
        } else {
            resultPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(resultPage);
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
        String baseUrl = request.getRequestURL().toString();
        
        KegiatanFungsiKawasanPageDTO resultPage = service.searchWithCache(
            kegiatanId, keyWord, pageable, baseUrl);
            
        return ResponseEntity.ok(resultPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get KegiatanFungsiKawasan by ID")
    public ResponseEntity<KegiatanFungsiKawasanDTO> getKegiatanFungsiKawasanById(@PathVariable Long id) {
        try {
            KegiatanFungsiKawasanDTO dto = service.findDTOById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

   @PostMapping
@ResponseStatus(HttpStatus.CREATED)
@Operation(summary = "Create new KegiatanFungsiKawasan")
public ResponseEntity<KegiatanFungsiKawasan> createKegiatanFungsiKawasan(
        @RequestPart Long kegiatanId,
        @RequestPart Long fungsiKawasanId,
        @RequestPart Long statusId,
        @RequestPart(required = false) Double targetLuas,
        @RequestPart(required = false) Double realisasiLuas,
        @RequestPart(required = false) Integer tahunId,
        @RequestPart(required = false) String keterangan) {
    
    try {
        KegiatanFungsiKawasan newEntity = new KegiatanFungsiKawasan();
        
        // Set the kegiatan relation using kegiatanService
        newEntity.setKegiatan(kegiatanService.findById(kegiatanId));
        
        // Set fungsiKawasanId from Lov
        Lov fungsiKawasan = lovService.findById(fungsiKawasanId);
        newEntity.setFungsiKawasanId(fungsiKawasan);
        
        // Set statusId from Lov
        Lov status = lovService.findById(statusId);
        newEntity.setStatusId(status);
        
        // Set other fields
        newEntity.setTargetLuas(targetLuas);
        newEntity.setRealisasiLuas(realisasiLuas);
        newEntity.setTahunId(tahunId);
        newEntity.setKeterangan(keterangan);
        
        KegiatanFungsiKawasan savedEntity = service.save(newEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
    } catch (EntityNotFoundException e) {
        return ResponseEntity.badRequest().build();
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@PutMapping("/{id}")
@Operation(summary = "Update KegiatanFungsiKawasan")
public ResponseEntity<KegiatanFungsiKawasan> updateKegiatanFungsiKawasan(
        @PathVariable Long id,
        @RequestPart(required = false) Long kegiatanId,
        @RequestPart(required = false) Long fungsiKawasanId,
        @RequestPart(required = false) Long statusId,
        @RequestPart(required = false) Double targetLuas,
        @RequestPart(required = false) Double realisasiLuas,
        @RequestPart(required = false) Integer tahunId,
        @RequestPart(required = false) String keterangan) {
    
    try {
        KegiatanFungsiKawasan existingEntity = service.findById(id);
        
        // Update kegiatan relation if provided
        if (kegiatanId != null) {
            existingEntity.setKegiatan(kegiatanService.findById(kegiatanId));
        }
        
        // Update fungsiKawasanId if provided
        if (fungsiKawasanId != null) {
            Lov fungsiKawasan = lovService.findById(fungsiKawasanId);
            existingEntity.setFungsiKawasanId(fungsiKawasan);
        }
        
        // Update statusId if provided
        if (statusId != null) {
            Lov status = lovService.findById(statusId);
            existingEntity.setStatusId(status);
        }
        
        // Update other fields if provided
        if (targetLuas != null) {
            existingEntity.setTargetLuas(targetLuas);
        }
        
        if (realisasiLuas != null) {
            existingEntity.setRealisasiLuas(realisasiLuas);
        }
        
        if (tahunId != null) {
            existingEntity.setTahunId(tahunId);
        }
        
        if (keterangan != null) {
            existingEntity.setKeterangan(keterangan);
        }
        
        KegiatanFungsiKawasan updatedEntity = service.update(id, existingEntity);
        return ResponseEntity.ok(updatedEntity);
    } catch (EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete KegiatanFungsiKawasan by ID")
    public ResponseEntity<Void> deleteKegiatanFungsiKawasan(@PathVariable Long id) {
        try {
            // Verify the entity exists
            service.findById(id);
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}