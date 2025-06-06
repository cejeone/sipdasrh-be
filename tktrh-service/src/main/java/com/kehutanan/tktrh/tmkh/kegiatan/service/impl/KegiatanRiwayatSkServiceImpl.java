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
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanRiwayatSkPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRiwayatSk;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRiwayatSkShp;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanRiwayatSkDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.repository.KegiatanRiwayatSkRepository;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanRiwayatSkService;
import com.kehutanan.tktrh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanRiwayatSkServiceImpl implements KegiatanRiwayatSkService {
    private final KegiatanRiwayatSkRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Kegiatan/RiwayatSk/";

    @Autowired
    public KegiatanRiwayatSkServiceImpl(KegiatanRiwayatSkRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<KegiatanRiwayatSk> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanRiwayatSkDTO findDTOById(Long id) {
        KegiatanRiwayatSk riwayatSk = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanRiwayatSk not found with id: " + id));

        KegiatanRiwayatSkDTO riwayatSkDTO = new KegiatanRiwayatSkDTO(riwayatSk);
        return riwayatSkDTO;
    }

    @Override
    public KegiatanRiwayatSk findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanRiwayatSk not found with id: " + id));
    }

    @Override
    public KegiatanRiwayatSk save(KegiatanRiwayatSk riwayatSk) {
        return repository.save(riwayatSk);
    }

    @Override
    public KegiatanRiwayatSk update(Long id, KegiatanRiwayatSk riwayatSk) {
        // Ensure the entity exists before updating
        findById(id);
        return repository.save(riwayatSk);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        KegiatanRiwayatSk riwayatSk = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanRiwayatSk not found with id: " + id));

        // Delete all SHP files from MinIO storage
        if (riwayatSk.getKegiatanRiwayatSkShps() != null) {
            for (KegiatanRiwayatSkShp shp : riwayatSk.getKegiatanRiwayatSkShps()) {
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
    public KegiatanRiwayatSk uploadShp(Long id, List<MultipartFile> files) {
        KegiatanRiwayatSk riwayatSk = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanRiwayatSk not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "shp");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + riwayatSk.getId() + "/shp/";
            String filePath = folderName + fileName;

            KegiatanRiwayatSkShp shpFile = new KegiatanRiwayatSkShp();
            shpFile.setId(idfile);
            shpFile.setKegiatanRiwayatSk(riwayatSk);
            shpFile.setNamaAsli(file.getOriginalFilename());
            shpFile.setNamaFile(fileName);
            shpFile.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            shpFile.setViewUrl("/file/view?fileName=" + encodedPath);
            shpFile.setDownloadUrl("/file/download?fileName=" + encodedPath);
            shpFile.setContentType(file.getContentType());
            shpFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            shpFile.setUploadedAt(LocalDateTime.now());

            if (riwayatSk.getKegiatanRiwayatSkShps() == null) {
                riwayatSk.setKegiatanRiwayatSkShps(null);
            }
            riwayatSk.getKegiatanRiwayatSkShps().add(shpFile);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                riwayatSk.getKegiatanRiwayatSkShps().removeIf(shp -> shp.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(riwayatSk);
    }

    @Override
    @Transactional
    public KegiatanRiwayatSk deleteShp(Long id, List<String> uuidShp) {
        KegiatanRiwayatSk riwayatSk = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanRiwayatSk not found with id: " + id));

        if (riwayatSk.getKegiatanRiwayatSkShps() != null) {
            riwayatSk.getKegiatanRiwayatSkShps().removeIf(file -> {
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
        }
        
        return repository.save(riwayatSk);
    }

    @Override
    public KegiatanRiwayatSkPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanRiwayatSk> page = repository.findAll(pageable);

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

        return new KegiatanRiwayatSkPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanRiwayatSkPageDTO findByFiltersWithCache(Long kegiatanId, String skPenetapan, String keterangan, 
            List<String> statusList, Pageable pageable, String baseUrl) {
            
        // Create specification based on filters
        Specification<KegiatanRiwayatSk> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for skPenetapan if provided
        if (skPenetapan != null && !skPenetapan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("skPenetapan")),
                    "%" + skPenetapan.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for keterangan if provided
        if (keterangan != null && !keterangan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("keterangan")),
                    "%" + keterangan.toLowerCase() + "%"));
        }

        // Add status filter if provided
        if (statusList != null && !statusList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("status").in(statusList));
        }

        // Execute query with filters
        Page<KegiatanRiwayatSk> page = repository.findAll(spec, pageable);

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
        if (skPenetapan != null && !skPenetapan.isEmpty()) {
            builder.queryParam("skPenetapan", skPenetapan);
        }
        if (keterangan != null && !keterangan.isEmpty()) {
            builder.queryParam("keterangan", keterangan);
        }
        if (statusList != null && !statusList.isEmpty()) {
            for (String status : statusList) {
                builder.queryParam("statusList", status);
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

        return new KegiatanRiwayatSkPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanRiwayatSkPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanRiwayatSk> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("skPenetapan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kegiatan").get("namaKegiatan")), searchPattern)));
        }

        Page<KegiatanRiwayatSk> page = repository.findAll(spec, pageable);

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

        return new KegiatanRiwayatSkPageDTO(page, pageMetadata, links);
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