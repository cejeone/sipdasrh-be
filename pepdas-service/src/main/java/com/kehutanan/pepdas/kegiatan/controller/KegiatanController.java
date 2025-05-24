package com.kehutanan.pepdas.kegiatan.controller;

import com.kehutanan.pepdas.kegiatan.dto.KegiatanDto;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.pepdas.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.pepdas.kegiatan.model.KegiatanLokusShp;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisVideo;
import com.kehutanan.pepdas.kegiatan.service.KegiatanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/kegiatan")
@Tag(name = "Kegiatan", description = "API untuk manajemen Kegiatan")
@RequiredArgsConstructor
public class KegiatanController {

    private final KegiatanService kegiatanService;
    private final PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary = "Mendapatkan semua kegiatan dengan pagination dan filter")
    public ResponseEntity<PagedModel<EntityModel<Kegiatan>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String programName,
            @RequestParam(required = false) String namaKegiatan) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Kegiatan> kegiatanPage = kegiatanService.findAll(pageable, programName, namaKegiatan);

        PagedModel<EntityModel<Kegiatan>> pagedModel = pagedResourcesAssembler.toModel(kegiatanPage);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data kegiatan berdasarkan ID")
    public ResponseEntity<Kegiatan> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(kegiatanService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Membuat data kegiatan baru")
    public ResponseEntity<Kegiatan> create(@Valid @RequestBody KegiatanDto kegiatanDto) {
        return ResponseEntity.ok(kegiatanService.create(kegiatanDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data kegiatan")
    public ResponseEntity<Kegiatan> update(
            @PathVariable UUID id,
            @Valid @RequestBody KegiatanDto kegiatanDto) {
        return ResponseEntity.ok(kegiatanService.update(id, kegiatanDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Menghapus data kegiatan")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        kegiatanService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/shps", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Unggah multiple SHPs Untuk Rancangan Teknis")
    public ResponseEntity<?> uploadShps(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(kegiatanService.addFilesShp(id, files));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/shps")
    @Operation(summary = "Menghapus SHP-SHP tertentu dari Rancangan Teknis")
    public ResponseEntity<?> deleteShps(
            @PathVariable UUID id,
            @RequestBody List<UUID> shpIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteFilesShp(id, shpIds));
    }

    @GetMapping("/{kegiatanId}/shp/{shpId}/download")
    @Operation(summary = "Download SHP Rancangan Teknis")
    public ResponseEntity<byte[]> downloadShp(@PathVariable UUID kegiatanId, @PathVariable UUID shpId)
            throws Exception {
        return kegiatanService.downloadFileShp(kegiatanId, shpId);

    }

    @PostMapping(value = "/{id}/rantek-pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Unggah multiple PDFs Untuk Rancangan Teknis")
    public ResponseEntity<?> uploadPdf(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(kegiatanService.addFilesRancanganTeknisPdf(id, files));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/rantek-pdfs")
    @Operation(summary = "Menghapus PDF-PDF tertentu dari Rancangan Teknis")
    public ResponseEntity<?> deletePdf(
            @PathVariable UUID id,
            @RequestBody List<UUID> pdfIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteFilesRancanganTeknisPdf(id, pdfIds));
    }

    @GetMapping("/{kegiatanId}/rantek-pdf/{pdfId}/download")
    @Operation(summary = "Download PDF Rancangan Teknis")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable UUID kegiatanId, @PathVariable UUID pdfId)
            throws Exception {
        return kegiatanService.downloadFileRancanganTeknisPdf(kegiatanId, pdfId);

    }

    @PostMapping(value = "/{id}/rantek-fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Unggah multiple Fotos Untuk Rancangan Teknis")
    public ResponseEntity<?> uploadImage(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(kegiatanService.addFilesRancanganTeknisImage(id, files));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload Foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/rantek-fotos")
    @Operation(summary = "Menghapus Foto-Foto tertentu dari Rancangan Teknis")
    public ResponseEntity<?> deleteImage(
            @PathVariable UUID id,
            @RequestBody List<UUID> imageIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteFilesRancanganTeknisImage(id, imageIds));
    }

    @GetMapping("/{kegiatanId}/rantek-foto/{imgId}/view")
    @Operation(summary = "View Foto Rancangan Teknis")
    public ResponseEntity<byte[]> ViewImage(@PathVariable UUID kegiatanId, @PathVariable UUID imgId)
            throws Exception {

        return kegiatanService.downloadFileRancanganTeknisImage(kegiatanId, imgId);

    }

    @PostMapping(value = "/{id}/rantek-videos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Unggah multiple video Untuk Rancangan Teknis")
    public ResponseEntity<?> uploadVideo(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(kegiatanService.addFilesRancanganTeknisVideo(id, files));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload Foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/rantek-videos")
    @Operation(summary = "Menghapus Video-video tertentu dari Rancangan Teknis")
    public ResponseEntity<?> deleteVideo(
            @PathVariable UUID id,
            @RequestBody List<UUID> imageIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteFilesRancanganTeknisVideo(id, imageIds));
    }

    @GetMapping("/{kegiatanId}/rantek-video/{vidId}/view")
    @Operation(summary = "View Video Rancangan Teknis")
    public ResponseEntity<byte[]> downloadVideo(@PathVariable UUID kegiatanId, @PathVariable UUID vidId)
            throws Exception {

        return kegiatanService.streamFileRancanganTeknisVideo(kegiatanId, vidId);

    }

    
    @PostMapping(value = "/{id}/kontrak-pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Unggah multiple PDFs Untuk Kontrak")
    public ResponseEntity<?> uploadKontrakPdf(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(kegiatanService.addFilesKontrakPdf(id, files));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/kontrak-pdfs")
    @Operation(summary = "Menghapus PDF-PDF tertentu dari Kontrak")
    public ResponseEntity<?> deleteKontrakPdf(
            @PathVariable UUID id,
            @RequestBody List<UUID> pdfIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteFilesKontrakPdf(id, pdfIds));
    }

    @GetMapping("/{kegiatanId}/kontrak-pdf/{pdfId}/download")
    @Operation(summary = "Download PDF Kontrak")
    public ResponseEntity<byte[]> downloadKontrakPdf(@PathVariable UUID kegiatanId, @PathVariable UUID pdfId)
            throws Exception {
        return kegiatanService.downloadFileKontrakPdf(kegiatanId, pdfId);

    }

    
    @PostMapping(value = "/{id}/dokumentasi-fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Unggah multiple Fotos Untuk Dokumentasi")
    public ResponseEntity<?> uploadDokumentasiImage(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(kegiatanService.addFilesDokumentasiImage(id, files));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload Foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/dokumentasi-fotos")
    @Operation(summary = "Menghapus Foto-Foto tertentu dari Dokumentasi")
    public ResponseEntity<?> deleteDokumentasiImage(
            @PathVariable UUID id,
            @RequestBody List<UUID> imageIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteFilesDokumentasiImage(id, imageIds));
    }

    @GetMapping("/{kegiatanId}/dokumentasi-foto/{imgId}/view")
    @Operation(summary = "View Foto Dokumentasi")
    public ResponseEntity<byte[]> ViewDokumentasiImage(@PathVariable UUID kegiatanId, @PathVariable UUID imgId)
            throws Exception {

        return kegiatanService.downloadFileDokumentasiImage(kegiatanId, imgId);

    }

    @PostMapping(value = "/{id}/dokumentasi-videos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Unggah multiple video Untuk Dokumentasi")
    public ResponseEntity<?> uploadDokumentasiVideo(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(kegiatanService.addFilesDokumentasiVideo(id, files));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload Foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/dokumentasi-videos")
    @Operation(summary = "Menghapus Video-video tertentu dari Dokumentasi")
    public ResponseEntity<?> deleteDokumentasiVideo(
            @PathVariable UUID id,
            @RequestBody List<UUID> videoIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteFilesDokumentasiVideo(id, videoIds));
    }

    @GetMapping("/{kegiatanId}/dokumentasi-video/{vidId}/view")
    @Operation(summary = "View Video Dokumentasi")
    public ResponseEntity<byte[]> downloadDokumentasiVideo(@PathVariable UUID kegiatanId, @PathVariable UUID vidId)
            throws Exception {

        return kegiatanService.streamFileDokumentasiVideo(kegiatanId, vidId);

    }
    
}