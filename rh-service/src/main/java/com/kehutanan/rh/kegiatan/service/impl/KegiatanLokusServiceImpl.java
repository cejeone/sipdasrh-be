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
import com.kehutanan.rh.kegiatan.dto.KegiatanLokusDTO;
import com.kehutanan.rh.kegiatan.dto.KegiatanLokusPageDTO;
import com.kehutanan.rh.kegiatan.model.KegiatanLokus;
import com.kehutanan.rh.kegiatan.model.KegiatanLokusShp;
import com.kehutanan.rh.kegiatan.repository.KegiatanLokusRepository;
import com.kehutanan.rh.kegiatan.service.KegiatanLokusService;
import com.kehutanan.rh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanLokusServiceImpl implements KegiatanLokusService {
    private final KegiatanLokusRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Kegiatan/KegiatanLokus/";

    @Autowired
    public KegiatanLokusServiceImpl(KegiatanLokusRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<KegiatanLokus> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanLokusDTO findDTOById(Long id) {
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        KegiatanLokusDTO kegiatanLokusDTO = new KegiatanLokusDTO(kegiatanLokus);

        return kegiatanLokusDTO;
    }

    @Override
    public KegiatanLokus findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));
    }

    @Override
    @CachePut(value = "kegiatanLokusCache", key = "#kegiatanLokus.id")
    public KegiatanLokus save(KegiatanLokus kegiatanLokus) {
        return repository.save(kegiatanLokus);
    }

    @Override
    @CachePut(value = "kegiatanLokusCache", key = "#id")
    public KegiatanLokus update(Long id, KegiatanLokus kegiatanLokus) {
        // Ensure the kegiatanLokus exists
        repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));
        
        return repository.save(kegiatanLokus);
    }

    @Override
    @CacheEvict(value = "kegiatanLokusCache", key = "#id")
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        // Delete all SHP files from MinIO storage if they exist
        if (kegiatanLokus.getKegiatanLokusShps() != null) {
            for (KegiatanLokusShp shp : kegiatanLokus.getKegiatanLokusShps()) {
                try {
                    minioStorageService.deleteFile("", shp.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete SHP file: " + shp.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    @Override
    @CachePut(value = "kegiatanLokusCache", key = "#id")
    public KegiatanLokus uploadKegiatanLokusShp(Long id, List<MultipartFile> files) {
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "shp");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatanLokus.getId() + "/shp/";
            String filePath = folderName + fileName;

            KegiatanLokusShp kegiatanLokusShp = new KegiatanLokusShp();
            kegiatanLokusShp.setId(idfile);
            kegiatanLokusShp.setKegiatanLokus(kegiatanLokus);
            kegiatanLokusShp.setNamaAsli(file.getOriginalFilename());
            kegiatanLokusShp.setNamaFile(fileName);
            kegiatanLokusShp.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanLokusShp.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanLokusShp.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanLokusShp.setContentType(file.getContentType());
            kegiatanLokusShp.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanLokusShp.setUploadedAt(LocalDateTime.now());

            if (kegiatanLokus.getKegiatanLokusShps() == null) {
                kegiatanLokus.setKegiatanLokusShps(new ArrayList<>());
            }
            kegiatanLokus.getKegiatanLokusShps().add(kegiatanLokusShp);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatanLokus.getKegiatanLokusShps().removeIf(shp -> shp.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatanLokus);
    }

    @Override
    @CachePut(value = "kegiatanLokusCache", key = "#id")
    @Transactional
    public KegiatanLokus deleteKegiatanLokusShp(Long id, List<String> uuidShp) {
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        if (kegiatanLokus.getKegiatanLokusShps() != null) {
            kegiatanLokus.getKegiatanLokusShps().removeIf(file -> {
                if (uuidShp.contains(file.getId().toString())) {
                    try {
                        // Delete file from MinIO storage
                        minioStorageService.deleteFile("", file.getPathFile());
                        return true; // Remove this shp from the list
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                    }
                } else {
                    return false; // Keep this shp in the list
                }
            });
        }
        
        return repository.save(kegiatanLokus);
    }

    @Override
    @Cacheable(value = "kegiatanLokusPageCache", key = "'all:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public KegiatanLokusPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanLokus> page = repository.findAll(pageable);

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

        return new KegiatanLokusPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "kegiatanLokusPageCache", key = "'filters:' + #kegiatanId + ':' + #keterangan + ':' + #provinsi + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public KegiatanLokusPageDTO findByFiltersWithCache(Long kegiatanId, String keterangan, List<String> provinsi,
            Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<KegiatanLokus> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for keterangan if provided
        if (keterangan != null && !keterangan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("keterangan")),
                    "%" + keterangan.toLowerCase() + "%"));
        }

        // Add filter for provinsi list if provided
        if (provinsi != null && !provinsi.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("provinsi").get("id").in(provinsi));
        }

        // Execute query with filters
        Page<KegiatanLokus> page = repository.findAll(spec, pageable);

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
        if (keterangan != null && !keterangan.isEmpty()) {
            builder.queryParam("keterangan", keterangan);
        }
        if (provinsi != null && !provinsi.isEmpty()) {
            for (String prov : provinsi) {
                builder.queryParam("provinsi", prov);
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

        return new KegiatanLokusPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "kegiatanLokusPageCache", key = "'search:' + #kegiatanId + ':' + #keyWord + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public KegiatanLokusPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanLokus> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add search filter if keyword provided
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("provinsi").get("namaProvinsi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("kabupatenKota").get("namaKabupatenKota")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("kecamatan").get("namaKecamatan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("kelurahanDesa").get("namaKelurahanDesa")), searchPattern)
            ));
        }

        Page<KegiatanLokus> page = repository.findAll(spec, pageable);

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

        return new KegiatanLokusPageDTO(page, pageMetadata, links);
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