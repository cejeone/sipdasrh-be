package com.kehutanan.pepdas.monev.service;

import java.time.LocalDateTime;
import java.util.List;
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

import com.kehutanan.pepdas.util.FileValidationUtil;
import com.kehutanan.pepdas.common.service.MinioStorageService;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanRepository;
import com.kehutanan.pepdas.monev.dto.MonevDto;
import com.kehutanan.pepdas.monev.model.Monev;
import com.kehutanan.pepdas.monev.model.MonevPdf;
import com.kehutanan.pepdas.monev.repository.MonevPdfRepository;
import com.kehutanan.pepdas.monev.repository.MonevRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MonevService {
    private final MonevRepository monevRepository;
    private final KegiatanRepository kegiatanRepository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private final MonevPdfRepository monevPdfRepository;
    private String FOLDER_PREFIX = "Monev/";

    @Autowired
    public MonevService(MonevRepository monevRepository, KegiatanRepository kegiatanRepository,
            FileValidationUtil fileValidationUtil, MinioStorageService minioStorageService,
            MonevPdfRepository monevPdfRepository) {
        this.monevRepository = monevRepository;
        this.kegiatanRepository = kegiatanRepository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
        this.monevPdfRepository = monevPdfRepository;
    }

    public Page<Monev> findAll(Pageable pageable) {
        return monevRepository.findAll(pageable);
    }

    public Page<Monev> findByFilters(String nomorMonev, String namaKegiatan, List<String> pelaksana,
            Pageable pageable) {
        Specification<Monev> spec = Specification.where(null);

        if (nomorMonev != null && !nomorMonev.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nomorMonev")),
                    "%" + nomorMonev.toLowerCase() + "%"));
        }

        if (namaKegiatan != null && !namaKegiatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kegiatan").get("namaKegiatan")),
                    "%" + namaKegiatan.toLowerCase() + "%"));
        }

        if (pelaksana != null && !pelaksana.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("pelaksana").in(pelaksana));
        }

        return monevRepository.findAll(spec, pageable);
    }

    public Monev findById(UUID id) {
        return monevRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));
    }

    public Monev create(MonevDto monevDto) {
        Monev newMonev = new Monev();

        if (monevDto.getKegiatanId() != null) {
            Kegiatan kegiatan = kegiatanRepository.findById(monevDto.getKegiatanId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Kegiatan tidak ditemukan dengan id: " + monevDto.getKegiatanId()));
            newMonev.setKegiatan(kegiatan);
        }

        newMonev.setNomorMonev(monevDto.getNomorMonev());
        newMonev.setKontrak(monevDto.getKontrak());
        newMonev.setRantek(monevDto.getRantek());
        newMonev.setPelaksana(monevDto.getPelaksana());
        newMonev.setTanggal(monevDto.getTanggal());
        newMonev.setDeskripsi(monevDto.getDeskripsi());
        newMonev.setStatus(monevDto.getStatus());
        return monevRepository.save(newMonev);
    }

    public Monev update(UUID id, MonevDto monevDto) {

        if (!monevRepository.existsById(id)) {
            throw new EntityNotFoundException("Monev not found with id: " + id);
        }

        Monev existingMonev = monevRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));

        if (monevDto.getKegiatanId() != null) {
            Kegiatan kegiatan = kegiatanRepository.findById(monevDto.getKegiatanId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Kegiatan tidak ditemukan dengan id: " + monevDto.getKegiatanId()));
            existingMonev.setKegiatan(kegiatan);
        }

        existingMonev.setNomorMonev(monevDto.getNomorMonev());
        existingMonev.setKontrak(monevDto.getKontrak());
        existingMonev.setRantek(monevDto.getRantek());
        existingMonev.setPelaksana(monevDto.getPelaksana());
        existingMonev.setTanggal(monevDto.getTanggal());
        existingMonev.setDeskripsi(monevDto.getDeskripsi());
        existingMonev.setStatus(monevDto.getStatus());
        return monevRepository.save(existingMonev);
    }

    public void delete(UUID id) {
        if (!monevRepository.existsById(id)) {
            throw new EntityNotFoundException("Monev not found with id: " + id);
        }
        monevRepository.deleteById(id);
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
    public Monev addFilesPdf(UUID id, List<MultipartFile> files) throws Exception {
        Monev monev = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioStorageService.uploadFile(FOLDER_PREFIX, fileName, file.getInputStream(), file.getContentType());

            MonevPdf pdfFile = new MonevPdf();
            pdfFile.setMonev(monev);
            pdfFile.setNamaFile(fileName);
            pdfFile.setNamaAsli(file.getOriginalFilename());
            pdfFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            pdfFile.setContentType(file.getContentType());
            pdfFile.setUploadedAt(LocalDateTime.now());

            monev.getMonevPdfs().add(pdfFile);
        }

        return monevRepository.save(monev);
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
    public Monev deleteFilesPdf(UUID Id, List<UUID> fileIds) throws Exception {
        Monev monev = findById(Id);

        List<MonevPdf> pdfsToDelete = monevPdfRepository.findAllById(fileIds);

        for (MonevPdf pdf : pdfsToDelete) {
            if (pdf.getMonev().getId().equals(Id)) {
                try {
                    minioStorageService.deleteFile(FOLDER_PREFIX, pdf.getNamaFile());
                    monevPdfRepository.delete(pdf);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete PDF file from storage: " + e.getMessage(), e);
                }
            }
        }

        return monevRepository.save(monev);
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
        Monev monev = findById(Id);
        MonevPdf pdfFile = monev.getMonevPdfs().stream()
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