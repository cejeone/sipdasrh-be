package com.kehutanan.tktrh.bkta.kegiatan.service.impl;

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

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanBastPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanBast;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanBastPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanBastDTO;
import com.kehutanan.tktrh.bkta.kegiatan.repository.KegiatanBastRepository;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanBastService;
import com.kehutanan.tktrh.common.service.MinioStorageService;
import com.kehutanan.tktrh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanBastServiceImpl implements KegiatanBastService {
    
    private final KegiatanBastRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "BKTA/KegiatanBast/";
    
    @Autowired
    public KegiatanBastServiceImpl(KegiatanBastRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }
    
    @Override
    public List<KegiatanBast> findAll() {
        return repository.findAll();
    }
    
    @Override
    public KegiatanBastDTO findDTOById(Long id) {
        KegiatanBast kegiatanBast = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanBast not found with id: " + id));
        
        KegiatanBastDTO kegiatanBastDTO = new KegiatanBastDTO(kegiatanBast);
        return kegiatanBastDTO;
    }
    
    @Override
    public KegiatanBast findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanBast not found with id: " + id));
    }
    
    @Override
    public KegiatanBast save(KegiatanBast kegiatanBast) {
        return repository.save(kegiatanBast);
    }
    
    @Override
    public KegiatanBast update(Long id, KegiatanBast kegiatanBast) {
        // Check if entity exists
        findById(id);
        return repository.save(kegiatanBast);
    }
    
    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        KegiatanBast kegiatanBast = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanBast not found with id: " + id));
        
        // Delete all PDF files from MinIO storage
        if (kegiatanBast.getKegiatanKontrakPdfs() != null) {
            for (KegiatanBastPdf pdf : kegiatanBast.getKegiatanKontrakPdfs()) {
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
    public KegiatanBast uploadKegiatanBastPdf(Long id, List<MultipartFile> files) {
        KegiatanBast kegiatanBast = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanBast not found with id: " + id));
    
        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();
            
            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatanBast.getId() + "/pdf/";
            String filePath = folderName + fileName;
            
            KegiatanBastPdf kegiatanBastPdf = new KegiatanBastPdf();
            kegiatanBastPdf.setId(idfile);
            kegiatanBastPdf.setKegiatanBast(kegiatanBast);
            kegiatanBastPdf.setNamaAsli(file.getOriginalFilename());
            kegiatanBastPdf.setNamaFile(fileName);
            kegiatanBastPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }
            
            kegiatanBastPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanBastPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanBastPdf.setContentType(file.getContentType());
            kegiatanBastPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanBastPdf.setUploadedAt(LocalDateTime.now());
            
            kegiatanBast.getKegiatanKontrakPdfs().add(kegiatanBastPdf);
            
            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatanBast.getKegiatanKontrakPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }
        
        return repository.save(kegiatanBast);
    }
    
    @Override
    @Transactional
    public KegiatanBast deleteKegiatanBastPdf(Long id, List<String> uuidPdf) {
        KegiatanBast kegiatanBast = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanBast not found with id: " + id));
        
        kegiatanBast.getKegiatanKontrakPdfs().removeIf(file -> {
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
        return repository.save(kegiatanBast);
    }
    
    @Override
    public KegiatanBastPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanBast> page = repository.findAll(pageable);
        
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
        
        return new KegiatanBastPageDTO(page, pageMetadata, links);
    }
    
    @Override
    public KegiatanBastPageDTO findByFiltersWithCache(Long kegiatanId, String identitasBkta, String jenisBkta, 
            List<String> status, Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<KegiatanBast> spec = Specification.where(null);
        
        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }
        
        // Add case-insensitive LIKE filter for identitasBkta if provided
        if (identitasBkta != null && !identitasBkta.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("identitasBkta").get("name")),
                    "%" + identitasBkta.toLowerCase() + "%"));
        }
        
        // Add case-insensitive LIKE filter for jenisBkta if provided
        if (jenisBkta != null && !jenisBkta.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("jenisBkta").get("name")),
                    "%" + jenisBkta.toLowerCase() + "%"));
        }
        
        // Add filter for status if provided
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("status").get("id").in(status));
        }
        
        // Execute query with filters
        Page<KegiatanBast> page = repository.findAll(spec, pageable);
        
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
        if (identitasBkta != null && !identitasBkta.isEmpty()) {
            builder.queryParam("identitasBkta", identitasBkta);
        }
        if (jenisBkta != null && !jenisBkta.isEmpty()) {
            builder.queryParam("jenisBkta", jenisBkta);
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
        
        return new KegiatanBastPageDTO(page, pageMetadata, links);
    }
    
    @Override
    public KegiatanBastPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanBast> spec = Specification.where(null);
        
        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }
        
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("identitasBkta").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("jenisBkta").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("kelompokMasyarakat").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("status").get("name")), searchPattern)));
        }
        
        Page<KegiatanBast> page = repository.findAll(spec, pageable);
        
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
        
        return new KegiatanBastPageDTO(page, pageMetadata, links);
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