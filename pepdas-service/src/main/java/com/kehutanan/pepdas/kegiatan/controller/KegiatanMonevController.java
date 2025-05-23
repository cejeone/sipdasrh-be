package com.kehutanan.pepdas.kegiatan.controller;

import com.kehutanan.pepdas.kegiatan.dto.KegiatanMonevDto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanMonev;
import com.kehutanan.pepdas.kegiatan.model.KegiatanMonevPdf;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanRepository;
import com.kehutanan.pepdas.kegiatan.service.KegiatanMonevService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/kegiatan-monev")
@Tag(name = "Kegiatan Monitoring dan Evaluasi", description = "API endpoints for managing kegiatan monitoring dan evaluasi")
public class KegiatanMonevController {

    private final KegiatanMonevService kegiatanMonevService;
    private final PagedResourcesAssembler<KegiatanMonev> pagedResourcesAssembler;
    private final KegiatanRepository kegiatanRepository;

    public KegiatanMonevController(KegiatanMonevService kegiatanMonevService,
            PagedResourcesAssembler<KegiatanMonev> pagedResourcesAssembler,
            KegiatanRepository kegiatanRepository) {
        this.kegiatanMonevService = kegiatanMonevService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.kegiatanRepository = kegiatanRepository;
    }

    @GetMapping
    @Operation(summary = "Get all kegiatan monev", description = "Returns all kegiatan monitoring dan evaluasi with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan monev", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = PagedModel.class)))
    })
    public ResponseEntity<PagedModel<EntityModel<KegiatanMonev>>> getAll(
             @Parameter(description = "Id Kegiatan") @RequestParam String KegiatanId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Mencari untuk deskripsi") @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        PagedModel<EntityModel<KegiatanMonev>> pagedModel = kegiatanMonevService.findAll(KegiatanId,search,
                pageable, pagedResourcesAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find kegiatan monev by ID", description = "Returns a single kegiatan monitoring dan evaluasi by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the kegiatan monev"),
            @ApiResponse(responseCode = "404", description = "Kegiatan monev not found")
    })
    public ResponseEntity<KegiatanMonev> findById(
            @Parameter(description = "Kegiatan monev ID") @PathVariable UUID id) {

        KegiatanMonev kegiatanMonev = kegiatanMonevService.findById(id);
        return ResponseEntity.ok(kegiatanMonev);
    }

    @PostMapping
    @Operation(summary = "Create a new kegiatan monev", description = "Create a new kegiatan monitoring dan evaluasi with form data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kegiatan monev created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<KegiatanMonev> create(
            @Valid @RequestBody KegiatanMonevDto kegiatanMonevDto) throws Exception { // Changed from UUID to String

        // Parse the date and UUID manually to be more resilient

        return ResponseEntity.status(HttpStatus.CREATED).body(
                kegiatanMonevService.create(kegiatanMonevDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update kegiatan monev", description = "Update an existing kegiatan monitoring dan evaluasi entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kegiatan monev updated successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan monev not found")
    })
    public ResponseEntity<KegiatanMonev> update(
            @Parameter(description = "Kegiatan monev ID") @PathVariable UUID id,
            @RequestBody KegiatanMonevDto kegiatanMonevDto) {

        KegiatanMonev updated = kegiatanMonevService.update(id, kegiatanMonevDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a kegiatan monev", description = "Delete a kegiatan monitoring dan evaluasi entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kegiatan monev deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Kegiatan monev not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Kegiatan monev ID") @PathVariable UUID id) {

        kegiatanMonevService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple PDFs for a Kegiatan Monev")
    public ResponseEntity<?> uploadPdfs(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KegiatanMonevPdf> uploadedPdfs = kegiatanMonevService.uploadPdfs(id, files);
            return ResponseEntity.ok(uploadedPdfs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/pdfs")
    @Operation(summary = "Menghapus PDF-PDF tertentu dari Kegiatan Monev")
    public ResponseEntity<KegiatanMonev> deletePdfs(
            @PathVariable UUID id,
            @RequestBody List<UUID> pdfIds) throws Exception {
        return ResponseEntity.ok(kegiatanMonevService.deletePdfs(id, pdfIds));
    }

    @GetMapping("/{monevId}/pdfs/{pdfId}/download")
    @Operation(summary = "Download PDF Kegiatan Monev")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable UUID monevId, @PathVariable UUID pdfId) {
        try {
            // Dapatkan data PDF dari service
            byte[] pdfData = kegiatanMonevService.viewPdf(monevId, pdfId);

            // Dapatkan informasi PDF untuk contentType
            KegiatanMonevPdf pdf = kegiatanMonevService.getPdfById(pdfId);

            // Validasi PDF tersebut milik kegiatan monev yang dimaksud
            if (!pdf.getKegiatanMonev().getId().equals(monevId)) {
                return ResponseEntity.notFound().build();
            }

            // Buat response dengan header Content-Type yang sesuai
            // Gunakan "attachment" untuk menandakan browser harus mendownload file
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(pdf.getContentType()))
                    .header("Content-Disposition", "attachment; filename=\"" + pdf.getNamaAsli() + "\"")
                    .body(pdfData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}