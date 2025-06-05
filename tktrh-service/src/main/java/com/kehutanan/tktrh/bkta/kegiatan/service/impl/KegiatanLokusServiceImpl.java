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

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanLokusPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokus;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokusBangunanPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokusLokasiPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokusProposalPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanLokusDTO;
import com.kehutanan.tktrh.bkta.kegiatan.repository.KegiatanLokusRepository;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanLokusService;
import com.kehutanan.tktrh.common.service.MinioStorageService;
import com.kehutanan.tktrh.util.FileValidationUtil;

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

        return new KegiatanLokusDTO(kegiatanLokus);
    }

    @Override
    public KegiatanLokus findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));
    }

    @Override
    public KegiatanLokus save(KegiatanLokus kegiatanLokus) {
        return repository.save(kegiatanLokus);
    }

    @Override
    public KegiatanLokus update(Long id, KegiatanLokus kegiatanLokus) {
        return repository.save(kegiatanLokus);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        // Delete all Proposal PDF files from MinIO storage
        if (kegiatanLokus.getLokusProposalPdfs() != null) {
            for (KegiatanLokusProposalPdf pdf : kegiatanLokus.getLokusProposalPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete Proposal PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all Lokasi PDF files from MinIO storage
        if (kegiatanLokus.getLokusLokasiPdfs() != null) {
            for (KegiatanLokusLokasiPdf pdf : kegiatanLokus.getLokusLokasiPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete Lokasi PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all Bangunan PDF files from MinIO storage
        if (kegiatanLokus.getLokusBangunanPdfs() != null) {
            for (KegiatanLokusBangunanPdf pdf : kegiatanLokus.getLokusBangunanPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete Bangunan PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    @Override
    public KegiatanLokus uploadProposalPdf(Long id, List<MultipartFile> files) {
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatanLokus.getId() + "/proposal/";
            String filePath = folderName + fileName;

            KegiatanLokusProposalPdf proposalPdf = new KegiatanLokusProposalPdf();
            proposalPdf.setId(idfile);
            proposalPdf.setKegiatanLokus(kegiatanLokus);
            proposalPdf.setNamaAsli(file.getOriginalFilename());
            proposalPdf.setNamaFile(fileName);
            proposalPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            proposalPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            proposalPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            proposalPdf.setContentType(file.getContentType());
            proposalPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            proposalPdf.setUploadedAt(LocalDateTime.now());

            kegiatanLokus.getLokusProposalPdfs().add(proposalPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatanLokus.getLokusProposalPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatanLokus);
    }

    @Override
    public KegiatanLokus uploadLokasiPdf(Long id, List<MultipartFile> files) {
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatanLokus.getId() + "/lokasi/";
            String filePath = folderName + fileName;

            KegiatanLokusLokasiPdf lokasiPdf = new KegiatanLokusLokasiPdf();
            lokasiPdf.setId(idfile);
            lokasiPdf.setKegiatanLokus(kegiatanLokus);
            lokasiPdf.setNamaAsli(file.getOriginalFilename());
            lokasiPdf.setNamaFile(fileName);
            lokasiPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            lokasiPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            lokasiPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            lokasiPdf.setContentType(file.getContentType());
            lokasiPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            lokasiPdf.setUploadedAt(LocalDateTime.now());

            kegiatanLokus.getLokusLokasiPdfs().add(lokasiPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatanLokus.getLokusLokasiPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatanLokus);
    }

    @Override
    public KegiatanLokus uploadBangunanPdf(Long id, List<MultipartFile> files) {
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatanLokus.getId() + "/bangunan/";
            String filePath = folderName + fileName;

            KegiatanLokusBangunanPdf bangunanPdf = new KegiatanLokusBangunanPdf();
            bangunanPdf.setId(idfile);
            bangunanPdf.setKegiatanLokus(kegiatanLokus);
            bangunanPdf.setNamaAsli(file.getOriginalFilename());
            bangunanPdf.setNamaFile(fileName);
            bangunanPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            bangunanPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            bangunanPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            bangunanPdf.setContentType(file.getContentType());
            bangunanPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            bangunanPdf.setUploadedAt(LocalDateTime.now());

            kegiatanLokus.getLokusBangunanPdfs().add(bangunanPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatanLokus.getLokusBangunanPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatanLokus);
    }

    @Override
    @Transactional
    public KegiatanLokus deleteProposalPdf(Long id, List<String> uuidPdf) {
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        kegiatanLokus.getLokusProposalPdfs().removeIf(file -> {
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
        return repository.save(kegiatanLokus);
    }

    @Override
    @Transactional
    public KegiatanLokus deleteLokasiPdf(Long id, List<String> uuidPdf) {
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        kegiatanLokus.getLokusLokasiPdfs().removeIf(file -> {
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
        return repository.save(kegiatanLokus);
    }

    @Override
    @Transactional
    public KegiatanLokus deleteBangunanPdf(Long id, List<String> uuidPdf) {
        KegiatanLokus kegiatanLokus = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanLokus not found with id: " + id));

        kegiatanLokus.getLokusBangunanPdfs().removeIf(file -> {
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
        return repository.save(kegiatanLokus);
    }

    @Override
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
    public KegiatanLokusPageDTO findByFiltersWithCache(Long kegiatanId, String provinsi, String catatan, 
            List<String> status, Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<KegiatanLokus> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for provinsi if provided
        if (provinsi != null && !provinsi.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("provinsi")),
                    "%" + provinsi.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for catatan if provided
        if (catatan != null && !catatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("catatan")),
                    "%" + catatan.toLowerCase() + "%"));
        }

        // Add filter for status if provided
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("status").in(status));
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
        if (provinsi != null && !provinsi.isEmpty()) {
            builder.queryParam("provinsi", provinsi);
        }
        if (catatan != null && !catatan.isEmpty()) {
            builder.queryParam("catatan", catatan);
        }
        if (status != null && !status.isEmpty()) {
            for (String s : status) {
                builder.queryParam("status", s);
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
    public KegiatanLokusPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanLokus> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add search filter if keyword is provided
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("namaLokus")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("provinsi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kabupaten")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kecamatan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("desa")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("catatan")), searchPattern)));
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

        // Add kegiatanId parameter if provided
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
        }
        
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