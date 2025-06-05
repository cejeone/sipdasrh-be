package com.kehutanan.rh.serahterima.controller;

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

import com.kehutanan.rh.master.model.Bpdas;
import com.kehutanan.rh.master.model.Lov;
import com.kehutanan.rh.master.model.Provinsi;
import com.kehutanan.rh.master.service.BpdasService;
import com.kehutanan.rh.master.service.LovService;
import com.kehutanan.rh.master.service.ProvinsiService;
import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.program.service.ProgramService;
import com.kehutanan.rh.serahterima.dto.BastPusatPageDTO;
import com.kehutanan.rh.serahterima.model.BastPusat;
import com.kehutanan.rh.serahterima.model.dto.BastPusatDTO;
import com.kehutanan.rh.serahterima.service.BastPusatService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bast-pusat")
public class BastPusatController {

    private final BastPusatService service;
    private final ProgramService programService;
    private final BpdasService bpdasService;
    private final ProvinsiService provinsiService;
    private final LovService lovService;
    private final PagedResourcesAssembler<BastPusat> pagedResourcesAssembler;

    @Autowired
    public BastPusatController(
            BastPusatService service,
            ProgramService programService,
            BpdasService bpdasService,
            ProvinsiService provinsiService,
            LovService lovService,
            PagedResourcesAssembler<BastPusat> pagedResourcesAssembler) {
        this.service = service;
        this.programService = programService;
        this.bpdasService = bpdasService;
        this.provinsiService = provinsiService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all BAST Pusat with filters")
    public ResponseEntity<BastPusatPageDTO> getAllBastPusat(
            @RequestParam(required = false) String keterangan,
            @RequestParam(required = false) List<String> bpdasList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        BastPusatPageDTO bastPusatPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((keterangan != null && !keterangan.isEmpty()) ||
                (bpdasList != null && !bpdasList.isEmpty())) {
            bastPusatPage = service.findByFiltersWithCache(keterangan, bpdasList, pageable, baseUrl);
        } else {
            bastPusatPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(bastPusatPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search BAST Pusat by keyword")
    public ResponseEntity<BastPusatPageDTO> searchBastPusat(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        BastPusatPageDTO bastPusatPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(bastPusatPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get BAST Pusat by ID")
    public ResponseEntity<BastPusatDTO> getBastPusatById(@PathVariable Long id) {
        try {
            BastPusatDTO bastPusatDto = service.findDTOById(id);
            return ResponseEntity.ok(bastPusatDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new BAST Pusat")
    public ResponseEntity<BastPusat> createBastPusat(
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String keterangan) {

        try {
            BastPusat newBastPusat = new BastPusat();

            // Set relationships if IDs are provided
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                newBastPusat.setProgramId(program);
            }

            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                newBastPusat.setBpdasId(bpdas);
            }

            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                newBastPusat.setProvinsiId(provinsi);
            }

            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long fungsiKawasanIdLong = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(fungsiKawasanIdLong);
                newBastPusat.setFungsiKawasan(fungsiKawasan);
            }

            if (targetLuas != null && !targetLuas.isEmpty()) {
                newBastPusat.setTargetLuas(Integer.parseInt(targetLuas));
            }

            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                newBastPusat.setRealisasiLuas(Integer.parseInt(realisasiLuas));
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newBastPusat.setStatusId(status);
            }

            newBastPusat.setKeterangan(keterangan);

            BastPusat savedBastPusat = service.save(newBastPusat);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBastPusat);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Add detailed logging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing BAST Pusat")
    public ResponseEntity<BastPusat> updateBastPusat(
            @PathVariable Long id,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String fungsiKawasanId,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String realisasiLuas,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String keterangan) {

        try {
            BastPusat existingBastPusat = service.findById(id);

            // Update relationships if IDs are provided
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                existingBastPusat.setProgramId(program);
            }

            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                existingBastPusat.setBpdasId(bpdas);
            }

            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                existingBastPusat.setProvinsiId(provinsi);
            }

            if (fungsiKawasanId != null && !fungsiKawasanId.isEmpty()) {
                Long fungsiKawasanIdLong = Long.parseLong(fungsiKawasanId);
                Lov fungsiKawasan = lovService.findById(fungsiKawasanIdLong);
                existingBastPusat.setFungsiKawasan(fungsiKawasan);
            }

            if (targetLuas != null && !targetLuas.isEmpty()) {
                existingBastPusat.setTargetLuas(Integer.parseInt(targetLuas));
            }

            if (realisasiLuas != null && !realisasiLuas.isEmpty()) {
                existingBastPusat.setRealisasiLuas(Integer.parseInt(realisasiLuas));
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingBastPusat.setStatusId(status);
            }

            if (keterangan != null) {
                existingBastPusat.setKeterangan(keterangan);
            }

            BastPusat updatedBastPusat = service.update(id, existingBastPusat);
            return ResponseEntity.ok(updatedBastPusat);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a BAST Pusat by ID")
    public ResponseEntity<Void> deleteBastPusat(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}