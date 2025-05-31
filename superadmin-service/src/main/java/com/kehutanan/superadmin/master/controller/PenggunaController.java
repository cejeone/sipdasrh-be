package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.PenggunaService;
import com.kehutanan.superadmin.master.model.Pengguna;
import com.kehutanan.superadmin.master.model.PenggunaFoto;
import com.kehutanan.superadmin.master.service.PeranService;

import io.swagger.v3.oas.annotations.Operation;

import com.kehutanan.superadmin.master.service.LovService;
import com.kehutanan.superadmin.master.model.Peran;
import com.kehutanan.superadmin.master.model.Lov;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.web.PagedResourcesAssembler;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/pengguna")
public class PenggunaController {

    private final PenggunaService service;
    private final PeranService peranService;
    private final LovService lovService;
    private final PagedResourcesAssembler<Pengguna> pagedResourcesAssembler;

    @Autowired
    public PenggunaController(PenggunaService service,
            PeranService peranService,
            LovService lovService,
            PagedResourcesAssembler<Pengguna> pagedResourcesAssembler) {
        this.service = service;
        this.peranService = peranService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Pengguna>>> getAllPengguna(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String namaLengkap,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long peranId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Pengguna> penggunaPage;

        // Check if any filter is provided
        if ((username != null && !username.isEmpty()) ||
                (namaLengkap != null && !namaLengkap.isEmpty()) ||
                (email != null && !email.isEmpty()) ||
                (peranId != null) ||
                (statusId != null)) {
            penggunaPage = service.findByFilters(username, namaLengkap, email, peranId, statusId, pageable);
        } else {
            penggunaPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Pengguna>> pagedModel = pagedResourcesAssembler.toModel(penggunaPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pengguna> getPenggunaById(@PathVariable Long id) {
        try {
            Pengguna pengguna = service.findById(id);
            return ResponseEntity.ok(pengguna);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Pengguna> createPengguna(
            @RequestPart String username,
            @RequestPart String namaLengkap,
            @RequestPart(required = false) String noHp,
            @RequestPart String email,
            @RequestPart String kataSandi,
            @RequestPart String peranId,
            @RequestPart String statusId) {

        try {
            Long peranIdLong = Long.parseLong(peranId);
            Long statusIdLong = Long.parseLong(statusId);

            Peran peran = peranService.findById(peranIdLong);
            Lov status = lovService.findById(statusIdLong);

            Pengguna newPengguna = new Pengguna();
            newPengguna.setUsername(username);
            newPengguna.setNamaLengkap(namaLengkap);
            newPengguna.setNoHp(noHp);
            newPengguna.setEmail(email);
            newPengguna.setKataSandi(kataSandi);
            newPengguna.setPeran(peran);
            newPengguna.setStatus(status);

            Pengguna savedPengguna = service.save(newPengguna);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPengguna);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Pengguna> updatePengguna(
            @PathVariable Long id,
            @RequestPart String username,
            @RequestPart String namaLengkap,
            @RequestPart(required = false) String noHp,
            @RequestPart String email,
            @RequestPart(required = false) String kataSandi,
            @RequestPart String peranId,
            @RequestPart String statusId) {

        try {
            Long peranIdLong = Long.parseLong(peranId);
            Long statusIdLong = Long.parseLong(statusId);

            Pengguna existingPengguna = service.findById(id);
            Peran peran = peranService.findById(peranIdLong);
            Lov status = lovService.findById(statusIdLong);

            // Update the existing pengguna with new values
            existingPengguna.setUsername(username);
            existingPengguna.setNamaLengkap(namaLengkap);
            existingPengguna.setNoHp(noHp);
            existingPengguna.setEmail(email);

            // Only update password if provided
            if (kataSandi != null && !kataSandi.isEmpty()) {
                existingPengguna.setKataSandi(kataSandi);
            }

            existingPengguna.setPeran(peran);
            existingPengguna.setStatus(status);

            Pengguna updatedPengguna = service.update(id, existingPengguna);
            return ResponseEntity.ok(updatedPengguna);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePengguna(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple photos for Pengguna")
    public ResponseEntity<?> uploadProfileFotos(
            @PathVariable Long id,
            @RequestPart("files") List<MultipartFile> fotoProfiles) {
        try {

            Pengguna pengguna = service.uploadPenggunaFoto(id, fotoProfiles);

            return ResponseEntity.ok(pengguna);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload gambar: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/file")
    @Operation(summary = "Delete multiple photos for Pengguna")
    public ResponseEntity<?> deleteProfileFotos(
            @PathVariable Long id,
            @RequestBody List<String> uuidFotoProfiles) {
        try {
            Pengguna pengguna = service.deletePenggunaFoto(id, uuidFotoProfiles);
            return ResponseEntity.ok(pengguna);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus gambar: " + e.getMessage());
        }
    }
}