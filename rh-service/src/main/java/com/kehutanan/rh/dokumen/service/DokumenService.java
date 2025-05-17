package com.kehutanan.rh.dokumen.service;

import com.kehutanan.rh.dokumen.model.Dokumen;
import com.kehutanan.rh.dokumen.model.DokumenFile;
import com.kehutanan.rh.dokumen.repository.DokumenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DokumenService {

    private final DokumenRepository dokumenRepository;
    private final MinioService minioService;

    @Autowired
    public DokumenService(DokumenRepository dokumenRepository, MinioService minioService) {
        this.dokumenRepository = dokumenRepository;
        this.minioService = minioService;
    }

    public Page<Dokumen> findAll(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return dokumenRepository.findByNamaDokumenContainingIgnoreCase(search, pageable);
        }
        return dokumenRepository.findAll(pageable);
    }

    public Dokumen findById(Long id) {
        return dokumenRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Dokumen tidak ditemukan dengan id: " + id));
    }

    @Transactional
    public Dokumen create(List<MultipartFile> files, String tipe, String namaDokumen, 
            String status, String keterangan) throws Exception {
        
        Dokumen dokumen = new Dokumen();
        dokumen.setTipe(tipe);
        dokumen.setNamaDokumen(namaDokumen);
        dokumen.setStatus(status);
        dokumen.setKeterangan(keterangan);
        dokumen.setUploadedAt(LocalDateTime.now());

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            
            minioService.uploadFile(fileName, file.getInputStream(), file.getContentType());
            
            DokumenFile dokumenFile = new DokumenFile();
            dokumenFile.setDokumen(dokumen);
            dokumenFile.setNamaFile(fileName);
            dokumenFile.setNamaAsli(file.getOriginalFilename());
            dokumenFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            dokumenFile.setContentType(file.getContentType());
            dokumenFile.setUploadedAt(LocalDateTime.now());
            
            dokumen.getFiles().add(dokumenFile);
        }

        return dokumenRepository.save(dokumen);
    }

    @Transactional
    public Dokumen update(Long id, List<MultipartFile> newFiles, List<Long> deleteFileIds,
            String tipe, String namaDokumen, String status, String keterangan) throws Exception {
        
        Dokumen dokumen = findById(id);

        // Delete files if requested
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            dokumen.getFiles().removeIf(file -> {
                if (deleteFileIds.contains(file.getId())) {
                    try {
                        minioService.deleteFile(file.getNamaFile());
                        return true;
                    } catch (Exception e) {
                        log.error("Error deleting file: {}", file.getNamaFile(), e);
                        return false;
                    }
                }
                return false;
            });
        }

        // Add new files
        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                
                minioService.uploadFile(fileName, file.getInputStream(), file.getContentType());
                
                DokumenFile dokumenFile = new DokumenFile();
                dokumenFile.setDokumen(dokumen);
                dokumenFile.setNamaFile(fileName);
                dokumenFile.setNamaAsli(file.getOriginalFilename());
                dokumenFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
                dokumenFile.setContentType(file.getContentType());
                dokumenFile.setUploadedAt(LocalDateTime.now());
                
                dokumen.getFiles().add(dokumenFile);
            }
        }

        // Update metadata
        dokumen.setTipe(tipe);
        dokumen.setNamaDokumen(namaDokumen);
        dokumen.setStatus(status);
        dokumen.setKeterangan(keterangan);

        return dokumenRepository.save(dokumen);
    }

    @Transactional
    public void delete(Long id) throws Exception {
        Dokumen dokumen = findById(id);
        
        // Delete all files from MinIO
        for (DokumenFile file : dokumen.getFiles()) {
            minioService.deleteFile(file.getNamaFile());
        }
        
        dokumenRepository.delete(dokumen);
    }

    public String getFileUrl(Long dokumenId, Long fileId) throws Exception {
        Dokumen dokumen = findById(dokumenId);
        DokumenFile file = dokumen.getFiles().stream()
            .filter(f -> f.getId().equals(fileId))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("File tidak ditemukan"));
            
        return minioService.getPresignedUrl(file.getNamaFile());
    }
}