package com.kehutanan.rh.kegiatan.controller;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekFoto;
import com.kehutanan.rh.bimtek.model.BimtekPdf;
import com.kehutanan.rh.kegiatan.dto.KegiatanDto;
import com.kehutanan.rh.kegiatan.dto.KegiatanDtoDetail;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.rh.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisVideo;
import com.kehutanan.rh.kegiatan.model.KegiatanSerahTerimaPdf;
import com.kehutanan.rh.kegiatan.service.KegiatanService;
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
    private final PagedResourcesAssembler<KegiatanDto> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary = "Mendapatkan semua kegiatan dengan pagination")
    public ResponseEntity<PagedModel<EntityModel<KegiatanDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<KegiatanDto> kegiatanPage = kegiatanService.findAll(pageable);
        PagedModel<EntityModel<KegiatanDto>> pagedModel = pagedResourcesAssembler.toModel(kegiatanPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mendapatkan data kegiatan berdasarkan ID")
    public ResponseEntity<KegiatanDtoDetail> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(kegiatanService.findByIdDtoMin(id));
    }

    @PostMapping
    @Operation(summary = "Membuat data kegiatan baru")
    public ResponseEntity<KegiatanDto> create(@Valid @RequestBody KegiatanDto kegiatanDto) {
        return ResponseEntity.ok(kegiatanService.create(kegiatanDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Memperbarui data kegiatan")
    public ResponseEntity<KegiatanDto> update(
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

    @PostMapping(value = "/{id}/rancangan-teknis/fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple foto rancangan teknis untuk sebuah Kegiatan ")
    public ResponseEntity<?> uploadRancanganTeknisFotos(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KegiatanRancanganTeknisFoto> uploadedFotos = kegiatanService.uploadKegiatanRancanganTeknisFotos(id,
                    files);
            return ResponseEntity.ok(uploadedFotos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload foto rancangan teknis: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/rancangan-teknis/pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple PDFs rancangan teknis untuk sebuah Kegiatan ")
    public ResponseEntity<?> uploadRancanganTeknisPdfs(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KegiatanRancanganTeknisPdf> uploadedPdfs = kegiatanService.uploadKegiatanRancanganTeknisPdfs(id,
                    files);
            return ResponseEntity.ok(uploadedPdfs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/rancangan-teknis/videos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple Videos rancangan teknis untuk sebuah Kegiatan ")
    public ResponseEntity<?> uploadRancanganTeknisVideos(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KegiatanRancanganTeknisVideo> uploadedVideos = kegiatanService.uploadKegiatanRancanganTeknisVideos(id,
                    files);
            return ResponseEntity.ok(uploadedVideos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/kontrak/pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple PDFs rancangan teknis untuk sebuah Kegiatan ")
    public ResponseEntity<?> uploadKontrakPdfs(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KegiatanKontrakPdf> uploadedPdfs = kegiatanService.uploadKegiatanKontrakPdfs(id, files);
            return ResponseEntity.ok(uploadedPdfs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload PDF: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/dokumentasi/fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple foto dokumentasi untuk sebuah Kegiatan ")
    public ResponseEntity<?> uploadDokumentasiFotos(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KegiatanDokumentasiFoto> uploadedFotos = kegiatanService.uploadKegiatanDokumentasiFotos(id,
                    files);
            return ResponseEntity.ok(uploadedFotos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload foto rancangan teknis: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/dokumentasi/videos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple video dokumentasi untuk sebuah Kegiatan ")
    public ResponseEntity<?> uploadDokumentasiVideos(
            @PathVariable UUID id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            List<KegiatanDokumentasiVideo> uploadedFotos = kegiatanService.uploadKegiatanDokumentasiVideos(id,
                    files);
            return ResponseEntity.ok(uploadedFotos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload foto rancangan teknis: " + e.getMessage());
        }
    }

    @GetMapping("/{kegiatanId}/rancangan-teknis/fotos/{fotoId}/view")
    @Operation(summary = "Menampilkan foto rancangan teknis kegiatan")
    public ResponseEntity<byte[]> viewRancanganTeknisFoto(
            @PathVariable UUID kegiatanId, @PathVariable UUID fotoId) {
        try {
            // Dapatkan foto dari service
            byte[] imageData = kegiatanService.viewRancanganTeknisFoto(kegiatanId, fotoId);

            // Dapatkan informasi foto untuk contentType
            KegiatanRancanganTeknisFoto foto = kegiatanService.getRancanganTeknisFotoById(fotoId);

            // Buat response dengan header Content-Type yang sesuai
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(foto.getContentType()))
                    .body(imageData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error viewing rancangan teknis foto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{kegiatanId}/rancangan-teknis/pdfs/{pdfId}/view")
    @Operation(summary = "Menampilkan PDF rancangan teknis kegiatan")
    public ResponseEntity<byte[]> viewRancanganTeknisPdf(
            @PathVariable UUID kegiatanId, @PathVariable UUID pdfId) {
        try {
            // Dapatkan PDF dari service
            byte[] pdfData = kegiatanService.viewRancanganTeknisPdf(kegiatanId, pdfId);

            // Dapatkan informasi PDF untuk contentType
            KegiatanRancanganTeknisPdf pdf = kegiatanService.getRancanganTeknisPdfById(pdfId);

            // Buat response dengan header Content-Type yang sesuai
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(pdf.getContentType()))
                    .body(pdfData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error viewing rancangan teknis PDF: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{kegiatanId}/rancangan-teknis/videos/{videoId}/view")
    @Operation(summary = "Menampilkan video rancangan teknis kegiatan")
    public ResponseEntity<byte[]> viewRancanganTeknisVideo(
            @PathVariable UUID kegiatanId, @PathVariable UUID videoId) {
        try {
            // Dapatkan video dari service
            byte[] videoData = kegiatanService.viewRancanganTeknisVideo(kegiatanId, videoId);

            // Dapatkan informasi video untuk contentType
            KegiatanRancanganTeknisVideo video = kegiatanService.getRancanganTeknisVideoById(videoId);

            // Buat response dengan header Content-Type yang sesuai
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(video.getContentType()))
                    .body(videoData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error viewing rancangan teknis video: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{kegiatanId}/kontrak/pdfs/{pdfId}/view")
    @Operation(summary = "Menampilkan PDF kontrak kegiatan")
    public ResponseEntity<byte[]> viewKontrakPdf(
            @PathVariable UUID kegiatanId, @PathVariable UUID pdfId) {
        try {
            // Dapatkan PDF dari service
            byte[] pdfData = kegiatanService.viewKontrakPdf(kegiatanId, pdfId);

            // Dapatkan informasi PDF untuk contentType
            KegiatanKontrakPdf pdf = kegiatanService.getKontrakPdfById(pdfId);

            // Buat response dengan header Content-Type yang sesuai
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(pdf.getContentType()))
                    .body(pdfData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error viewing kontrak PDF: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{kegiatanId}/dokumentasi/fotos/{fotoId}/view")
    @Operation(summary = "Menampilkan foto dokumentasi kegiatan")
    public ResponseEntity<byte[]> viewDokumentasiFoto(
            @PathVariable UUID kegiatanId, @PathVariable UUID fotoId) {
        try {
            // Dapatkan foto dari service
            byte[] imageData = kegiatanService.viewDokumentasiFoto(kegiatanId, fotoId);

            // Dapatkan informasi foto untuk contentType
            KegiatanDokumentasiFoto foto = kegiatanService.getDokumentasiFotoById(fotoId);

            // Buat response dengan header Content-Type yang sesuai
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(foto.getContentType()))
                    .body(imageData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error viewing dokumentasi foto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{kegiatanId}/dokumentasi/videos/{videoId}/view")
    @Operation(summary = "Menampilkan video dokumentasi kegiatan")
    public ResponseEntity<byte[]> viewDokumentasiVideo(
            @PathVariable UUID kegiatanId, @PathVariable UUID videoId) {
        try {
            // Dapatkan video dari service
            byte[] videoData = kegiatanService.viewDokumentasiVideo(kegiatanId, videoId);

            // Dapatkan informasi video untuk contentType
            KegiatanDokumentasiVideo video = kegiatanService.getDokumentasiVideoById(videoId);

            // Buat response dengan header Content-Type yang sesuai
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(video.getContentType()))
                    .body(videoData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error viewing dokumentasi video: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}/rancangan-teknis/fotos")
    @Operation(summary = "Menghapus foto-foto rancangan teknis dari Kegiatan")
    public ResponseEntity<KegiatanDto> deleteRancanganTeknisFotos(
            @PathVariable UUID id,
            @RequestBody List<UUID> fotoIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteRancanganTeknisFotos(id, fotoIds));
    }

    @DeleteMapping("/{id}/rancangan-teknis/pdfs")
    @Operation(summary = "Menghapus PDF rancangan teknis dari Kegiatan")
    public ResponseEntity<KegiatanDto> deleteRancanganTeknisPdfs(
            @PathVariable UUID id,
            @RequestBody List<UUID> pdfIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteRancanganTeknisPdfs(id, pdfIds));
    }

    @DeleteMapping("/{id}/rancangan-teknis/videos")
    @Operation(summary = "Menghapus video rancangan teknis dari Kegiatan")
    public ResponseEntity<KegiatanDto> deleteRancanganTeknisVideos(
            @PathVariable UUID id,
            @RequestBody List<UUID> videoIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteRancanganTeknisVideos(id, videoIds));
    }

    @DeleteMapping("/{id}/kontrak/pdfs")
    @Operation(summary = "Menghapus PDF kontrak dari Kegiatan")
    public ResponseEntity<KegiatanDto> deleteKontrakPdfs(
            @PathVariable UUID id,
            @RequestBody List<UUID> pdfIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteKontrakPdfs(id, pdfIds));
    }

    @DeleteMapping("/{id}/dokumentasi/fotos")
    @Operation(summary = "Menghapus foto-foto dokumentasi dari Kegiatan")
    public ResponseEntity<KegiatanDto> deleteDokumentasiFotos(
            @PathVariable UUID id,
            @RequestBody List<UUID> fotoIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteDokumentasiFotos(id, fotoIds));
    }

    @DeleteMapping("/{id}/dokumentasi/videos")
    @Operation(summary = "Menghapus video dokumentasi dari Kegiatan")
    public ResponseEntity<KegiatanDto> deleteDokumentasiVideos(
            @PathVariable UUID id,
            @RequestBody List<UUID> videoIds) throws Exception {
        return ResponseEntity.ok(kegiatanService.deleteDokumentasiVideos(id, videoIds));
    }
}