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
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastZip;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanPbakPdfShp;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanPerijinanPdf;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRehabPdf;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.repository.KegiatanRepository;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;


@Service("tmkhKegiatanServiceImpl")
public class KegiatanServiceImpl implements KegiatanService {
    private final KegiatanRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Kegiatan/";

    @Autowired
    public KegiatanServiceImpl(KegiatanRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<Kegiatan> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanDTO findDTOById(Long id) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        KegiatanDTO kegiatanDTO = new KegiatanDTO(kegiatan);
        return kegiatanDTO;
    }

    @Override
    public Kegiatan findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
    }

    @Override
    public Kegiatan save(Kegiatan kegiatan) {
        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan update(Long id, Kegiatan kegiatan) {
        // Ensure the kegiatan exists
        findById(id);
        return repository.save(kegiatan);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        // Delete all perijinan PDF files from MinIO storage
        if (kegiatan.getKegiatanPerijinanPdfs() != null) {
            for (KegiatanPerijinanPdf file : kegiatan.getKegiatanPerijinanPdfs()) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete perijinan file: " + file.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all PBAK files from MinIO storage
        if (kegiatan.getKegiatanPakPdfShps() != null) {
            for (KegiatanPbakPdfShp file : kegiatan.getKegiatanPakPdfShps()) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete PBAK file: " + file.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all Rehab files from MinIO storage
        if (kegiatan.getKegiatanRehabPdfs() != null) {
            for (KegiatanRehabPdf file : kegiatan.getKegiatanRehabPdfs()) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete Rehab file: " + file.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all BAST files from MinIO storage
        if (kegiatan.getKegiatanBastZips() != null) {
            for (KegiatanBastZip file : kegiatan.getKegiatanBastZips()) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete BAST file: " + file.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    @Override
    public Kegiatan uploadPerijinanPdf(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/perijinan/";
            String filePath = folderName + fileName;

            KegiatanPerijinanPdf kegiatanPerijinanPdf = new KegiatanPerijinanPdf();
            kegiatanPerijinanPdf.setId(idfile);
            kegiatanPerijinanPdf.setKegiatan(kegiatan);
            kegiatanPerijinanPdf.setNamaAsli(file.getOriginalFilename());
            kegiatanPerijinanPdf.setNamaFile(fileName);
            kegiatanPerijinanPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanPerijinanPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanPerijinanPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanPerijinanPdf.setContentType(file.getContentType());
            kegiatanPerijinanPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanPerijinanPdf.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanPerijinanPdfs().add(kegiatanPerijinanPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanPerijinanPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan uploadPbakPdfShp(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            // Determine file type (pdf or shp)
            String fileType = determineFileType(file);
            fileValidationUtil.validateFileType(file, fileType);
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/pbak/";
            String filePath = folderName + fileName;

            KegiatanPbakPdfShp kegiatanPbakPdfShp = new KegiatanPbakPdfShp();
            kegiatanPbakPdfShp.setId(idfile);
            kegiatanPbakPdfShp.setKegiatan(kegiatan);
            kegiatanPbakPdfShp.setNamaAsli(file.getOriginalFilename());
            kegiatanPbakPdfShp.setNamaFile(fileName);
            kegiatanPbakPdfShp.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanPbakPdfShp.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanPbakPdfShp.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanPbakPdfShp.setContentType(file.getContentType());
            kegiatanPbakPdfShp.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanPbakPdfShp.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanPakPdfShps().add(kegiatanPbakPdfShp);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanPakPdfShps().removeIf(pbakF -> pbakF.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan uploadRehabPdf(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/rehab/";
            String filePath = folderName + fileName;

            KegiatanRehabPdf kegiatanRehabPdf = new KegiatanRehabPdf();
            kegiatanRehabPdf.setId(idfile);
            kegiatanRehabPdf.setKegiatan(kegiatan);
            kegiatanRehabPdf.setNamaAsli(file.getOriginalFilename());
            kegiatanRehabPdf.setNamaFile(fileName);
            kegiatanRehabPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanRehabPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanRehabPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanRehabPdf.setContentType(file.getContentType());
            kegiatanRehabPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanRehabPdf.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanRehabPdfs().add(kegiatanRehabPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanRehabPdfs().removeIf(rehab -> rehab.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan uploadBastZip(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "zip");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/bast/";
            String filePath = folderName + fileName;

            KegiatanBastZip kegiatanBastZip = new KegiatanBastZip();
            kegiatanBastZip.setId(idfile);
            kegiatanBastZip.setKegiatan(kegiatan);
            kegiatanBastZip.setNamaAsli(file.getOriginalFilename());
            kegiatanBastZip.setNamaFile(fileName);
            kegiatanBastZip.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanBastZip.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanBastZip.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanBastZip.setContentType(file.getContentType());
            kegiatanBastZip.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanBastZip.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanBastZips().add(kegiatanBastZip);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanBastZips().removeIf(bast -> bast.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deletePerijinanPdf(Long id, List<String> uuidPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanPerijinanPdfs().removeIf(file -> {
            if (uuidPdf.contains(file.getId().toString())) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this file from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this file in the list
            }
        });
        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deletePbakPdfShp(Long id, List<String> uuidPdfShp) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanPakPdfShps().removeIf(file -> {
            if (uuidPdfShp.contains(file.getId().toString())) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this file from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this file in the list
            }
        });
        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deleteRehabPdf(Long id, List<String> uuidRehabPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanRehabPdfs().removeIf(file -> {
            if (uuidRehabPdf.contains(file.getId().toString())) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this file from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this file in the list
            }
        });
        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deleteBastZip(Long id, List<String> uuidBastZip) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanBastZips().removeIf(file -> {
            if (uuidBastZip.contains(file.getId().toString())) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this file from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this file in the list
            }
        });
        return repository.save(kegiatan);
    }

    @Override
    public KegiatanPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Kegiatan> page = repository.findAll(pageable);

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

        return new KegiatanPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanPageDTO findByFiltersWithCache(String namaKegiatan, String status, List<String> bpdasList,
            Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<Kegiatan> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaKegiatan if provided
        if (namaKegiatan != null && !namaKegiatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaKegiatan")),
                    "%" + namaKegiatan.toLowerCase() + "%"));
        }

        // Add exact match filter for status if provided
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("status"), status));
        }

        // Add filter for bpdas list if provided
        if (bpdasList != null && !bpdasList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("bpdas").get("id").in(bpdasList));
        }

        // Execute query with filters
        Page<Kegiatan> page = repository.findAll(spec, pageable);

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
        if (namaKegiatan != null && !namaKegiatan.isEmpty()) {
            builder.queryParam("namaKegiatan", namaKegiatan);
        }
        if (status != null && !status.isEmpty()) {
            builder.queryParam("status", status);
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

        return new KegiatanPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Kegiatan> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("namaKegiatan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("deskripsi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("bpdas").get("namaBpdas")), searchPattern)));
        }

        Page<Kegiatan> page = repository.findAll(spec, pageable);

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

        return new KegiatanPageDTO(page, pageMetadata, links);
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

    private String determineFileType(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename().toLowerCase();
        
        if (contentType != null && contentType.contains("pdf") || filename.endsWith(".pdf")) {
            return "pdf";
        } else if (filename.endsWith(".shp") || filename.endsWith(".zip") || 
                  (contentType != null && (contentType.contains("zip") || contentType.contains("octet-stream")))) {
            return "shp";
        }
        
        // Default case
        return "unknown";
    }
}