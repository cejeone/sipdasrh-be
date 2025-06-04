package com.kehutanan.rh.kegiatan.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.rh.common.service.MinioStorageService;
import com.kehutanan.rh.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.rh.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisVideo;
import com.kehutanan.rh.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.rh.kegiatan.repository.KegiatanRepository;
import com.kehutanan.rh.kegiatan.service.KegiatanService;
import com.kehutanan.rh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanServiceImpl implements KegiatanService {
    private final KegiatanRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Kegiatan/";

    @Autowired
    public KegiatanServiceImpl(KegiatanRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<Kegiatan> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanDTO findDTOById(Long id) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        KegiatanDTO kegiatanDTO = new KegiatanDTO(kegiatan);
        return kegiatanDTO;
    }

    @Override
    public Kegiatan findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
    }

    @Override
    public Kegiatan save(Kegiatan kegiatan) {
        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan update(Long id, Kegiatan kegiatan) {
        return repository.save(kegiatan);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        // Delete all PDF files from MinIO storage
        if (kegiatan.getKegiatanRancanganTeknisPdfs() != null) {
            for (KegiatanRancanganTeknisPdf pdf : kegiatan.getKegiatanRancanganTeknisPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all rantek foto files from MinIO storage
        if (kegiatan.getKegiatanRancanganTeknisFotos() != null) {
            for (KegiatanRancanganTeknisFoto foto : kegiatan.getKegiatanRancanganTeknisFotos()) {
                try {
                    minioStorageService.deleteFile("", foto.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete rantek photo file: " + foto.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all rantek video files from MinIO storage
        if (kegiatan.getKegiatanRancanganTeknisVideos() != null) {
            for (KegiatanRancanganTeknisVideo video : kegiatan.getKegiatanRancanganTeknisVideos()) {
                try {
                    minioStorageService.deleteFile("", video.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete rantek video file: " + video.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all kontrak PDF files from MinIO storage
        if (kegiatan.getKegiatanKontrakPdfs() != null) {
            for (KegiatanKontrakPdf pdf : kegiatan.getKegiatanKontrakPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete kontrak PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all dokumentasi foto files from MinIO storage
        if (kegiatan.getKegiatanDokumentasiFotos() != null) {
            for (KegiatanDokumentasiFoto foto : kegiatan.getKegiatanDokumentasiFotos()) {
                try {
                    minioStorageService.deleteFile("", foto.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete dokumentasi photo file: " + foto.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all dokumentasi video files from MinIO storage
        if (kegiatan.getKegiatanDokumentasiVideos() != null) {
            for (KegiatanDokumentasiVideo video : kegiatan.getKegiatanDokumentasiVideos()) {
                try {
                    minioStorageService.deleteFile("", video.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete dokumentasi video file: " + video.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    // File Upload Methods
    @Override
    public Kegiatan uploadRancanganTeknisPdf(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/rancanganTeknis/pdf/";
            String filePath = folderName + fileName;

            KegiatanRancanganTeknisPdf kegiatanPdf = new KegiatanRancanganTeknisPdf();
            kegiatanPdf.setId(idfile);
            kegiatanPdf.setKegiatan(kegiatan);
            kegiatanPdf.setNamaAsli(file.getOriginalFilename());
            kegiatanPdf.setNamaFile(fileName);
            kegiatanPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanPdf.setContentType(file.getContentType());
            kegiatanPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanPdf.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanRancanganTeknisPdfs().add(kegiatanPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanRancanganTeknisPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan uploadRancanganTeknisFoto(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/rancanganTeknis/foto/";
            String filePath = folderName + fileName;

            KegiatanRancanganTeknisFoto kegiatanFoto = new KegiatanRancanganTeknisFoto();
            kegiatanFoto.setId(idfile);
            kegiatanFoto.setKegiatan(kegiatan);
            kegiatanFoto.setNamaAsli(file.getOriginalFilename());
            kegiatanFoto.setNamaFile(fileName);
            kegiatanFoto.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanFoto.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanFoto.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanFoto.setContentType(file.getContentType());
            kegiatanFoto.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanFoto.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanRancanganTeknisFotos().add(kegiatanFoto);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanRancanganTeknisFotos().removeIf(foto -> foto.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan uploadRancanganTeknisVideo(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "video");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/rancanganTeknis/video/";
            String filePath = folderName + fileName;

            KegiatanRancanganTeknisVideo kegiatanVideo = new KegiatanRancanganTeknisVideo();
            kegiatanVideo.setId(idfile);
            kegiatanVideo.setKegiatan(kegiatan);
            kegiatanVideo.setNamaAsli(file.getOriginalFilename());
            kegiatanVideo.setNamaFile(fileName);
            kegiatanVideo.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanVideo.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanVideo.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanVideo.setContentType(file.getContentType());
            kegiatanVideo.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanVideo.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanRancanganTeknisVideos().add(kegiatanVideo);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanRancanganTeknisVideos().removeIf(video -> video.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan uploadKontrakPdf(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/kontrak/";
            String filePath = folderName + fileName;

            KegiatanKontrakPdf kegiatanPdf = new KegiatanKontrakPdf();
            kegiatanPdf.setId(idfile);
            kegiatanPdf.setKegiatan(kegiatan);
            kegiatanPdf.setNamaAsli(file.getOriginalFilename());
            kegiatanPdf.setNamaFile(fileName);
            kegiatanPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanPdf.setContentType(file.getContentType());
            kegiatanPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanPdf.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanKontrakPdfs().add(kegiatanPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanKontrakPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan uploadDokumentasiFoto(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/dokumentasi/foto/";
            String filePath = folderName + fileName;

            KegiatanDokumentasiFoto kegiatanFoto = new KegiatanDokumentasiFoto();
            kegiatanFoto.setId(idfile);
            kegiatanFoto.setKegiatan(kegiatan);
            kegiatanFoto.setNamaAsli(file.getOriginalFilename());
            kegiatanFoto.setNamaFile(fileName);
            kegiatanFoto.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanFoto.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanFoto.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanFoto.setContentType(file.getContentType());
            kegiatanFoto.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanFoto.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanDokumentasiFotos().add(kegiatanFoto);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanDokumentasiFotos().removeIf(foto -> foto.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan uploadDokumentasiVideo(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "video");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/dokumentasi/video/";
            String filePath = folderName + fileName;

            KegiatanDokumentasiVideo kegiatanVideo = new KegiatanDokumentasiVideo();
            kegiatanVideo.setId(idfile);
            kegiatanVideo.setKegiatan(kegiatan);
            kegiatanVideo.setNamaAsli(file.getOriginalFilename());
            kegiatanVideo.setNamaFile(fileName);
            kegiatanVideo.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanVideo.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanVideo.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanVideo.setContentType(file.getContentType());
            kegiatanVideo.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanVideo.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanDokumentasiVideos().add(kegiatanVideo);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanDokumentasiVideos().removeIf(video -> video.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    // File Deletion Methods
    @Override
    @Transactional
    public Kegiatan deleteRancanganTeknisPdf(Long id, List<String> uuidPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanRancanganTeknisPdfs().removeIf(file -> {
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
        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deleteRancanganTeknisFoto(Long id, List<String> uuidFoto) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanRancanganTeknisFotos().removeIf(file -> {
            if (uuidFoto.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this foto from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this foto in the list
            }
        });
        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deleteRancanganTeknisVideo(Long id, List<String> uuidVideo) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanRancanganTeknisVideos().removeIf(file -> {
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
        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deleteKontrakPdf(Long id, List<String> uuidPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanKontrakPdfs().removeIf(file -> {
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
        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deleteDokumentasiFoto(Long id, List<String> uuidFoto) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanDokumentasiFotos().removeIf(file -> {
            if (uuidFoto.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this foto from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this foto in the list
            }
        });
        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deleteDokumentasiVideo(Long id, List<String> uuidVideo) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanDokumentasiVideos().removeIf(file -> {
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
        return repository.save(kegiatan);
    }

    // Search and Filter Methods with Cache
    @Override
    @Cacheable(value = "kegiatanCache", key = "'allKegiatan-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public KegiatanPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Kegiatan> page = repository.findAll(pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Create links for pagination
        List<Link> links = new ArrayList<>();

        // Self link
        links.add(Link.of(createPageUrl(baseUrl, page.getNumber(), page.getSize()), "self"));

        // First page link
        links.add(Link.of(createPageUrl(baseUrl, 0, page.getSize()), "first"));

        // Previous page link (if not first page)
        if (page.getNumber() > 0) {
            links.add(Link.of(createPageUrl(baseUrl, page.getNumber() - 1, page.getSize()), "prev"));
        }

        // Next page link (if not last page)
        if (page.getNumber() < page.getTotalPages() - 1) {
            links.add(Link.of(createPageUrl(baseUrl, page.getNumber() + 1, page.getSize()), "next"));
        }

        // Last page link
        if (page.getTotalPages() > 0) {
            links.add(Link.of(createPageUrl(baseUrl, page.getTotalPages() - 1, page.getSize()), "last"));
        }

        return new KegiatanPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "kegiatanCache", key = "'filterKegiatan-' + #namaProgram + '-' + #jenisKegiatan + '-' + #jenis + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public KegiatanPageDTO findByFiltersWithCache(String namaProgram, String jenisKegiatan, List<String> jenis,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<Kegiatan> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaProgram if provided
        if (namaProgram != null && !namaProgram.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("program").get("namaProgram")),
                    "%" + namaProgram.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for jenisKegiatan if provided
        if (jenisKegiatan != null && !jenisKegiatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("jenisKegiatanId").get("namaLov")),
                    "%" + jenisKegiatan.toLowerCase() + "%"));
        }

        // Filter by jenis if provided
        if (jenis != null && !jenis.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("jenisKegiatanId").get("id").in(jenis));
        }

        // Execute query with filters
        Page<Kegiatan> page = repository.findAll(spec, pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Create links for pagination
        List<Link> links = new ArrayList<>();

        // Base URL with filters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);

        // Add all filter parameters to URL
        if (namaProgram != null && !namaProgram.isEmpty()) {
            builder.queryParam("namaProgram", namaProgram);
        }
        
        if (jenisKegiatan != null && !jenisKegiatan.isEmpty()) {
            builder.queryParam("jenisKegiatan", jenisKegiatan);
        }
        
        if (jenis != null && !jenis.isEmpty()) {
            for (String jenisId : jenis) {
                builder.queryParam("jenis", jenisId);
            }
        }

        String filterBaseUrl = builder.build().toUriString();

        // Self link
        links.add(Link.of(createFilterPageUrl(filterBaseUrl, page.getNumber(), page.getSize()), "self"));

        // First page link
        links.add(Link.of(createFilterPageUrl(filterBaseUrl, 0, page.getSize()), "first"));

        // Previous page link (if not first page)
        if (page.getNumber() > 0) {
            links.add(Link.of(createFilterPageUrl(filterBaseUrl, page.getNumber() - 1, page.getSize()), "prev"));
        }

        // Next page link (if not last page)
        if (page.getNumber() < page.getTotalPages() - 1) {
            links.add(Link.of(createFilterPageUrl(filterBaseUrl, page.getNumber() + 1, page.getSize()), "next"));
        }

        // Last page link
        if (page.getTotalPages() > 0) {
            links.add(Link.of(createFilterPageUrl(filterBaseUrl, page.getTotalPages() - 1, page.getSize()), "last"));
        }

        return new KegiatanPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "kegiatanCache", key = "'searchKegiatan-' + #keyWord + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public KegiatanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Kegiatan> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("namaKegiatan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("pemangkuKawasan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nomor")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("program").get("namaProgram")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("jenisKegiatanId").get("namaLov")), searchPattern)));
        }

        Page<Kegiatan> page = repository.findAll(spec, pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Create links for pagination
        List<Link> links = new ArrayList<>();

        // Base URL with search parameter
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);

        // Add search keyword parameter to URL
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }

        String searchBaseUrl = builder.build().toUriString();

        // Self link
        links.add(Link.of(createFilterPageUrl(searchBaseUrl, page.getNumber(), page.getSize()), "self"));

        // First page link
        links.add(Link.of(createFilterPageUrl(searchBaseUrl, 0, page.getSize()), "first"));

        // Previous page link (if not first page)
        if (page.getNumber() > 0) {
            links.add(Link.of(createFilterPageUrl(searchBaseUrl, page.getNumber() - 1, page.getSize()), "prev"));
        }

        // Next page link (if not last page)
        if (page.getNumber() < page.getTotalPages() - 1) {
            links.add(Link.of(createFilterPageUrl(searchBaseUrl, page.getNumber() + 1, page.getSize()), "next"));
        }

        // Last page link
        if (page.getTotalPages() > 0) {
            links.add(Link.of(createFilterPageUrl(searchBaseUrl, page.getTotalPages() - 1, page.getSize()), "last"));
        }

        return new KegiatanPageDTO(page, pageMetadata, links);
    }

    private String createPageUrl(String baseUrl, int page, int size) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .toUriString();
    }

    private String createFilterPageUrl(String filterBaseUrl, int page, int size) {
        // Check if the URL already has query parameters
        String connector = filterBaseUrl.contains("?") ? "&" : "?";

        return filterBaseUrl + connector + "page=" + page + "&size=" + size;
    }
}