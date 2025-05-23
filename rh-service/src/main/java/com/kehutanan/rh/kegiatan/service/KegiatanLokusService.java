package com.kehutanan.rh.kegiatan.service;

import com.kehutanan.rh.kegiatan.model.KegiatanLokus;
import com.kehutanan.rh.kegiatan.model.KegiatanLokusShp;
import com.kehutanan.rh.kegiatan.repository.KegiatanLokusRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanLokusShpRepository;
import com.kehutanan.rh.kegiatan.service.MinioKegiatanService;
import com.kehutanan.rh.util.FileValidationUtil;
import com.kehutanan.rh.kegiatan.controller.KegiatanLokusController;
import com.kehutanan.rh.kegiatan.dto.KegiatanLokusDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class KegiatanLokusService {

    private final KegiatanLokusRepository kegiatanLokusRepository;
    private final KegiatanLokusShpRepository kegiatanLokusShpRepository;
    private final MinioKegiatanService minioKegiatanService;
    private final FileValidationUtil fileValidationUtil;

    public KegiatanLokusService(KegiatanLokusRepository kegiatanLokusRepository,
            KegiatanLokusShpRepository kegiatanLokusShpRepository,
            MinioKegiatanService minioKegiatanService,FileValidationUtil fileValidationUtil) {
        this.kegiatanLokusRepository = kegiatanLokusRepository;
        this.kegiatanLokusShpRepository = kegiatanLokusShpRepository;
        this.minioKegiatanService = minioKegiatanService;
        this.fileValidationUtil = fileValidationUtil;
    }

    public PagedModel<EntityModel<KegiatanLokus>> findAll(String KegiatanId, String search, Pageable pageable,
            PagedResourcesAssembler<KegiatanLokus> assembler) {

        Page<KegiatanLokus> page;

        // Create base specification
        Specification<KegiatanLokus> spec = Specification.where(null);

        // Add program ID filter if provided
        if (KegiatanId != null && !KegiatanId.isEmpty()) {
            try {
                UUID programUuid = UUID.fromString(KegiatanId);
                spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("kegiatan").get("id"),
                        programUuid));
            } catch (IllegalArgumentException e) {
                // Handle invalid UUID format
                throw new IllegalArgumentException("Invalid Program ID format");
            }
        }

        // Add search filter if provided
        if (search != null && !search.isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern)));
        }

         page = kegiatanLokusRepository.findAll(spec, pageable);

        return assembler.toModel(page, entity -> EntityModel.of(entity)
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(KegiatanLokusController.class)
                        .findById(entity.getId())).withSelfRel()));
    }

    public KegiatanLokus findById(UUID id) {
        return kegiatanLokusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Kegiatan Lokus not found with id: " + id));
    }

    @Transactional
    public KegiatanLokus create(KegiatanLokus kegiatanLokus) throws Exception {
        KegiatanLokus saved = kegiatanLokusRepository.save(kegiatanLokus);
        // Return the saved entity with its ID
        return kegiatanLokusRepository.findById(saved.getId()).orElseThrow();
    }

    @Transactional
    public KegiatanLokus update(
            UUID id,
            KegiatanLokusDto kegiatanLokusDto) throws Exception {

        KegiatanLokus kegiatanLokus = findById(id);

        kegiatanLokus.setProvinsi(kegiatanLokusDto.getProvinsi());
        kegiatanLokus.setKabupatenKota(kegiatanLokusDto.getKabupatenKota());
        kegiatanLokus.setKecamatan(kegiatanLokusDto.getKecamatan());
        kegiatanLokus.setKelurahanDesa(kegiatanLokusDto.getKelurahanDesa());
        kegiatanLokus.setAlamat(kegiatanLokusDto.getAlamat());

        return kegiatanLokusRepository.save(kegiatanLokus);
    }

    @Transactional
    public void delete(UUID id) {
        KegiatanLokus kegiatanLokus = findById(id);

        // Delete all associated shapefile entries and files from Minio
        for (KegiatanLokusShp shp : kegiatanLokus.getKegiatanLokusShps()) {
            try {
                minioKegiatanService.deleteFile(shp.getNamaFile());
            } catch (Exception e) {
                // Log the error but continue with the deletion
                System.err.println("Failed to delete file from storage: " + e.getMessage());
            }
        }

        kegiatanLokusRepository.deleteById(id);
    }

    public ResponseEntity<byte[]> downloadFile(UUID lokusId, UUID shpId) throws Exception {
        KegiatanLokus kegiatanLokus = findById(lokusId);
        KegiatanLokusShp shpFile = kegiatanLokus.getKegiatanLokusShps().stream()
                .filter(f -> f.getId().equals(shpId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File tidak ditemukan"));

        byte[] data = minioKegiatanService.getFileData(shpFile.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType
                .parseMediaType(shpFile.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(shpFile.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @Transactional
    public KegiatanLokus addFiles(UUID id, List<MultipartFile> files) throws Exception {
        KegiatanLokus kegiatanLokus = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "shp");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKegiatanService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KegiatanLokusShp dokumenFile = new KegiatanLokusShp();
            dokumenFile.setKegiatanLokus(kegiatanLokus);
            dokumenFile.setNamaFile(fileName);
            dokumenFile.setNamaAsli(file.getOriginalFilename());
            dokumenFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            dokumenFile.setContentType(file.getContentType());
            dokumenFile.setUploadedAt(LocalDateTime.now());

            kegiatanLokus.getKegiatanLokusShps().add(dokumenFile);
        }

        return kegiatanLokusRepository.save(kegiatanLokus);
    }

    @Transactional
    public KegiatanLokus deleteFiles(UUID lokusId, List<UUID> fileIds) throws Exception {
        KegiatanLokus kegiatanLokus = findById(lokusId);
        System.out.println("Deleting files with IDs: " + fileIds);

        List<KegiatanLokusShp> shpsToDelete = kegiatanLokusShpRepository.findAllById(fileIds);

        for (KegiatanLokusShp shp : shpsToDelete) {
            if (shp.getKegiatanLokus().getId().equals(lokusId)) {
                try {
                    minioKegiatanService.deleteFile(shp.getNamaFile());
                    kegiatanLokusShpRepository.delete(shp);
                } catch (Exception e) {
                    System.err.println("Failed to delete file from storage: " + e.getMessage());
                    throw e;
                }
            }
        }

        return kegiatanLokusRepository.save(kegiatanLokus);
    }

}