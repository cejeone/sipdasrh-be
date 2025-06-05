package com.kehutanan.rm.kegiatan.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.rm.common.service.MinioStorageService;
import com.kehutanan.rm.kegiatan.dto.KegiatanMonevPageDTO;
import com.kehutanan.rm.kegiatan.model.KegiatanMonev;
import com.kehutanan.rm.kegiatan.model.KegiatanMonevPdf;
import com.kehutanan.rm.kegiatan.model.dto.KegiatanMonevDTO;
import com.kehutanan.rm.kegiatan.repository.KegiatanMonevRepository;
import com.kehutanan.rm.kegiatan.service.KegiatanMonevService;
import com.kehutanan.rm.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanMonevServiceImpl implements KegiatanMonevService {
    
    private final KegiatanMonevRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Kegiatan/Monev/";

    @Autowired
    public KegiatanMonevServiceImpl(KegiatanMonevRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<KegiatanMonev> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanMonevDTO findDTOById(Long id) {
        KegiatanMonev kegiatanMonev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonev not found with id: " + id));

        KegiatanMonevDTO kegiatanMonevDTO = new KegiatanMonevDTO(kegiatanMonev);

        return kegiatanMonevDTO;
    }

    @Override
    public KegiatanMonev findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonev not found with id: " + id));
    }

    @Override
    public KegiatanMonev save(KegiatanMonev kegiatanMonev) {
        return repository.save(kegiatanMonev);
    }

    @Override
    public KegiatanMonev update(Long id, KegiatanMonev kegiatanMonev) {
        // Ensure the entity exists
        findById(id);
        return repository.save(kegiatanMonev);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        KegiatanMonev kegiatanMonev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonev not found with id: " + id));

        // Delete all PDF files from MinIO storage
        if (kegiatanMonev.getKegiatanMonevPdfs() != null) {
            for (KegiatanMonevPdf pdf : kegiatanMonev.getKegiatanMonevPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    @Override
    public KegiatanMonev uploadPdf(Long id, List<MultipartFile> files) {
        KegiatanMonev kegiatanMonev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonev not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatanMonev.getId() + "/pdf/";
            String filePath = folderName + fileName;

            KegiatanMonevPdf monevPdf = new KegiatanMonevPdf();
            monevPdf.setId(idfile);
            monevPdf.setKegiatanMonev(kegiatanMonev);
            monevPdf.setNamaAsli(file.getOriginalFilename());
            monevPdf.setNamaFile(fileName);
            monevPdf.setPathFile(filePath);
            
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            monevPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            monevPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            monevPdf.setContentType(file.getContentType());
            monevPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            monevPdf.setUploadedAt(LocalDateTime.now());

            kegiatanMonev.getKegiatanMonevPdfs().add(monevPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatanMonev.getKegiatanMonevPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatanMonev);
    }

    @Override
    @Transactional
    public KegiatanMonev deletePdf(Long id, List<String> uuidPdf) {
        KegiatanMonev kegiatanMonev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonev not found with id: " + id));

        kegiatanMonev.getKegiatanMonevPdfs().removeIf(file -> {
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
        return repository.save(kegiatanMonev);
    }

    @Override
    public KegiatanMonevPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanMonev> page = repository.findAll(pageable);

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

        return new KegiatanMonevPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanMonevPageDTO findByFiltersWithCache(Long kegiatanId, String kegiatan, List<String> statusId,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<KegiatanMonev> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for kegiatan name if provided
        if (kegiatan != null && !kegiatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kegiatan").get("nama")),
                    "%" + kegiatan.toLowerCase() + "%"));
        }

        // Add filter for statusId if provided
        if (statusId != null && !statusId.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("statusId").get("id").in(statusId));
        }

        // Execute query with filters
        Page<KegiatanMonev> page = repository.findAll(spec, pageable);

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
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
        }
        if (kegiatan != null && !kegiatan.isEmpty()) {
            builder.queryParam("kegiatan", kegiatan);
        }
        if (statusId != null && !statusId.isEmpty()) {
            for (String status : statusId) {
                builder.queryParam("statusId", status);
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

        return new KegiatanMonevPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanMonevPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanMonev> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kegiatan").get("id"), kegiatanId));
        }

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nomor")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("deskripsi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("kegiatan").get("nama")), searchPattern)));
        }

        Page<KegiatanMonev> page = repository.findAll(spec, pageable);

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

        // Add search parameters to URL
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
        }
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

        return new KegiatanMonevPageDTO(page, pageMetadata, links);
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