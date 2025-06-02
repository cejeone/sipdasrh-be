package com.kehutanan.pepdas.konten.controller;

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

import com.kehutanan.pepdas.konten.dto.KontenDTO;
import com.kehutanan.pepdas.konten.dto.KontenDeleteFilesRequest;
import com.kehutanan.pepdas.konten.dto.KontenPageDTO;
import com.kehutanan.pepdas.konten.model.Konten;
import com.kehutanan.pepdas.konten.service.KontenService;
import com.kehutanan.pepdas.master.model.Lov;
import com.kehutanan.pepdas.master.service.LovService;

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
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KontenPageDTO kontenPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((judul != null && !judul.isEmpty()) || 
                (bpdas != null && !bpdas.isEmpty())) {
            kontenPage = service.findByFiltersWithCache(judul, bpdas, pageable, baseUrl);
        } else {
            kontenPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kontenPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Konten by keyword")
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
            KontenDTO kontenDto = service.findDTOById(id);
            return ResponseEntity.ok(kontenDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Konten> createKonten(
            @RequestPart(required = true) String judul,
            @RequestPart(required = false) String kontenText,
            @RequestPart(required = false) String kataKunci,
            @RequestPart(required = false) String waktuAwalTayang,
            @RequestPart(required = false) String waktuAkhirTayang,
            @RequestPart(required = false) String tipeId,
            @RequestPart(required = false) String statusId) {

        try {
            Konten newKonten = new Konten();
            newKonten.setJudul(judul);
            
            if (kontenText != null) {
                newKonten.setKonten(kontenText);
            }
            
            // Process kata kunci - convert comma separated string to list
            if (kataKunci != null && !kataKunci.isEmpty()) {
                List<String> kataKunciList = new ArrayList<>(Arrays.asList(kataKunci.split(",")));
                // Trim each kata kunci
                kataKunciList.replaceAll(String::trim);
                newKonten.setKataKunci(kataKunciList);
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
            // Add detailed logging for troubleshooting
            e.printStackTrace(); // Log stack trace to console
            
            // Return more specific error details in response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Consider sending a structured error response instead of null
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Konten> updateKonten(
            @PathVariable Long id,
            @RequestPart(required = true) String judul,
            @RequestPart(required = false) String kontenText,
            @RequestPart(required = false) String kataKunci,
            @RequestPart(required = false) String waktuAwalTayang,
            @RequestPart(required = false) String waktuAkhirTayang,
            @RequestPart(required = false) String tipeId,
            @RequestPart(required = false) String statusId) {

        try {
            Konten existingKonten = service.findById(id);
            
            existingKonten.setJudul(judul);
            
            if (kontenText != null) {
                existingKonten.setKonten(kontenText);
            }
            
            // Process kata kunci - convert comma separated string to list
            if (kataKunci != null && !kataKunci.isEmpty()) {
                List<String> kataKunciList = new ArrayList<>(Arrays.asList(kataKunci.split(",")));
                // Trim each kata kunci
                kataKunciList.replaceAll(String::trim);
                existingKonten.setKataKunci(kataKunciList);
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
            e.printStackTrace();
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
    @Operation(summary = "Upload images for Konten")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "kontenGambars", required = false) List<MultipartFile> kontenGambars,
            @RequestPart(value = "kontenGambarUtamas", required = false) List<MultipartFile> kontenGambarUtamas) {
        try {
            if (kontenGambars != null && !kontenGambars.isEmpty()) {
                service.uploadKontenGambar(id, kontenGambars);
            }
            
            if (kontenGambarUtamas != null && !kontenGambarUtamas.isEmpty()) {
                service.uploadKontenGambarUtama(id, kontenGambarUtamas);
            }
            
            Konten konten = service.findById(id);
            return ResponseEntity.ok(konten);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload gambar: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete images from Konten")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody(required = false) KontenDeleteFilesRequest filesRequest) {
        try {
            if (filesRequest != null) {
                if (filesRequest.getKontenGambarIds() != null && !filesRequest.getKontenGambarIds().isEmpty()) {
                    service.deleteKontenGambar(id, filesRequest.getKontenGambarIds());
                }
                
                if (filesRequest.getKontenGambarUtamaIds() != null && !filesRequest.getKontenGambarUtamaIds().isEmpty()) {
                    service.deleteKontenGambarUtama(id, filesRequest.getKontenGambarUtamaIds());
                }
            }

            Konten konten = service.findById(id);
            return ResponseEntity.ok(konten);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus gambar: " + e.getMessage());
        }
    }
}