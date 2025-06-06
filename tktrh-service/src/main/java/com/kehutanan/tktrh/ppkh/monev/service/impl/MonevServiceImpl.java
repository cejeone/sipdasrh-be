package com.kehutanan.tktrh.ppkh.monev.service.impl;

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
import com.kehutanan.tktrh.ppkh.monev.dto.MonevPageDTO;
import com.kehutanan.tktrh.ppkh.monev.model.Monev;
import com.kehutanan.tktrh.ppkh.monev.model.MonevPdf;
import com.kehutanan.tktrh.ppkh.monev.model.dto.MonevDTO;
import com.kehutanan.tktrh.ppkh.monev.repository.MonevRepository;
import com.kehutanan.tktrh.ppkh.monev.service.MonevService;
import com.kehutanan.tktrh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MonevServiceImpl implements MonevService {
    
    private final MonevRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "PPKH/Monev/";
    
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
        
        return new MonevDTO(monev);
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
        // Ensure the monev exists
        findById(id);
        return repository.save(monev);
    }
    
    @Override
    public void deleteById(Long id) {
        Monev monev = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));
        
        // Delete all PDF files from MinIO storage
        if (monev.getMonevPdfs() != null) {
            for (MonevPdf pdf : monev.getMonevPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
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
    public MonevPageDTO findByFiltersWithCache(String namaProgram, String audiensi, List<String> bpdasList,
            Pageable pageable, String baseUrl) {
            
        // Create specification based on filters
        Specification<Monev> spec = Specification.where(null);
        
        // Add case-insensitive LIKE filter for namaProgram if provided
        if (namaProgram != null && !namaProgram.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("program").get("namaProgram")),
                    "%" + namaProgram.toLowerCase() + "%"));
        }
        
        // Add case-insensitive LIKE filter for audiensi if provided
        if (audiensi != null && !audiensi.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("audiensi")),
                    "%" + audiensi.toLowerCase() + "%"));
        }
        
        // Add filter for BPDAS IDs if provided
        if (bpdasList != null && !bpdasList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("bpdas").get("id").in(bpdasList));
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
        if (namaProgram != null && !namaProgram.isEmpty()) {
            builder.queryParam("namaProgram", namaProgram);
        }
        if (audiensi != null && !audiensi.isEmpty()) {
            builder.queryParam("audiensi", audiensi);
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
        
        return new MonevPageDTO(page, pageMetadata, links);
    }

    @Override
    public MonevPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Monev> spec = Specification.where(null);
        
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("program").get("namaProgram")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("subjek")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("audiensi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("isuTindakLanjut")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("bpdas").get("namaBpdas")), searchPattern)));
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