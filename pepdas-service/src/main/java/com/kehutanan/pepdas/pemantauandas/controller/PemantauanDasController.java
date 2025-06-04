package com.kehutanan.pepdas.pemantauandas.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.kehutanan.pepdas.master.model.Bpdas;
import com.kehutanan.pepdas.master.model.Das;
import com.kehutanan.pepdas.master.model.Spas;
import com.kehutanan.pepdas.master.service.BpdasService;
import com.kehutanan.pepdas.master.service.DasService;
import com.kehutanan.pepdas.master.service.SpasService;
import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasPageDTO;
import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;
import com.kehutanan.pepdas.pemantauandas.model.dto.PemantauanDasDTO;
import com.kehutanan.pepdas.pemantauandas.service.PemantauanDasService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/pemantauan-das")
public class PemantauanDasController {

    private final PemantauanDasService service;
    private final BpdasService bpdasService;
    private final DasService dasService;
    private final SpasService spasService;
    private final PagedResourcesAssembler<PemantauanDas> pagedResourcesAssembler;

    @Autowired
    public PemantauanDasController(
            PemantauanDasService service,
            BpdasService bpdasService,
            DasService dasService,
            SpasService spasService,
            PagedResourcesAssembler<PemantauanDas> pagedResourcesAssembler) {
        this.service = service;
        this.bpdasService = bpdasService;
        this.dasService = dasService;
        this.spasService = spasService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all pemantauan DAS with filtering options")
    public ResponseEntity<PemantauanDasPageDTO> getAllPemantauanDas(
            @RequestParam(required = false) String das,
            @RequestParam(required = false) String spasId,
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        PemantauanDasPageDTO pemantauanDasPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((das != null && !das.isEmpty()) ||
                (spasId != null && !spasId.isEmpty()) ||
                (bpdas != null && !bpdas.isEmpty())) {
            pemantauanDasPage = service.findByFiltersWithCache(das, spasId, bpdas, pageable, baseUrl);
        } else {
            pemantauanDasPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(pemantauanDasPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search pemantauan DAS by keyword")
    public ResponseEntity<PemantauanDasPageDTO> searchPemantauanDas(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        PemantauanDasPageDTO pemantauanDasPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(pemantauanDasPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pemantauan DAS by ID")
    public ResponseEntity<PemantauanDasDTO> getPemantauanDasById(@PathVariable Long id) {
        try {
            PemantauanDasDTO pemantauanDasDTO = service.findDTOById(id);
            return ResponseEntity.ok(pemantauanDasDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new pemantauan DAS")
    public ResponseEntity<PemantauanDas> createPemantauanDas(
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String dasId,
            @RequestPart(required = false) String spasId,
            @RequestPart(required = false) String tanggalDanWaktu,
            @RequestPart(required = false) String nilaiTma,
            @RequestPart(required = false) String nilaiCurahHujan,
            @RequestPart(required = false) String teganganBaterai) {

        try {
            PemantauanDas newPemantauanDas = new PemantauanDas();

            // Set relations if IDs are provided
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                newPemantauanDas.setBpdas(bpdas);
            }

            if (dasId != null && !dasId.isEmpty()) {
                Long dasIdLong = Long.parseLong(dasId);
                Das das = dasService.findById(dasIdLong);
                newPemantauanDas.setDas(das);
            }

            if (spasId != null && !spasId.isEmpty()) {
                Long spasIdLong = Long.parseLong(spasId);
                Spas spas = spasService.findById(spasIdLong);
                newPemantauanDas.setSpas(spas);
            }

            // Parse and set date time
            if (tanggalDanWaktu != null && !tanggalDanWaktu.isEmpty()) {
                LocalDateTime dateTime = LocalDateTime.parse(tanggalDanWaktu, DateTimeFormatter.ISO_DATE_TIME);
                newPemantauanDas.setTanggalDanWaktu(dateTime);
            } else {
                newPemantauanDas.setTanggalDanWaktu(LocalDateTime.now());
            }

            // Set numeric values
            if (nilaiTma != null && !nilaiTma.isEmpty()) {
                newPemantauanDas.setNilaiTma(Integer.parseInt(nilaiTma));
            }

            if (nilaiCurahHujan != null && !nilaiCurahHujan.isEmpty()) {
                newPemantauanDas.setNilaiCurahHujan(Integer.parseInt(nilaiCurahHujan));
            }

            if (teganganBaterai != null && !teganganBaterai.isEmpty()) {
                newPemantauanDas.setTeganganBaterai(Integer.parseInt(teganganBaterai));
            }

            PemantauanDas savedPemantauanDas = service.save(newPemantauanDas);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPemantauanDas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing pemantauan DAS")
    public ResponseEntity<PemantauanDas> updatePemantauanDas(
            @PathVariable Long id,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String dasId,
            @RequestPart(required = false) String spasId,
            @RequestPart(required = false) String tanggalDanWaktu,
            @RequestPart(required = false) String nilaiTma,
            @RequestPart(required = false) String nilaiCurahHujan,
            @RequestPart(required = false) String teganganBaterai) {

        try {
            PemantauanDas existingPemantauanDas = service.findById(id);

            // Update relations if IDs are provided
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                existingPemantauanDas.setBpdas(bpdas);
            }

            if (dasId != null && !dasId.isEmpty()) {
                Long dasIdLong = Long.parseLong(dasId);
                Das das = dasService.findById(dasIdLong);
                existingPemantauanDas.setDas(das);
            }

            if (spasId != null && !spasId.isEmpty()) {
                Long spasIdLong = Long.parseLong(spasId);
                Spas spas = spasService.findById(spasIdLong);
                existingPemantauanDas.setSpas(spas);
            }

            // Update date time if provided
            if (tanggalDanWaktu != null && !tanggalDanWaktu.isEmpty()) {
                LocalDateTime dateTime = LocalDateTime.parse(tanggalDanWaktu, DateTimeFormatter.ISO_DATE_TIME);
                existingPemantauanDas.setTanggalDanWaktu(dateTime);
            }

            // Update numeric values if provided
            if (nilaiTma != null && !nilaiTma.isEmpty()) {
                existingPemantauanDas.setNilaiTma(Integer.parseInt(nilaiTma));
            }

            if (nilaiCurahHujan != null && !nilaiCurahHujan.isEmpty()) {
                existingPemantauanDas.setNilaiCurahHujan(Integer.parseInt(nilaiCurahHujan));
            }

            if (teganganBaterai != null && !teganganBaterai.isEmpty()) {
                existingPemantauanDas.setTeganganBaterai(Integer.parseInt(teganganBaterai));
            }

            PemantauanDas updatedPemantauanDas = service.update(id, existingPemantauanDas);
            return ResponseEntity.ok(updatedPemantauanDas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a pemantauan DAS by ID")
    public ResponseEntity<Void> deletePemantauanDas(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}