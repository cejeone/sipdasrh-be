package com.kehutanan.rh.bimtek.controller;

import com.kehutanan.rh.bimtek.dto.BimtekDto;
import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekFoto;
import com.kehutanan.rh.bimtek.model.BimtekPdf;
import com.kehutanan.rh.bimtek.model.BimtekVideo;
import com.kehutanan.rh.bimtek.service.BimtekService;
import com.kehutanan.rh.dokumen.model.Dokumen;
import com.kehutanan.rh.dokumen.service.DokumenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

@RestController
@RequestMapping("/api/bimtek")
@Tag(name = "Bimtek", description = "API untuk manajemen Bimbingan Teknis")
@RequiredArgsConstructor
public class BimtekController {

    private final BimtekService bimtekService;
    private final PagedResourcesAssembler<Bimtek> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary = "Mendapatkan semua bimtek dengan pagination dan filter")
    public ResponseEntity<PagedModel<EntityModel<Bimtek>>> getAll(
            @RequestParam(required = false) String namaBimtek,
            @RequestParam(required = false) String subjek,
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Bimtek> bimtekPage;
        
        // Use filtered search if any filter parameter is provided, otherwise get all
        if (namaBimtek != null || subjek != null || (bpdas != null && !bpdas.isEmpty())) {
            bimtekPage = bimtekService.findByFilters(namaBimtek, subjek, bpdas, pageable);
        } else {
            bimtekPage = bimtekService.findAll(pageable);
        }
        
        PagedModel<EntityModel<Bimtek>> pagedModel = pagedResourcesAssembler.toModel(bimtekPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data bimtek berdasarkan ID")
    public ResponseEntity<Bimtek> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(bimtekService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Membuat data bimtek baru")
    public ResponseEntity<Bimtek> create(@Valid @RequestBody BimtekDto bimtekDto) {
        return ResponseEntity.ok(bimtekService.create(bimtekDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data bimtek")
    public ResponseEntity<Bimtek> update(
            @PathVariable UUID id,
            @Valid @RequestBody BimtekDto bimtekDto) {
        return ResponseEntity.ok(bimtekService.update(id, bimtekDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data bimtek")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        bimtekService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple photos for a Bimtek")
    public ResponseEntity<?> uploadFotos(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<BimtekFoto> uploadedFotos = bimtekService.uploadFotos(id, files);
            return ResponseEntity.ok(uploadedFotos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/files")
    @Operation(summary = "Menghapus foto-foto tertentu dari Bimtek")
    public ResponseEntity<Bimtek> deleteFiles(
            @PathVariable UUID id,
            @RequestBody List<UUID> fotoIds) throws Exception {
        return ResponseEntity.ok(bimtekService.deleteFotos(id, fotoIds));
    }

    @GetMapping("/{bimtekId}/fotos/{fotoId}/view")
    @Operation(summary = "Menampilkan foto Bimtek")
    public ResponseEntity<byte[]> viewFoto(
            @PathVariable UUID bimtekId, @PathVariable UUID fotoId) {

        try {
            // Dapatkan foto dari service
            byte[] imageData = bimtekService.viewFoto(bimtekId, fotoId);

            // Dapatkan informasi foto untuk contentType
            BimtekFoto foto = bimtekService.getFotoById(fotoId);

            // Buat response dengan header Content-Type yang sesuai
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(foto.getContentType()))
                    .body(imageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping(value = "/{id}/videos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple videos for a Bimtek")
    public ResponseEntity<?> uploadVideos(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<BimtekVideo> uploadedVideos = bimtekService.uploadVideos(id, files);
            return ResponseEntity.ok(uploadedVideos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload video: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/videos")
    @Operation(summary = "Menghapus video-video tertentu dari Bimtek")
    public ResponseEntity<Bimtek> deleteVideos(
            @PathVariable UUID id,
            @RequestBody List<UUID> videoIds) throws Exception {
        return ResponseEntity.ok(bimtekService.deleteVideos(id, videoIds));
    }

    @GetMapping("/{bimtekId}/videos/{videoId}/view")
    @Operation(summary = "Menampilkan video Bimtek")
    public ResponseEntity<StreamingResponseBody> viewVideo(
            @PathVariable UUID bimtekId,
            @PathVariable UUID videoId,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {

        try {
            // Dapatkan informasi video untuk contentType
            BimtekVideo video = bimtekService.getVideoById(videoId);

            // Validasi video tersebut milik bimtek yang dimaksud
            if (!video.getBimtek().getId().equals(bimtekId)) {
                return ResponseEntity.notFound().build();
            }

            // Create a streaming response that reads from the MinIO/storage
            StreamingResponseBody responseBody = outputStream -> {
                try (InputStream videoStream = bimtekService.getVideoStream(bimtekId, videoId)) {
                    IOUtils.copy(videoStream, outputStream);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.parseMediaType(video.getContentType()))
                    // Add content-disposition with filename for better browser handling
                    .header("Content-Disposition", "inline; filename=\"" + video.getNamaAsli() + "\"")
                    // Add accept-ranges header to indicate server supports range requests
                    .header("Accept-Ranges", "bytes")
                    .body(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/{id}/pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple PDFs for a Bimtek")
    public ResponseEntity<?> uploadPdfs(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<BimtekPdf> uploadedPdfs = bimtekService.uploadPdfs(id, files);
            return ResponseEntity.ok(uploadedPdfs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/pdfs")
    @Operation(summary = "Menghapus PDF-PDF tertentu dari Bimtek")
    public ResponseEntity<Bimtek> deletePdfs(
            @PathVariable UUID id,
            @RequestBody List<UUID> pdfIds) throws Exception {
        return ResponseEntity.ok(bimtekService.deletePdfs(id, pdfIds));
    }

    @GetMapping("/{bimtekId}/pdfs/{pdfId}/download")
    @Operation(summary = "Download PDF Bimtek")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable UUID bimtekId, @PathVariable UUID pdfId) {
        try {
            // Dapatkan data PDF dari service
            byte[] pdfData = bimtekService.viewPdf(bimtekId, pdfId);

            // Dapatkan informasi PDF untuk contentType
            BimtekPdf pdf = bimtekService.getPdfById(pdfId);

            // Validasi PDF tersebut milik bimtek yang dimaksud
            if (!pdf.getBimtek().getId().equals(bimtekId)) {
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
