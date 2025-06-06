package com.kehutanan.tktrh.tmkh.kegiatan.service.impl;

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

import com.kehutanan.tktrh.common.service.MinioStorageService;
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanMonevPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonevPdf;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanMonevDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.repository.KegiatanMonevRepository;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanMonevService;
import com.kehutanan.tktrh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanMonevServiceImpl implements KegiatanMonevService {
    
    private final KegiatanMonevRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "TMKH/KegiatanMonev/";

    @Autowired
    public KegiatanMonevServiceImpl(KegiatanMonevRepository repository, 
            FileValidationUtil fileValidationUtil,
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
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan Monev not found with id: " + id));
        return new KegiatanMonevDTO(kegiatanMonev);
    }

    @Override
    public KegiatanMonev findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan Monev not found with id: " + id));
    }

    @Override
    @Transactional
    public KegiatanMonev save(KegiatanMonev kegiatanMonev) {
        return repository.save(kegiatanMonev);
    }

    @Override
    @Transactional
    public KegiatanMonev update(Long id, KegiatanMonev kegiatanMonev) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Kegiatan Monev not found with id: " + id);
        }
        return repository.save(kegiatanMonev);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        KegiatanMonev kegiatanMonev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan Monev not found with id: " + id));

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
    @Transactional
    public KegiatanMonev uploadKegiatanMonevPdf(Long id, List<MultipartFile> files) {
        KegiatanMonev kegiatanMonev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan Monev not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatanMonev.getId() + "/pdf/";
            String filePath = folderName + fileName;

            KegiatanMonevPdf kegiatanMonevPdf = new KegiatanMonevPdf();
            kegiatanMonevPdf.setId(idfile);
            kegiatanMonevPdf.setKegiatanMonev(kegiatanMonev);
            kegiatanMonevPdf.setNamaAsli(file.getOriginalFilename());
            kegiatanMonevPdf.setNamaFile(fileName);
            kegiatanMonevPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanMonevPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanMonevPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanMonevPdf.setContentType(file.getContentType());
            kegiatanMonevPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanMonevPdf.setContentType(file.getContentType());
            kegiatanMonevPdf.setUploadedAt(LocalDateTime.now());

            kegiatanMonev.getKegiatanMonevPdfs().add(kegiatanMonevPdf);

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
    public KegiatanMonev deleteKegiatanMonevPdf(Long id, List<String> uuidPdf) {
        KegiatanMonev kegiatanMonev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan Monev not found with id: " + id));

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
    public KegiatanMonevPageDTO findByFiltersWithCache(
            Long kegiatanId, 
            String nomor, 
            String deskripsi, 
            List<String> statusList,
            Pageable pageable, 
            String baseUrl) {

        // Create specification based on filters
        Specification<KegiatanMonev> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for nomor if provided
        if (nomor != null && !nomor.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nomor")),
                    "%" + nomor.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for deskripsi if provided
        if (deskripsi != null && !deskripsi.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("deskripsi")),
                    "%" + deskripsi.toLowerCase() + "%"));
        }

        // Add filter for statusList if provided
        if (statusList != null && !statusList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("statusId").get("id").in(statusList));
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
        if (nomor != null && !nomor.isEmpty()) {
            builder.queryParam("nomor", nomor);
        }
        if (deskripsi != null && !deskripsi.isEmpty()) {
            builder.queryParam("deskripsi", deskripsi);
        }
        if (statusList != null && !statusList.isEmpty()) {
            for (String statusId : statusList) {
                builder.queryParam("statusList", statusId);
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
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("statusId").get("name")), searchPattern)));
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