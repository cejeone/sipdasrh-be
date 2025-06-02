package com.kehutanan.pepdas.monev.service.impl;

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

import com.kehutanan.pepdas.common.service.MinioStorageService;
import com.kehutanan.pepdas.monev.dto.MonevDTO;
import com.kehutanan.pepdas.monev.dto.MonevPageDTO;
import com.kehutanan.pepdas.monev.model.Monev;
import com.kehutanan.pepdas.monev.model.MonevPdf;
import com.kehutanan.pepdas.monev.repository.MonevRepository;
import com.kehutanan.pepdas.monev.service.MonevService;
import com.kehutanan.pepdas.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MonevServiceImpl implements MonevService {
    private final MonevRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Pepdas/Monev/";

    @Autowired
    public MonevServiceImpl(MonevRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<Monev> findAll() {
        return repository.findAll();
    }

    @Override
    public MonevDTO findDTOById(Long id) {
        Monev monev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));

        MonevDTO monevDTO = new MonevDTO(monev);
        return monevDTO;
    }

    @Override
    public Monev findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));
    }

    @Override
    public Monev save(Monev monev) {
        return repository.save(monev);
    }

    @Override
    public Monev update(Long id, Monev monev) {
        return repository.save(monev);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Monev monev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));

        // Delete all PDF files from MinIO storage
        if (monev.getMonevPdfs() != null) {
            for (MonevPdf pdf : monev.getMonevPdfs()) {
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
    public Monev uploadMonevPdf(Long id, List<MultipartFile> files) {
        Monev monev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + monev.getId() + "/pdf/";
            String filePath = folderName + fileName;

            MonevPdf monevPdf = new MonevPdf();
            monevPdf.setId(idfile);
            monevPdf.setMonev(monev);
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
            monevPdf.setContentType(file.getContentType());
            monevPdf.setUploadedAt(LocalDateTime.now());

            monev.getMonevPdfs().add(monevPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                monev.getMonevPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(monev);
    }

    @Override
    @Transactional
    public Monev deleteMonevPdf(Long id, List<String> uuidPdf) {
        Monev monev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));

        monev.getMonevPdfs().removeIf(file -> {
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
        return repository.save(monev);
    }

    @Override
    public MonevPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Monev> page = repository.findAll(pageable);

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

        return new MonevPageDTO(page, pageMetadata, links);
    }

    @Override
    public MonevPageDTO findByFiltersWithCache(String nomor, String kegiatan, List<String> status,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<Monev> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for nomor if provided
        if (nomor != null && !nomor.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nomor")),
                    "%" + nomor.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for kegiatan if provided
        if (kegiatan != null && !kegiatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kegiatan").get("namaKegiatan")),
                    "%" + kegiatan.toLowerCase() + "%"));
        }

        // Add IN filter for status if provided
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("status").get("id").in(status));
        }

        // Execute query with filters
        Page<Monev> page = repository.findAll(spec, pageable);

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
        if (nomor != null && !nomor.isEmpty()) {
            builder.queryParam("nomor", nomor);
        }
        if (kegiatan != null && !kegiatan.isEmpty()) {
            builder.queryParam("kegiatan", kegiatan);
        }
        if (status != null && !status.isEmpty()) {
            for (String statusId : status) {
                builder.queryParam("status", statusId);
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

        return new MonevPageDTO(page, pageMetadata, links);
    }

    @Override
    public MonevPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Monev> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nomor")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("deskripsi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("kegiatan").get("namaKegiatan")), searchPattern)));
        }

        Page<Monev> page = repository.findAll(spec, pageable);

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

        return new MonevPageDTO(page, pageMetadata, links);
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