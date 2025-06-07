package com.kehutanan.ppth.penghijauan.serahterima.service.impl;

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

import com.kehutanan.ppth.common.service.MinioStorageService;
import com.kehutanan.ppth.penghijauan.serahterima.dto.BastPageDTO;
import com.kehutanan.ppth.penghijauan.serahterima.model.Bast;
import com.kehutanan.ppth.penghijauan.serahterima.model.BastPdf;
import com.kehutanan.ppth.penghijauan.serahterima.model.dto.BastDTO;
import com.kehutanan.ppth.penghijauan.serahterima.repository.BastRepository;
import com.kehutanan.ppth.penghijauan.serahterima.service.BastService;
import com.kehutanan.ppth.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service("penghijauanBastService")
public class BastServiceImpl implements BastService {
    private final BastRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Transaksi/Bast/";

    @Autowired
    public BastServiceImpl(BastRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<Bast> findAll() {
        return repository.findAll();
    }

    @Override
    public BastDTO findDTOById(Long id) {
        Bast bast = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bast not found with id: " + id));

        BastDTO bastDTO = new BastDTO(bast);
        return bastDTO;
    }

    @Override
    public Bast findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bast not found with id: " + id));
    }

    @Override
    public Bast save(Bast bast) {
        return repository.save(bast);
    }

    @Override
    public Bast update(Long id, Bast bast) {
        return repository.save(bast);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Bast bast = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bast not found with id: " + id));

        // Delete all PDF files from MinIO storage
        if (bast.getBastPdfs() != null) {
            for (BastPdf pdf : bast.getBastPdfs()) {
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
    public Bast uploadBastPdf(Long id, List<MultipartFile> files) {
        Bast bast = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bast not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + bast.getId() + "/pdf/";
            String filePath = folderName + fileName;

            BastPdf bastPdf = new BastPdf();
            bastPdf.setId(idfile);
            bastPdf.setBast(bast);
            bastPdf.setNamaAsli(file.getOriginalFilename());
            bastPdf.setNamaFile(fileName);
            bastPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            bastPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            bastPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            bastPdf.setContentType(file.getContentType());
            bastPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            bastPdf.setContentType(file.getContentType());
            bastPdf.setUploadedAt(LocalDateTime.now());

            bast.getBastPdfs().add(bastPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                bast.getBastPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(bast);
    }

    @Override
    @Transactional
    public Bast deleteBastPdf(Long id, List<String> uuidPdf) {
        Bast bast = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bast not found with id: " + id));

        bast.getBastPdfs().removeIf(file -> {
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
        return repository.save(bast);
    }

    @Override
    public BastPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Bast> page = repository.findAll(pageable);

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

        return new BastPageDTO(page, pageMetadata, links);
    }

    @Override
    public BastPageDTO findByFiltersWithCache(String nomor, String kontrak, List<String> bpdas,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<Bast> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for nomorBast if provided
        if (nomor != null && !nomor.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nomorBast")),
                    "%" + nomor.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for kontrak if provided
        if (kontrak != null && !kontrak.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kegiatan").get("kontrak").get("nomorKontrak")),
                    "%" + kontrak.toLowerCase() + "%"));
        }

        if (bpdas != null && !bpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("kegiatan").get("kontrak").get("bpdas").get("id").in(bpdas));
        }

        // Execute query with filters
        Page<Bast> page = repository.findAll(spec, pageable);

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
        if (kontrak != null && !kontrak.isEmpty()) {
            builder.queryParam("kontrak", kontrak);
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

        return new BastPageDTO(page, pageMetadata, links);
    }

    @Override
    public BastPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Bast> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nomorBast")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("deskripsi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kegiatan").get("namaKegiatan")), searchPattern)));
        }

        Page<Bast> page = repository.findAll(spec, pageable);

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

        return new BastPageDTO(page, pageMetadata, links);
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