package com.kehutanan.pepdas.serahterima.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.pepdas.common.service.MinioStorageService;
import com.kehutanan.pepdas.monev.model.Monev;
import com.kehutanan.pepdas.monev.model.MonevPdf;
import com.kehutanan.pepdas.serahterima.dto.SerahTerimaDto;
import com.kehutanan.pepdas.serahterima.model.SerahTerima;
import com.kehutanan.pepdas.serahterima.model.SerahTerimaPdf;
import com.kehutanan.pepdas.serahterima.repository.SerahTerimaRepository;
import com.kehutanan.pepdas.serahterima.repository.SerahTerimaPdfRepository;
import com.kehutanan.pepdas.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SerahTerimaService {

    private final SerahTerimaRepository serahTerimaRepository;
    private final SerahTerimaPdfRepository serahTerimaPdfRepository;
    private final  FileValidationUtil fileValidationUtil;
    private final  MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "ٍSerahTerima/";

    @Autowired
    public SerahTerimaService(SerahTerimaRepository serahTerimaRepository,
             FileValidationUtil fileValidationUtil, MinioStorageService minioStorageService, SerahTerimaPdfRepository serahTerimaPdfRepository) {
        this.serahTerimaRepository = serahTerimaRepository;
        this.serahTerimaPdfRepository = serahTerimaPdfRepository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    public Page<SerahTerima> findAll(Pageable pageable) {
        return serahTerimaRepository.findAll(pageable);
    }

    public Optional<SerahTerima> findById(UUID id) {
        return serahTerimaRepository.findById(id);
    }

    public SerahTerima save(SerahTerimaDto serahTerimaDto) {
        SerahTerima serahTerima = new SerahTerima();

        serahTerima.setNomor(serahTerimaDto.getNomor());
        serahTerima.setKegiatan(serahTerimaDto.getKegiatan());
        serahTerima.setKontrak(serahTerimaDto.getKontrak());
        serahTerima.setTanggal(serahTerimaDto.getTanggal());
        serahTerima.setDeskripsi(serahTerimaDto.getDeskripsi());
        serahTerima.setStatus(serahTerimaDto.getStatus());
        return serahTerimaRepository.save(serahTerima);
    }

    public void deleteById(UUID id) {
        serahTerimaRepository.deleteById(id);
    }

    public Page<SerahTerima> findByFilters(String nomor, String kontrak, Pageable pageable) {
        Specification<SerahTerima> spec = Specification.where(null);

        if (nomor != null && !nomor.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nomor")),
                    "%" + nomor.toLowerCase() + "%"));
        }

        if (kontrak != null && !kontrak.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kontrak")),
                    "%" + kontrak.toLowerCase() + "%"));
        }

        return serahTerimaRepository.findAll(spec, pageable);
    }

    public SerahTerima update(UUID id, SerahTerimaDto serahTerimaDto) {
        SerahTerima serahTerima = serahTerimaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serah Terima not found with id " + id));

        serahTerima.setNomor(serahTerimaDto.getNomor());
        serahTerima.setKegiatan(serahTerimaDto.getKegiatan());
        serahTerima.setKontrak(serahTerimaDto.getKontrak());
        serahTerima.setTanggal(serahTerimaDto.getTanggal());
        serahTerima.setDeskripsi(serahTerimaDto.getDeskripsi());
        serahTerima.setStatus(serahTerimaDto.getStatus());

        return serahTerimaRepository.save(serahTerima);
    }

    /**
     * Add PDF files to a kegiatan's rancangan teknis
     * 
     * @param id    ID of the kegiatan
     * @param files List of PDF files to upload
     * @return Updated kegiatan entity
     * @throws Exception if files cannot be processed or uploaded
     */
    @Transactional
    public SerahTerima addFilesPdf(UUID id, List<MultipartFile> files) throws Exception {
        SerahTerima serahTerima = serahTerimaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serah Terima not found with id " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioStorageService.uploadFile(FOLDER_PREFIX, fileName, file.getInputStream(), file.getContentType());

            SerahTerimaPdf pdfFile = new SerahTerimaPdf();
            pdfFile.setSerahTerima(serahTerima);
            pdfFile.setNamaFile(fileName);
            pdfFile.setNamaAsli(file.getOriginalFilename());
            pdfFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            pdfFile.setContentType(file.getContentType());
            pdfFile.setUploadedAt(LocalDateTime.now());

            serahTerima.getSerahTerimaPdfs().add(pdfFile);
        }

        return serahTerimaRepository.save(serahTerima);
    }

    /**
     * Delete PDF files from a kegiatan's rancangan teknis
     * 
     * @param kegiatanId ID of the kegiatan
     * @param fileIds    List of file IDs to delete
     * @return Updated kegiatan entity
     * @throws Exception if files cannot be deleted
     */
    @Transactional
    public SerahTerima deleteFilesPdf(UUID Id, List<UUID> fileIds) throws Exception {
        SerahTerima serahTerima = serahTerimaRepository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Serah Terima not found with id " + Id));

        List<SerahTerimaPdf> pdfsToDelete = serahTerimaPdfRepository.findAllById(fileIds);

        for (SerahTerimaPdf pdf : pdfsToDelete) {
            if (pdf.getSerahTerima().getId().equals(Id)) {
                try {
                    minioStorageService.deleteFile(FOLDER_PREFIX, pdf.getNamaFile());
                    serahTerimaPdfRepository.delete(pdf);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete PDF file from storage: " + e.getMessage(), e);
                }
            }
        }

        return serahTerimaRepository.save(serahTerima);
    }

    /**
     * Download PDF file for rancangan teknis
     * 
     * @param kegiatanId ID of the kegiatan
     * @param pdfId      ID of the PDF file to download
     * @return ResponseEntity with file data and appropriate headers
     * @throws Exception if file cannot be downloaded
     */
    public ResponseEntity<byte[]> downloadFilePdf(UUID Id, UUID pdfId) throws Exception {
        SerahTerima serahTerima = serahTerimaRepository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Serah Terima not found with id " + Id));

        SerahTerimaPdf pdfFile = serahTerima.getSerahTerimaPdfs().stream()
                .filter(f -> f.getId().equals(pdfId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File PDF tidak ditemukan"));

        byte[] data = minioStorageService.getFileData(FOLDER_PREFIX, pdfFile.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pdfFile.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(pdfFile.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

}