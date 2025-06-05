package com.kehutanan.rh.konten.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.kehutanan.rh.konten.dto.KontenDeleteFilesRequest;
import com.kehutanan.rh.konten.dto.KontenPageDTO;
import com.kehutanan.rh.konten.model.Konten;
import com.kehutanan.rh.konten.model.dto.KontenDTO;
import com.kehutanan.rh.konten.service.KontenService;
import com.kehutanan.rh.master.model.Lov;
import com.kehutanan.rh.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/konten")
public class KontenController {

    private final KontenService service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Konten> pagedResourcesAssembler;

    @Autowired
    public KontenController(
            KontenService service,
            LovService lovService,
            PagedResourcesAssembler<Konten> pagedResourcesAssembler) {
        this.service = service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KontenPageDTO> getAllKonten(
            @RequestParam(required = false) String judul,
            @RequestParam(required = false) List<String> tipe,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        KontenPageDTO kontenPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((judul != null && !judul.isEmpty()) || (tipe != null && !tipe.isEmpty())) {
            kontenPage = service.findByFiltersWithCache(judul, tipe, pageable, baseUrl);
        } else {
            kontenPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kontenPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Konten by judul, konten content, or kata kunci")
    public ResponseEntity<KontenPageDTO> searchKonten(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KontenPageDTO kontenPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kontenPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KontenDTO> getKontenById(@PathVariable Long id) {
        try {
            KontenDTO kontenDTO = service.findDTOById(id);
            return ResponseEntity.ok(kontenDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Konten> createKonten(
            @RequestPart String judul,
            @RequestPart String konten,
            @RequestPart(value = "waktuAwalTayang", required = false) String waktuAwalTayang,
            @RequestPart(value = "waktuAkhirTayang", required = false) String waktuAkhirTayang,
            @RequestPart(value = "tipeId", required = false) String tipeId,
            @RequestPart(value = "statusId", required = false) String statusId,
            @RequestPart(value = "kataKunci", required = false) String kataKunciStr) {

        try {
            Konten newKonten = new Konten();
            newKonten.setJudul(judul);
            newKonten.setKonten(konten);

            // Process kata kunci string into a list
            if (kataKunciStr != null && !kataKunciStr.isEmpty()) {
                List<String> kataKunci = Arrays.asList(kataKunciStr.split(","));
                newKonten.setKataKunci(kataKunci);
            } else {
                newKonten.setKataKunci(new ArrayList<>());
            }

            if (waktuAwalTayang != null && !waktuAwalTayang.isEmpty()) {
                newKonten.setWaktuAwalTayang(LocalDateTime.parse(waktuAwalTayang));
            }

            if (waktuAkhirTayang != null && !waktuAkhirTayang.isEmpty()) {
                newKonten.setWaktuAkhirTayang(LocalDateTime.parse(waktuAkhirTayang));
            }

            // Set relations if IDs are provided
            if (tipeId != null && !tipeId.isEmpty()) {
                Long tipeIdLong = Long.parseLong(tipeId);
                Lov tipe = lovService.findById(tipeIdLong);
                newKonten.setTipe(tipe);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newKonten.setStatus(status);
            }

            Konten savedKonten = service.save(newKonten);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKonten);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Konten> updateKonten(
            @PathVariable Long id,
            @RequestPart String judul,
            @RequestPart String konten,
            @RequestPart(required = false) String waktuAwalTayang,
            @RequestPart(required = false) String waktuAkhirTayang,
            @RequestPart(required = false) String tipeId,
            @RequestPart(required = false) String statusId,
            @RequestPart(value = "kataKunci", required = false) String kataKunciStr) {

        try {
            Konten existingKonten = service.findById(id);

            existingKonten.setJudul(judul);
            existingKonten.setKonten(konten);

            if (kataKunciStr != null && !kataKunciStr.isEmpty()) {
                List<String> kataKunci = Arrays.asList(kataKunciStr.split(","));
                existingKonten.setKataKunci(kataKunci);
            } else {
                existingKonten.setKataKunci(new ArrayList<>());
            }
            if (waktuAwalTayang != null && !waktuAwalTayang.isEmpty()) {
                existingKonten.setWaktuAwalTayang(LocalDateTime.parse(waktuAwalTayang));
            }

            if (waktuAkhirTayang != null && !waktuAkhirTayang.isEmpty()) {
                existingKonten.setWaktuAkhirTayang(LocalDateTime.parse(waktuAkhirTayang));
            }

            // Update relations if IDs are provided
            if (tipeId != null && !tipeId.isEmpty()) {
                Long tipeIdLong = Long.parseLong(tipeId);
                Lov tipe = lovService.findById(tipeIdLong);
                existingKonten.setTipe(tipe);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingKonten.setStatus(status);
            }

            Konten updatedKonten = service.update(id, existingKonten);
            return ResponseEntity.ok(updatedKonten);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKonten(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple files for Konten")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "gambar", required = false) List<MultipartFile> gambar,
            @RequestPart(value = "gambarUtama", required = false) List<MultipartFile> gambarUtama) {

        try {
            if (gambar != null) {
                service.uploadKontenGambar(id, gambar);
            }
            if (gambarUtama != null) {
                service.uploadKontenGambarUtama(id, gambarUtama);
            }

            Konten konten = service.findById(id);
            return ResponseEntity.ok(konten);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete multiple files for Konten")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody(required = false) KontenDeleteFilesRequest filesRequest) {
        try {
            // Handle each file type list if provided
            if (filesRequest.getGambarIds() != null && !filesRequest.getGambarIds().isEmpty()) {
                service.deleteKontenGambar(id, filesRequest.getGambarIds());
            }

            if (filesRequest.getGambarUtamaIds() != null && !filesRequest.getGambarUtamaIds().isEmpty()) {
                service.deleteKontenGambarUtama(id, filesRequest.getGambarUtamaIds());
            }

            // Fetch and return the updated Konten entity
            Konten konten = service.findById(id);
            return ResponseEntity.ok(konten);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }
}