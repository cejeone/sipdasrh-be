package com.kehutanan.tktrh.perijinan.service.impl;

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
import com.kehutanan.tktrh.perijinan.dto.PerijinanPageDTO;
import com.kehutanan.tktrh.perijinan.model.Perijinan;
import com.kehutanan.tktrh.perijinan.model.PerijinanDokumenAwalPdf;
import com.kehutanan.tktrh.perijinan.model.PerijinanDokumenBastPdf;
import com.kehutanan.tktrh.perijinan.model.dto.PerijinanDTO;
import com.kehutanan.tktrh.perijinan.repository.PerijinanRepository;
import com.kehutanan.tktrh.perijinan.service.PerijinanService;
import com.kehutanan.tktrh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PerijinanServiceImpl implements PerijinanService {
    private final PerijinanRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Perijinan/";

    @Autowired
    public PerijinanServiceImpl(PerijinanRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<Perijinan> findAll() {
        return repository.findAll();
    }

    @Override
    public PerijinanDTO findDTOById(Long id) {
        Perijinan perijinan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perijinan not found with id: " + id));

        PerijinanDTO perijinanDTO = new PerijinanDTO(perijinan);
        return perijinanDTO;
    }

    @Override
    public Perijinan findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perijinan not found with id: " + id));
    }

    @Override
    public Perijinan save(Perijinan perijinan) {
        return repository.save(perijinan);
    }

    @Override
    public Perijinan update(Long id, Perijinan perijinan) {
        return repository.save(perijinan);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Perijinan perijinan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perijinan not found with id: " + id));

        // Delete all dokumen awal PDF files from MinIO storage
        if (perijinan.getDokumenAwalPdfs() != null) {
            for (PerijinanDokumenAwalPdf pdf : perijinan.getDokumenAwalPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all dokumen bast PDF files from MinIO storage
        if (perijinan.getDokumenBastPdfs() != null) {
            for (PerijinanDokumenBastPdf pdf : perijinan.getDokumenBastPdfs()) {
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
    public Perijinan uploadDokumenAwalPdf(Long id, List<MultipartFile> files) {
        Perijinan perijinan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perijinan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + perijinan.getId() + "/dokumen-awal/";
            String filePath = folderName + fileName;

            PerijinanDokumenAwalPdf dokumenAwalPdf = new PerijinanDokumenAwalPdf();
            dokumenAwalPdf.setId(idfile);
            dokumenAwalPdf.setPerijinan(perijinan);
            dokumenAwalPdf.setNamaAsli(file.getOriginalFilename());
            dokumenAwalPdf.setNamaFile(fileName);
            dokumenAwalPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            dokumenAwalPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            dokumenAwalPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            dokumenAwalPdf.setContentType(file.getContentType());
            dokumenAwalPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            dokumenAwalPdf.setUploadedAt(LocalDateTime.now());

            perijinan.getDokumenAwalPdfs().add(dokumenAwalPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                perijinan.getDokumenAwalPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(perijinan);
    }
    
    @Override
    public Perijinan uploadDokumenBastPdf(Long id, List<MultipartFile> files) {
        Perijinan perijinan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perijinan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + perijinan.getId() + "/dokumen-bast/";
            String filePath = folderName + fileName;

            PerijinanDokumenBastPdf dokumenBastPdf = new PerijinanDokumenBastPdf();
            dokumenBastPdf.setId(idfile);
            dokumenBastPdf.setPerijinan(perijinan);
            dokumenBastPdf.setNamaAsli(file.getOriginalFilename());
            dokumenBastPdf.setNamaFile(fileName);
            dokumenBastPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            dokumenBastPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            dokumenBastPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            dokumenBastPdf.setContentType(file.getContentType());
            dokumenBastPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            dokumenBastPdf.setUploadedAt(LocalDateTime.now());

            perijinan.getDokumenBastPdfs().add(dokumenBastPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                perijinan.getDokumenBastPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(perijinan);
    }

    @Override
    @Transactional
    public Perijinan deleteDokumenAwalPdf(Long id, List<String> uuidPdf) {
        Perijinan perijinan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perijinan not found with id: " + id));

        perijinan.getDokumenAwalPdfs().removeIf(file -> {
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
        return repository.save(perijinan);
    }
    
    @Override
    @Transactional
    public Perijinan deleteDokumenBastPdf(Long id, List<String> uuidPdf) {
        Perijinan perijinan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perijinan not found with id: " + id));

        perijinan.getDokumenBastPdfs().removeIf(file -> {
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
        return repository.save(perijinan);
    }

    @Override
    public PerijinanPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Perijinan> page = repository.findAll(pageable);

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

        return new PerijinanPageDTO(page, pageMetadata, links);
    }

    @Override
    public PerijinanPageDTO findByFiltersWithCache(String pelakuUsaha, List<String> bpdas, Pageable pageable, String baseUrl) {
        // Create specification based on filters
        Specification<Perijinan> spec = Specification.where(null);

        // Add filter for pelakuUsaha if provided
        if (pelakuUsaha != null && !pelakuUsaha.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("pelakuUsahaId").get("name")),
                    "%" + pelakuUsaha.toLowerCase() + "%"));
        }

        // Filter by bpdas list if provided
        if (bpdas != null && !bpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("bpdas").get("id").in(bpdas));
        }

        // Execute query with filters
        Page<Perijinan> page = repository.findAll(spec, pageable);

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
        if (pelakuUsaha != null && !pelakuUsaha.isEmpty()) {
            builder.queryParam("pelakuUsaha", pelakuUsaha);
        }
        if (bpdas != null && !bpdas.isEmpty()) {
            for (String bpdasId : bpdas) {
                builder.queryParam("bpdas", bpdasId);
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

        return new PerijinanPageDTO(page, pageMetadata, links);
    }

    @Override
    public PerijinanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Perijinan> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("pelakuUsahaId").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("levelId").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("statusId").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern)));
        }

        Page<Perijinan> page = repository.findAll(spec, pageable);

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

        return new PerijinanPageDTO(page, pageMetadata, links);
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