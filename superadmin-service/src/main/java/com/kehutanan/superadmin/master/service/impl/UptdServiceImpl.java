package com.kehutanan.superadmin.master.service.impl;

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

import com.kehutanan.superadmin.common.service.MinioStorageService;
import com.kehutanan.superadmin.master.dto.UptdDTO;
import com.kehutanan.superadmin.master.dto.UptdPageDTO;
import com.kehutanan.superadmin.master.model.Uptd;
import com.kehutanan.superadmin.master.model.UptdDokumentasiFoto;
import com.kehutanan.superadmin.master.model.UptdDokumentasiVideo;
import com.kehutanan.superadmin.master.model.UptdPetaShp;
import com.kehutanan.superadmin.master.model.UptdRantekPdf;
import com.kehutanan.superadmin.master.repository.UptdRepository;
import com.kehutanan.superadmin.master.service.UptdService;
import com.kehutanan.superadmin.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

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
    public List<Uptd> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "uptdCache", key = "#id")
    public UptdDTO findDTOById(Long id) {

        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        UptdDTO uptdDTO = new UptdDTO(uptd);

        return uptdDTO;
    }

    @Override
    public Uptd findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));
    }

    @Override
     @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
    public Uptd save(Uptd uptd) {
        return repository.save(uptd);
    }

    @Override
     @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
    public Uptd update(Long id, Uptd uptd) {
        return repository.save(uptd);
    }

    @Override
    @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        // Delete all PDF files from MinIO storage
        if (uptd.getUptdRantekPdfs() != null) {
            for (UptdRantekPdf pdf : uptd.getUptdRantekPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all photo files from MinIO storage
        if (uptd.getUptdDokumentasiFotos() != null) {
            for (UptdDokumentasiFoto foto : uptd.getUptdDokumentasiFotos()) {
                try {
                    minioStorageService.deleteFile("", foto.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete photo file: " + foto.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all video files from MinIO storage
        if (uptd.getUptdDokumentasiVideos() != null) {
            for (UptdDokumentasiVideo video : uptd.getUptdDokumentasiVideos()) {
                try {
                    minioStorageService.deleteFile("", video.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete video file: " + video.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all SHP files from MinIO storage
        if (uptd.getUptdPetaShps() != null) {
            for (UptdPetaShp shp : uptd.getUptdPetaShps()) {
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
    @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
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

            UptdDokumentasiFoto uptdFoto = new UptdDokumentasiFoto();
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

            uptd.getUptdDokumentasiFotos().add(uptdFoto);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                uptd.getUptdDokumentasiFotos().removeIf(foto -> foto.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(uptd);
    }

    @Override
    @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
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

            UptdRantekPdf uptdPdf = new UptdRantekPdf();
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

            uptd.getUptdRantekPdfs().add(uptdPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                uptd.getUptdRantekPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(uptd);
    }

    @Override
    @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
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

            UptdDokumentasiVideo uptdVideo = new UptdDokumentasiVideo();
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

            uptd.getUptdDokumentasiVideos().add(uptdVideo);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                uptd.getUptdDokumentasiVideos().removeIf(video -> video.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(uptd);
    }

    @Override
    @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
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

            UptdPetaShp uptdShp = new UptdPetaShp();
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

            uptd.getUptdPetaShps().add(uptdShp);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                uptd.getUptdPetaShps().removeIf(shp -> shp.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(uptd);
    }

    @Override
    @Transactional
    @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
    public Uptd deleteUptdFoto(Long id, List<String> uuidFoto) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        uptd.getUptdDokumentasiFotos().removeIf(file -> {
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
    @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
    public Uptd deleteUptdPdf(Long id, List<String> uuidPdf) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        uptd.getUptdRantekPdfs().removeIf(file -> {
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
    @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
    public Uptd deleteUptdVideo(Long id, List<String> uuidVideo) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        uptd.getUptdDokumentasiVideos().removeIf(file -> {
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
    @CacheEvict(value = { "uptdCache", "uptdPageCache", "uptdFilterCache",
            "uptdSearchCache" }, allEntries = true, beforeInvocation = true)
    public Uptd deleteUptdShp(Long id, List<String> uuidShp) {
        Uptd uptd = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Uptd not found with id: " + id));

        uptd.getUptdPetaShps().removeIf(file -> {
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

    @Override
    @Cacheable(value = "uptdPageCache", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public UptdPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Uptd> page = repository.findAll(pageable);

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

        return new UptdPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "uptdFilterCache", key = "{#namaBpdas, #namaUptd, #bpdasList, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public UptdPageDTO findByFiltersWithCache(String namaBpdas, String namaUptd, List<String> bpdasList,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<Uptd> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaUptd if provided
        if (namaBpdas != null && !namaBpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("bpdas").get("namaBpdas")),
                    "%" + namaBpdas.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for namaUptd if provided
        if (namaUptd != null && !namaUptd.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaUptd")),
                    "%" + namaUptd.toLowerCase() + "%"));
        }

        if (bpdasList != null && !bpdasList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("bpdas").get("id").in(bpdasList));
        }

        // Execute query with filters
        Page<Uptd> page = repository.findAll(spec, pageable);

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
        if (namaBpdas != null && !namaBpdas.isEmpty()) {
            builder.queryParam("namaBpdas", namaBpdas);
        }
        if (namaUptd != null && !namaUptd.isEmpty()) {
            builder.queryParam("namaUptd", namaUptd);
        }
        if (bpdasList != null && !bpdasList.isEmpty()) {
            for (String bpdasId : bpdasList) {
                builder.queryParam("bpdasList", bpdasId);
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

        return new UptdPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "uptdSearchCache", key = "{#keyWord, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public UptdPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Uptd> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("namaUptd")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("alamat")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("catatan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("bpdas").get("namaBpdas")), searchPattern)
                    ));
        }

        Page<Uptd> page = repository.findAll(spec, pageable);

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

        return new UptdPageDTO(page, pageMetadata, links);
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