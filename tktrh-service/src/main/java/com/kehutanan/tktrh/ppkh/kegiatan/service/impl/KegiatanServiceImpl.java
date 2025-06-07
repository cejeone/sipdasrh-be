package com.kehutanan.tktrh.ppkh.kegiatan.service.impl;

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
import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastZip;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanPakPdfShp;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanPerijinanPdf;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRantekPdf;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.repository.KegiatanRepository;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service("ppkhKegiatanServiceImpl") 
public class KegiatanServiceImpl implements KegiatanService {
    private final KegiatanRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private final String FOLDER_PREFIX = "PPKH/Kegiatan/";

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

        return new KegiatanDTO(kegiatan);
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
        // Check if entity exists
        repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
        return repository.save(kegiatan);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        // Delete all associated files from MinIO storage
        deleteAllAssociatedFiles(kegiatan);

        // Delete the entity from the database
        repository.deleteById(id);
    }

    private void deleteAllAssociatedFiles(Kegiatan kegiatan) {
        // Delete PerijinanPdf files
        if (kegiatan.getKegiatanPerijinanPdfs() != null) {
            for (KegiatanPerijinanPdf file : kegiatan.getKegiatanPerijinanPdfs()) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete PerijinanPdf file: " + file.getPathFile() + " - " + e.getMessage());
                }
            }
        }
        

        
        // Delete PakPdfShp files
        if (kegiatan.getKegiatanPakPdfShps() != null) {
            for (KegiatanPakPdfShp file : kegiatan.getKegiatanPakPdfShps()) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete PakPdfShp file: " + file.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        
        // Delete RantekPdf files
        if (kegiatan.getKegiatanRantekPdfs() != null) {
            for (KegiatanRantekPdf file : kegiatan.getKegiatanRantekPdfs()) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete RantekPdf file: " + file.getPathFile() + " - " + e.getMessage());
                }
            }
        }
        


        
        // Delete BastZip files
        if (kegiatan.getKegiatanBastZips() != null) {
            for (KegiatanBastZip file : kegiatan.getKegiatanBastZips()) {
                try {
                    minioStorageService.deleteFile("", file.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete BastZip file: " + file.getPathFile() + " - " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Kegiatan uploadPerijinanPdf(Long id, List<MultipartFile> perijinanPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : perijinanPdf) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idFile = UUID.randomUUID();

            String fileName = idFile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/perijinanPdf/";
            String filePath = folderName + fileName;

            KegiatanPerijinanPdf kegiatanFile = new KegiatanPerijinanPdf();
            kegiatanFile.setId(idFile);
            kegiatanFile.setKegiatan(kegiatan);
            kegiatanFile.setNamaAsli(file.getOriginalFilename());
            kegiatanFile.setNamaFile(fileName);
            kegiatanFile.setPathFile(filePath);
            
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanFile.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanFile.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanFile.setContentType(file.getContentType());
            kegiatanFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanPerijinanPdfs().add(kegiatanFile);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanPerijinanPdfs().removeIf(pdf -> pdf.getId().equals(idFile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

   

    @Override
    public Kegiatan uploadPakPdfShp(Long id, List<MultipartFile> pakPdfShp) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : pakPdfShp) {
            // Validate for either pdf or shp file types
            fileValidationUtil.validateFileSize(file);
            UUID idFile = UUID.randomUUID();

            String fileName = idFile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/pakPdfShp/";
            String filePath = folderName + fileName;

            KegiatanPakPdfShp kegiatanFile = new KegiatanPakPdfShp();
            kegiatanFile.setId(idFile);
            kegiatanFile.setKegiatan(kegiatan);
            kegiatanFile.setNamaAsli(file.getOriginalFilename());
            kegiatanFile.setNamaFile(fileName);
            kegiatanFile.setPathFile(filePath);
            
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanFile.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanFile.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanFile.setContentType(file.getContentType());
            kegiatanFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanPakPdfShps().add(kegiatanFile);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanPakPdfShps().removeIf(pak -> pak.getId().equals(idFile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }


    @Override
    public Kegiatan uploadRantekPdf(Long id, List<MultipartFile> rantekPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : rantekPdf) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idFile = UUID.randomUUID();

            String fileName = idFile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/rantekPdf/";
            String filePath = folderName + fileName;

            KegiatanRantekPdf kegiatanFile = new KegiatanRantekPdf();
            kegiatanFile.setId(idFile);
            kegiatanFile.setKegiatan(kegiatan);
            kegiatanFile.setNamaAsli(file.getOriginalFilename());
            kegiatanFile.setNamaFile(fileName);
            kegiatanFile.setPathFile(filePath);
            
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanFile.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanFile.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanFile.setContentType(file.getContentType());
            kegiatanFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanRantekPdfs().add(kegiatanFile);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanRantekPdfs().removeIf(rantek -> rantek.getId().equals(idFile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    public Kegiatan uploadBastZip(Long id, List<MultipartFile> bastZip) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        for (MultipartFile file : bastZip) {
            fileValidationUtil.validateFileType(file, "zip");
            fileValidationUtil.validateFileSize(file);
            UUID idFile = UUID.randomUUID();

            String fileName = idFile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/bastZip/";
            String filePath = folderName + fileName;

            KegiatanBastZip kegiatanFile = new KegiatanBastZip();
            kegiatanFile.setId(idFile);
            kegiatanFile.setKegiatan(kegiatan);
            kegiatanFile.setNamaAsli(file.getOriginalFilename());
            kegiatanFile.setNamaFile(fileName);
            kegiatanFile.setPathFile(filePath);
            
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            kegiatanFile.setViewUrl("/file/view?fileName=" + encodedPath);
            kegiatanFile.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kegiatanFile.setContentType(file.getContentType());
            kegiatanFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanBastZips().add(kegiatanFile);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanBastZips().removeIf(zip -> zip.getId().equals(idFile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(kegiatan);
    }

    @Override
    @Transactional
    public Kegiatan deletePerijinanPdf(Long id, List<String> uuidPerijinanPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanPerijinanPdfs().removeIf(file -> {
            if (uuidPerijinanPdf.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this file from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            }
            return false; // Keep this file in the list
        });
        
        return repository.save(kegiatan);
    }


    @Override
    @Transactional
    public Kegiatan deletePakPdfShp(Long id, List<String> uuidPakPdfShp) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanBastZips().removeIf(file -> {
            if (uuidPakPdfShp.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this file from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            }
            return false; // Keep this file in the list
        });
        
        return repository.save(kegiatan);
    }



    @Override
    @Transactional
    public Kegiatan deleteRantekPdf(Long id, List<String> uuidRantekPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        kegiatan.getKegiatanRantekPdfs().removeIf(file -> {
            if (uuidRantekPdf.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this file from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            }
            return false; // Keep this file in the list
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
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this file from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            }
            return false; // Keep this file in the list
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
    public KegiatanPageDTO findByFiltersWithCache(String namaKegiatan, String peruntukan, List<String> tahunList,
            Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<Kegiatan> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaKegiatan if provided
        if (namaKegiatan != null && !namaKegiatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaKegiatan")),
                    "%" + namaKegiatan.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for peruntukan if provided
        if (peruntukan != null && !peruntukan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("peruntukan")),
                    "%" + peruntukan.toLowerCase() + "%"));
        }

        // Add filter for tahun list if provided
        if (tahunList != null && !tahunList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("tahun").in(tahunList));
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
        if (peruntukan != null && !peruntukan.isEmpty()) {
            builder.queryParam("peruntukan", peruntukan);
        }
        if (tahunList != null && !tahunList.isEmpty()) {
            for (String tahun : tahunList) {
                builder.queryParam("tahunList", tahun);
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
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("peruntukan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("tahun")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("catatan")), searchPattern)));
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
}