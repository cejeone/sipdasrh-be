package com.kehutanan.pepdas.dokumen.service;

import com.kehutanan.pepdas.dokumen.dto.DokumenDto;
import com.kehutanan.pepdas.dokumen.model.Dokumen;
import com.kehutanan.pepdas.dokumen.model.DokumenFile;
import com.kehutanan.pepdas.dokumen.repository.DokumenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;

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

    public Page<Dokumen> findAll(Pageable pageable) {
        return dokumenRepository.findAll(pageable);
    }

    public Dokumen findById(UUID id) {
        return dokumenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dokumen tidak ditemukan dengan id: " + id));
    }

    @Transactional
    public Dokumen create(DokumenDto dokumenDto) throws Exception {

        Dokumen dokumen = new Dokumen();
        dokumen.setTipe(dokumenDto.getTipe());
        dokumen.setNamaDokumen(dokumenDto.getNamaDokumen());
        dokumen.setStatus(dokumenDto.getStatus());
        dokumen.setKeterangan(dokumenDto.getKeterangan());
        dokumen.setUploadedAt(LocalDateTime.now());

        return dokumenRepository.save(dokumen);
    }

    @Transactional
    public Dokumen update(UUID id,DokumenDto dokumenDto) throws Exception {

        Dokumen dokumen = findById(id);

        // Update metadata
        dokumen.setTipe(dokumenDto.getTipe());
        dokumen.setNamaDokumen(dokumenDto.getNamaDokumen());
        dokumen.setStatus(dokumenDto.getStatus());
        dokumen.setKeterangan(dokumenDto.getKeterangan());

        return dokumenRepository.save(dokumen);
    }

    @Transactional
    public void delete(UUID id) throws Exception {
        Dokumen dokumen = findById(id);
        dokumenRepository.delete(dokumen);
    }

    public String getFileUrl(UUID dokumenId, UUID fileId) throws Exception {
        Dokumen dokumen = findById(dokumenId);
        DokumenFile file = dokumen.getFiles().stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File tidak ditemukan"));

        return minioService.getPresignedUrl(file.getNamaFile());
    }

    /**
     * Downloads a file from the document and returns it as a response
     * 
     * @param dokumenId Document ID
     * @param fileId    File ID
     * @return ResponseEntity with the file for download
     * @throws Exception If file cannot be retrieved
     */
    public ResponseEntity<byte[]> downloadFile(UUID dokumenId, UUID fileId) throws Exception {
        Dokumen dokumen = findById(dokumenId);
        DokumenFile file = dokumen.getFiles().stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File tidak ditemukan"));

        byte[] data = minioService.getFileData(file.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(file.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @Transactional
    public Dokumen addFiles(UUID id, List<MultipartFile> files) throws Exception {
        Dokumen dokumen = findById(id);

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
    public Dokumen deleteFiles(UUID dokumenId, List<UUID> fileIds) throws Exception {
        Dokumen dokumen = findById(dokumenId);
        System.out.println("Deleting files with IDs: " + fileIds);
        dokumen.getFiles().removeIf(file -> {
            System.out.println("Checking file with ID: " + file.getId());
            if (fileIds.contains(file.getId())) {
                System.out.println("Deleting file with ID: " + file.getId());
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

        return dokumenRepository.save(dokumen);
    }

    public Page<Dokumen> findByFilters(String namaDokumen, List<String> bpdas, Pageable pageable) {
          Specification<Dokumen> spec = Specification.where(null);
    
    if (namaDokumen != null && !namaDokumen.isEmpty()) {
        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaDokumen")),
                    "%" + namaDokumen.toLowerCase() + "%"
                ));
    }
    
    if (bpdas != null && !bpdas.isEmpty()) {
        spec = spec.and((root, query, criteriaBuilder) ->
                root.get("bpdas").in(bpdas));
    }
    
    return dokumenRepository.findAll(spec, pageable);
    }
}