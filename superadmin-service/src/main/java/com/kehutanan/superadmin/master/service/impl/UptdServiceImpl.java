package com.kehutanan.superadmin.master.service.impl;

import com.kehutanan.superadmin.master.repository.UptdRepository;
import com.kehutanan.superadmin.util.FileValidationUtil;
import com.kehutanan.superadmin.master.service.UptdService;
import com.kehutanan.superadmin.master.dto.UptdDTO;
import com.kehutanan.superadmin.master.model.Uptd;
import com.kehutanan.superadmin.master.model.UptdFoto;
import com.kehutanan.superadmin.master.model.UptdPdf;
import com.kehutanan.superadmin.master.model.UptdVideo;
import com.kehutanan.superadmin.master.model.UptdShp;

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

import com.kehutanan.superadmin.common.service.MinioStorageService;
import jakarta.persistence.EntityNotFoundException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UptdServiceImpl implements UptdService {
    private final UptdRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Master/Uptd/";

    @Autowired
    public UptdServiceImpl(UptdRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public Page<Uptd> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Uptd> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "uptdCache", key = "#id")
    public UptdDTO findDTOById(Long id) {

        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        UptdDTO uptdDTO =  new UptdDTO(uptd);

        return uptdDTO;
    }

    @Override
    public Uptd findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));
    }

    @Override
    public Uptd save(Uptd uptd) {
        return repository.save(uptd);
    }

    @Override
    @CachePut(value = "uptdCache", key = "#id")
    public Uptd update(Long id, Uptd uptd) {
        return repository.save(uptd);
    }

    @Override
    @CacheEvict(value = "uptdCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        // Delete all PDF files from MinIO storage
        if (uptd.getUptdPdfList() != null) {
            for (UptdPdf pdf : uptd.getUptdPdfList()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all photo files from MinIO storage
        if (uptd.getUptdFotoList() != null) {
            for (UptdFoto foto : uptd.getUptdFotoList()) {
                try {
                    minioStorageService.deleteFile("", foto.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete photo file: " + foto.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all video files from MinIO storage
        if (uptd.getUptdVideoList() != null) {
            for (UptdVideo video : uptd.getUptdVideoList()) {
                try {
                    minioStorageService.deleteFile("", video.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete video file: " + video.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all SHP files from MinIO storage
        if (uptd.getUptdShpList() != null) {
            for (UptdShp shp : uptd.getUptdShpList()) {
                try {
                    minioStorageService.deleteFile("", shp.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete SHP file: " + shp.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    @Override
    public Page<Uptd> findByFilters(String namaUptd, Long bpdasId, Long provinsiId, Long kabupatenKotaId,
            Long kecamatanId, Long kelurahanDesaId, Pageable pageable) {
        Specification<Uptd> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaUptd if provided
        if (namaUptd != null && !namaUptd.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaUptd")),
                    "%" + namaUptd.toLowerCase() + "%"));
        }

        // Add equals filter for bpdasId if provided
        if (bpdasId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("bpdas").get("id"), bpdasId));
        }

        // Add equals filter for provinsiId if provided
        if (provinsiId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("provinsi").get("id"), provinsiId));
        }

        // Add equals filter for kabupatenKotaId if provided
        if (kabupatenKotaId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kabupatenKota").get("id"), kabupatenKotaId));
        }

        // Add equals filter for kecamatanId if provided
        if (kecamatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kecamatan").get("id"), kecamatanId));
        }

        // Add equals filter for kelurahanDesaId if provided
        if (kelurahanDesaId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kelurahanDesa").get("id"), kelurahanDesaId));
        }

        return repository.findAll(spec, pageable);
    }

    @Override
    @CacheEvict(value = "uptdCache", allEntries = true, beforeInvocation = true)
    public Uptd uploadUptdFoto(Long id, List<MultipartFile> files) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + uptd.getId() + "/foto/";
            String filePath = folderName + fileName;

            UptdFoto uptdFoto = new UptdFoto();
            uptdFoto.setId(idfile);
            uptdFoto.setUptd(uptd);
            uptdFoto.setNamaAsli(file.getOriginalFilename());
            uptdFoto.setNamaFile(fileName);
            uptdFoto.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            uptdFoto.setViewUrl("/file/view?fileName=" + encodedPath);
            uptdFoto.setDownloadUrl("/file/download?fileName=" + encodedPath);
            uptdFoto.setContentType(file.getContentType());
            uptdFoto.setUkuranMb((double) file.getSize() / (1024 * 1024));
            uptdFoto.setContentType(file.getContentType());
            uptdFoto.setUploadedAt(LocalDateTime.now());

            uptd.getUptdFotoList().add(uptdFoto);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                uptd.getUptdFotoList().removeIf(foto -> foto.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(uptd);
    }

    @Override
    @CacheEvict(value = "uptdCache", allEntries = true, beforeInvocation = true)
    public Uptd uploadUptdPdf(Long id, List<MultipartFile> files) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + uptd.getId() + "/pdf/";
            String filePath = folderName + fileName;

            UptdPdf uptdPdf = new UptdPdf();
            uptdPdf.setId(idfile);
            uptdPdf.setUptd(uptd);
            uptdPdf.setNamaAsli(file.getOriginalFilename());
            uptdPdf.setNamaFile(fileName);
            uptdPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            uptdPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            uptdPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            uptdPdf.setContentType(file.getContentType());
            uptdPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            uptdPdf.setContentType(file.getContentType());
            uptdPdf.setUploadedAt(LocalDateTime.now());

            uptd.getUptdPdfList().add(uptdPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                uptd.getUptdPdfList().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(uptd);
    }

    @Override
    @CacheEvict(value = "uptdCache", allEntries = true, beforeInvocation = true)
    public Uptd uploadUptdVideo(Long id, List<MultipartFile> files) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "video");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + uptd.getId() + "/video/";
            String filePath = folderName + fileName;

            UptdVideo uptdVideo = new UptdVideo();
            uptdVideo.setId(idfile);
            uptdVideo.setUptd(uptd);
            uptdVideo.setNamaAsli(file.getOriginalFilename());
            uptdVideo.setNamaFile(fileName);
            uptdVideo.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            uptdVideo.setViewUrl("/file/view?fileName=" + encodedPath);
            uptdVideo.setDownloadUrl("/file/download?fileName=" + encodedPath);
            uptdVideo.setContentType(file.getContentType());
            uptdVideo.setUkuranMb((double) file.getSize() / (1024 * 1024));
            uptdVideo.setContentType(file.getContentType());
            uptdVideo.setUploadedAt(LocalDateTime.now());

            uptd.getUptdVideoList().add(uptdVideo);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                uptd.getUptdVideoList().removeIf(video -> video.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(uptd);
    }

    @Override
    @CacheEvict(value = "uptdCache", allEntries = true, beforeInvocation = true)
    public Uptd uploadUptdShp(Long id, List<MultipartFile> files) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "shp");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + uptd.getId() + "/shp/";
            String filePath = folderName + fileName;

            UptdShp uptdShp = new UptdShp();
            uptdShp.setId(idfile);
            uptdShp.setUptd(uptd);
            uptdShp.setNamaAsli(file.getOriginalFilename());
            uptdShp.setNamaFile(fileName);
            uptdShp.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            uptdShp.setViewUrl("/file/view?fileName=" + encodedPath);
            uptdShp.setDownloadUrl("/file/download?fileName=" + encodedPath);
            uptdShp.setContentType(file.getContentType());
            uptdShp.setUkuranMb((double) file.getSize() / (1024 * 1024));
            uptdShp.setContentType(file.getContentType());
            uptdShp.setUploadedAt(LocalDateTime.now());

            uptd.getUptdShpList().add(uptdShp);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                uptd.getUptdShpList().removeIf(shp -> shp.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(uptd);
    }

    @Override
    @Transactional
    @CacheEvict(value = "uptdCache", allEntries = true, beforeInvocation = true)
    public Uptd deleteUptdFoto(Long id, List<String> uuidFoto) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        uptd.getUptdFotoList().removeIf(file -> {
            if (uuidFoto.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this photo from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this photo in the list
            }
        });
        return repository.save(uptd);
    }

    @Override
    @Transactional
    @CacheEvict(value = "uptdCache", allEntries = true, beforeInvocation = true)
    public Uptd deleteUptdPdf(Long id, List<String> uuidPdf) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        uptd.getUptdPdfList().removeIf(file -> {
            if (uuidPdf.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this PDF from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this PDF in the list
            }
        });
        return repository.save(uptd);
    }

    @Override
    @Transactional
    @CacheEvict(value = "uptdCache", allEntries = true, beforeInvocation = true)
    public Uptd deleteUptdVideo(Long id, List<String> uuidVideo) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        uptd.getUptdVideoList().removeIf(file -> {
            if (uuidVideo.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this video from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this video in the list
            }
        });
        return repository.save(uptd);
    }

    @Override
    @Transactional
    @CacheEvict(value = "uptdCache", allEntries = true, beforeInvocation = true)
    public Uptd deleteUptdShp(Long id, List<String> uuidShp) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        uptd.getUptdShpList().removeIf(file -> {
            if (uuidShp.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this SHP from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this SHP in the list
            }
        });
        return repository.save(uptd);
    }

}