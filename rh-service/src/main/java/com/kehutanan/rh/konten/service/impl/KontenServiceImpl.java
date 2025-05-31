package com.kehutanan.rh.konten.service.impl;

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
import com.kehutanan.rh.konten.dto.KontenDTO;
import com.kehutanan.rh.konten.dto.KontenPageDTO;
import com.kehutanan.rh.konten.model.Konten;
import com.kehutanan.rh.konten.model.KontenGambar;
import com.kehutanan.rh.konten.model.KontenGambarUtama;
import com.kehutanan.rh.konten.repository.KontenRepository;
import com.kehutanan.rh.konten.service.KontenService;
import com.kehutanan.rh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

@Service
public class KontenServiceImpl implements KontenService {
    private final KontenRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Konten/";

    @Autowired
    public KontenServiceImpl(KontenRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<Konten> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "kontenCache", key = "#id")
    public KontenDTO findDTOById(Long id) {
        Konten konten = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konten not found with id: " + id));

        KontenDTO kontenDTO = new KontenDTO(konten);

        return kontenDTO;
    }

    @Override
    public Konten findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konten not found with id: " + id));
    }

    @Override
    public Konten save(Konten konten) {
        return repository.save(konten);
    }

    @Override
    @CachePut(value = "kontenCache", key = "#id")
    public Konten update(Long id, Konten konten) {
        return repository.save(konten);
    }

    @Override
    @CacheEvict(value = { "kontenCache", "kontenPageCache", "kontenFilterCache",
            "kontenSearchCache" }, allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Konten konten = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konten not found with id: " + id));

        // Delete all images from MinIO storage
        if (konten.getKontenGambars() != null) {
            for (KontenGambar gambar : konten.getKontenGambars()) {
                try {
                    minioStorageService.deleteFile("", gambar.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete image file: " + gambar.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all main images from MinIO storage
        if (konten.getKontenGambarUtamas() != null) {
            for (KontenGambarUtama gambarUtama : konten.getKontenGambarUtamas()) {
                try {
                    minioStorageService.deleteFile("", gambarUtama.getPathFile());
                } catch (Exception e) {
                    System.err.println(
                            "Failed to delete main image file: " + gambarUtama.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    @Override
    @CacheEvict(value = { "kontenCache", "kontenPageCache", "kontenFilterCache",
            "kontenSearchCache" }, allEntries = true, beforeInvocation = true)
    public Konten uploadKontenGambar(Long id, List<MultipartFile> files) {
        Konten konten = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konten not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + konten.getId() + "/gambar/";
            String filePath = folderName + fileName;

            KontenGambar kontenGambar = new KontenGambar();
            kontenGambar.setId(idfile);
            kontenGambar.setKonten(konten);
            kontenGambar.setNamaAsli(file.getOriginalFilename());
            kontenGambar.setNamaFile(fileName);
            kontenGambar.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kontenGambar.setViewUrl("/file/view?fileName=" + encodedPath);
            kontenGambar.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kontenGambar.setContentType(file.getContentType());
            kontenGambar.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kontenGambar.setContentType(file.getContentType());
            kontenGambar.setUploadedAt(LocalDateTime.now());

            konten.getKontenGambars().add(kontenGambar);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                konten.getKontenGambars().removeIf(g -> g.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(konten);
    }

    @Override
    @CacheEvict(value = { "kontenCache", "kontenPageCache", "kontenFilterCache",
            "kontenSearchCache" }, allEntries = true, beforeInvocation = true)
    public Konten uploadKontenGambarUtama(Long id, List<MultipartFile> files) {
        Konten konten = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konten not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + konten.getId() + "/gambarUtama/";
            String filePath = folderName + fileName;

            KontenGambarUtama kontenGambarUtama = new KontenGambarUtama();
            kontenGambarUtama.setId(idfile);
            kontenGambarUtama.setKonten(konten);
            kontenGambarUtama.setNamaAsli(file.getOriginalFilename());
            kontenGambarUtama.setNamaFile(fileName);
            kontenGambarUtama.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kontenGambarUtama.setViewUrl("/file/view?fileName=" + encodedPath);
            kontenGambarUtama.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kontenGambarUtama.setContentType(file.getContentType());
            kontenGambarUtama.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kontenGambarUtama.setContentType(file.getContentType());
            kontenGambarUtama.setUploadedAt(LocalDateTime.now());

            konten.getKontenGambarUtamas().add(kontenGambarUtama);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                konten.getKontenGambarUtamas().removeIf(g -> g.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(konten);
    }

    @Override
    @Transactional
    @CacheEvict(value = { "kontenCache", "kontenPageCache", "kontenFilterCache",
            "kontenSearchCache" }, allEntries = true, beforeInvocation = true)
    public Konten deleteKontenGambar(Long id, List<String> uuidGambar) {
        Konten konten = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konten not found with id: " + id));

        konten.getKontenGambars().removeIf(file -> {
            if (uuidGambar.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this image from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this image in the list
            }
        });
        return repository.save(konten);
    }

    @Override
    @Transactional
    @CacheEvict(value = { "kontenCache", "kontenPageCache", "kontenFilterCache",
            "kontenSearchCache" }, allEntries = true, beforeInvocation = true)
    public Konten deleteKontenGambarUtama(Long id, List<String> uuidGambarUtama) {
        Konten konten = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konten not found with id: " + id));

        konten.getKontenGambarUtamas().removeIf(file -> {
            if (uuidGambarUtama.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this main image from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this main image in the list
            }
        });
        return repository.save(konten);
    }

    @Override
    @Cacheable(value = "kontenPageCache", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public KontenPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Konten> page = repository.findAll(pageable);

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

        return new KontenPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "kontenFilterCache", key = "{#judul, #tipe, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public KontenPageDTO findByFiltersWithCache(String judul, List<String> tipe,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<Konten> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for judul if provided
        if (judul != null && !judul.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("judul")),
                    "%" + judul.toLowerCase() + "%"));
        }

        if (tipe != null && !tipe.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("tipe").get("id").in(tipe));
        }

        // Execute query with filters
        Page<Konten> page = repository.findAll(spec, pageable);

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
        if (judul != null && !judul.isEmpty()) {
            builder.queryParam("judul", judul);
        }

        if (tipe != null && !tipe.isEmpty()) {
            for (String tipeId : tipe) {
                builder.queryParam("tipe", tipeId);
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

        return new KontenPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "kontenSearchCache", key = "{#keyWord, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public KontenPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Konten> spec = Specification.where(null);

        // if (keyWord != null && !keyWord.isEmpty()) {
        //     String searchPattern = "%" + keyWord.toLowerCase() + "%";
        //     spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
        //             criteriaBuilder.like(criteriaBuilder.lower(root.get("judul")), searchPattern),
        //             criteriaBuilder.like(criteriaBuilder.lower(root.join("kataKunci")), searchPattern)));
        // }

        // Add case-insensitive LIKE filter for judul if provided
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("judul")), searchPattern)));
        }

        Page<Konten> page = repository.findAll(spec, pageable);

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

        return new KontenPageDTO(page, pageMetadata, links);
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