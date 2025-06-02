package com.kehutanan.pepdas.dokumen.service.impl;

import com.kehutanan.pepdas.dokumen.repository.DokumenRepository;
import com.kehutanan.pepdas.dokumen.service.DokumenService;
import com.kehutanan.pepdas.dokumen.dto.DokumenDTO;
import com.kehutanan.pepdas.dokumen.model.Dokumen;
import com.kehutanan.pepdas.dokumen.model.DokumenFile;
import com.kehutanan.pepdas.util.FileValidationUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.pepdas.common.service.MinioStorageService;
import jakarta.persistence.EntityNotFoundException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DokumenServiceImpl implements DokumenService {
    private final DokumenRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Dokumen/";

    @Autowired
    public DokumenServiceImpl(DokumenRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public Page<Dokumen> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Dokumen> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "rh_dokumenCache", key = "#id")
    public DokumenDTO findDTOById(Long id) {
        Dokumen dokumen = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dokumen not found with id: " + id));

        return new DokumenDTO(dokumen);
    }

    @Override
    public Dokumen findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dokumen not found with id: " + id));
    }

    @Override
    public Dokumen save(Dokumen dokumen) {
        return repository.save(dokumen);
    }

    @Override
    @CachePut(value = "rh_dokumenCache", key = "#id")
    public Dokumen update(Long id, Dokumen dokumen) {
        return repository.save(dokumen);
    }

    @Override
    @CacheEvict(value = "rh_dokumenCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Dokumen dokumen = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dokumen not found with id: " + id));

        // Delete all files from MinIO storage
        if (dokumen.getDokumenFiles() != null) {
            for (DokumenFile file : dokumen.getDokumenFiles()) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete file: " + file.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    @Override
    public Page<Dokumen> findByFilters(String namaDokumen, Long tipeId, Long statusId, Pageable pageable) {
        Specification<Dokumen> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaDokumen if provided
        if (namaDokumen != null && !namaDokumen.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaDokumen")),
                    "%" + namaDokumen.toLowerCase() + "%"));
        }

        // Add equals filter for tipeId if provided
        if (tipeId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("tipe").get("id"), tipeId));
        }

        // Add equals filter for statusId if provided
        if (statusId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("status").get("id"), statusId));
        }

        return repository.findAll(spec, pageable);
    }

    @Override
    @CacheEvict(value = "rh_dokumenCache", allEntries = true, beforeInvocation = true)
    public Dokumen uploadDokumenFiles(Long id, List<MultipartFile> files) {
        Dokumen dokumen = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dokumen not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + dokumen.getId() + "/";
            String filePath = folderName + fileName;

            DokumenFile dokumenFile = new DokumenFile();
            dokumenFile.setId(idfile);
            dokumenFile.setDokumen(dokumen);
            dokumenFile.setNamaAsli(file.getOriginalFilename());
            dokumenFile.setNamaFile(fileName);
            dokumenFile.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            dokumenFile.setViewUrl("/file/view?fileName=" + encodedPath);
            dokumenFile.setDownloadUrl("/file/download?fileName=" + encodedPath);
            dokumenFile.setContentType(file.getContentType());
            dokumenFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            dokumenFile.setContentType(file.getContentType());
            dokumenFile.setUploadedAt(LocalDateTime.now());

            dokumen.getDokumenFiles().add(dokumenFile);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                dokumen.getDokumenFiles().removeIf(df -> df.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        dokumen.setUkuranDokumen(recalculateDokumenSize(dokumen));

        return repository.save(dokumen);
    }

    @Override
    @Transactional
    @CacheEvict(value = "rh_dokumenCache", allEntries = true, beforeInvocation = true)
    public Dokumen deleteDokumenFiles(Long id, List<String> uuidFiles) {
        Dokumen dokumen = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dokumen not found with id: " + id));

        dokumen.getDokumenFiles().removeIf(file -> {
            if (uuidFiles.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this file from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this file in the list
            }
        });
        dokumen.setUkuranDokumen(recalculateDokumenSize(dokumen));
        return repository.save(dokumen);
    }

    public double recalculateDokumenSize(Dokumen dokumen) {
        double totalSize = dokumen.getDokumenFiles().stream()
                .mapToDouble(DokumenFile::getUkuranMb)
                .sum();
        return totalSize;
    }
}