package com.kehutanan.rm.bimtek.service.impl;

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

import com.kehutanan.rm.bimtek.dto.BimtekPageDTO;
import com.kehutanan.rm.bimtek.model.Bimtek;
import com.kehutanan.rm.bimtek.model.BimtekFoto;
import com.kehutanan.rm.bimtek.model.BimtekPdf;
import com.kehutanan.rm.bimtek.model.BimtekVideo;
import com.kehutanan.rm.bimtek.model.dto.BimtekDTO;
import com.kehutanan.rm.bimtek.repository.BimtekRepository;
import com.kehutanan.rm.bimtek.service.BimtekService;
import com.kehutanan.rm.common.service.MinioStorageService;
import com.kehutanan.rm.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BimtekServiceImpl implements BimtekService {
    private final BimtekRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Bimtek/";

    @Autowired
    public BimtekServiceImpl(BimtekRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public List<Bimtek> findAll() {
        return repository.findAll();
    }

    @Override
    public BimtekDTO findDTOById(Long id) {
        Bimtek bimtek = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));

        BimtekDTO bimtekDTO = new BimtekDTO(bimtek);
        return bimtekDTO;
    }

    @Override
    public Bimtek findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));
    }

    @Override
    public Bimtek save(Bimtek bimtek) {
        return repository.save(bimtek);
    }

    @Override
    public Bimtek update(Long id, Bimtek bimtek) {
        return repository.save(bimtek);
    }

    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Bimtek bimtek = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));

        // Delete all PDF files from MinIO storage
        if (bimtek.getBimtekPdfs() != null) {
            for (BimtekPdf pdf : bimtek.getBimtekPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all photo files from MinIO storage
        if (bimtek.getBimtekFotos() != null) {
            for (BimtekFoto foto : bimtek.getBimtekFotos()) {
                try {
                    minioStorageService.deleteFile("", foto.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete photo file: " + foto.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Delete all video files from MinIO storage
        if (bimtek.getBimtekVideos() != null) {
            for (BimtekVideo video : bimtek.getBimtekVideos()) {
                try {
                    minioStorageService.deleteFile("", video.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete video file: " + video.getPathFile() + " - " + e.getMessage());
                }
            }
        }

        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    @Override
    public Bimtek uploadBimtekFoto(Long id, List<MultipartFile> files) {
        Bimtek bimtek = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + bimtek.getId() + "/foto/";
            String filePath = folderName + fileName;

            BimtekFoto bimtekFoto = new BimtekFoto();
            bimtekFoto.setId(idfile);
            bimtekFoto.setBimtek(bimtek);
            bimtekFoto.setNamaAsli(file.getOriginalFilename());
            bimtekFoto.setNamaFile(fileName);
            bimtekFoto.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            bimtekFoto.setViewUrl("/file/view?fileName=" + encodedPath);
            bimtekFoto.setDownloadUrl("/file/download?fileName=" + encodedPath);
            bimtekFoto.setContentType(file.getContentType());
            bimtekFoto.setUkuranMb((double) file.getSize() / (1024 * 1024));
            bimtekFoto.setUploadedAt(LocalDateTime.now());

            bimtek.getBimtekFotos().add(bimtekFoto);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                bimtek.getBimtekFotos().removeIf(foto -> foto.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(bimtek);
    }

    @Override
    public Bimtek uploadBimtekPdf(Long id, List<MultipartFile> files) {
        Bimtek bimtek = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + bimtek.getId() + "/pdf/";
            String filePath = folderName + fileName;

            BimtekPdf bimtekPdf = new BimtekPdf();
            bimtekPdf.setId(idfile);
            bimtekPdf.setBimtek(bimtek);
            bimtekPdf.setNamaAsli(file.getOriginalFilename());
            bimtekPdf.setNamaFile(fileName);
            bimtekPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            bimtekPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            bimtekPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            bimtekPdf.setContentType(file.getContentType());
            bimtekPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            bimtekPdf.setUploadedAt(LocalDateTime.now());

            bimtek.getBimtekPdfs().add(bimtekPdf);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                bimtek.getBimtekPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(bimtek);
    }

    @Override
    public Bimtek uploadBimtekVideo(Long id, List<MultipartFile> files) {
        Bimtek bimtek = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "video");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + bimtek.getId() + "/video/";
            String filePath = folderName + fileName;

            BimtekVideo bimtekVideo = new BimtekVideo();
            bimtekVideo.setId(idfile);
            bimtekVideo.setBimtek(bimtek);
            bimtekVideo.setNamaAsli(file.getOriginalFilename());
            bimtekVideo.setNamaFile(fileName);
            bimtekVideo.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            bimtekVideo.setViewUrl("/file/view?fileName=" + encodedPath);
            bimtekVideo.setDownloadUrl("/file/download?fileName=" + encodedPath);
            bimtekVideo.setContentType(file.getContentType());
            bimtekVideo.setUkuranMb((double) file.getSize() / (1024 * 1024));
            bimtekVideo.setUploadedAt(LocalDateTime.now());

            bimtek.getBimtekVideos().add(bimtekVideo);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                bimtek.getBimtekVideos().removeIf(video -> video.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(bimtek);
    }

    @Override
    @Transactional
    public Bimtek deleteBimtekFoto(Long id, List<String> uuidFoto) {
        Bimtek bimtek = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));

        bimtek.getBimtekFotos().removeIf(file -> {
            if (uuidFoto.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this photo from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this photo in the list
            }
        });
        return repository.save(bimtek);
    }

    @Override
    @Transactional
    public Bimtek deleteBimtekPdf(Long id, List<String> uuidPdf) {
        Bimtek bimtek = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));

        bimtek.getBimtekPdfs().removeIf(file -> {
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
        return repository.save(bimtek);
    }

    @Override
    @Transactional
    public Bimtek deleteBimtekVideo(Long id, List<String> uuidVideo) {
        Bimtek bimtek = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));

        bimtek.getBimtekVideos().removeIf(file -> {
            if (uuidVideo.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this video from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this video in the list
            }
        });
        return repository.save(bimtek);
    }

    @Override
    public BimtekPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Bimtek> page = repository.findAll(pageable);

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

        return new BimtekPageDTO(page, pageMetadata, links);
    }

    @Override
    public BimtekPageDTO findByFiltersWithCache(String namaBimtek, List<String> bpdasList,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<Bimtek> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaBimtek if provided
        if (namaBimtek != null && !namaBimtek.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaBimtek")),
                    "%" + namaBimtek.toLowerCase() + "%"));
        }

        if (bpdasList != null && !bpdasList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("bpdasId").get("id").in(bpdasList));
        }

        // Execute query with filters
        Page<Bimtek> page = repository.findAll(spec, pageable);

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
        if (namaBimtek != null && !namaBimtek.isEmpty()) {
            builder.queryParam("namaBimtek", namaBimtek);
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

        return new BimtekPageDTO(page, pageMetadata, links);
    }

    @Override
    public BimtekPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Bimtek> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("namaBimtek")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("subjek")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("tempat")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("audience")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("evaluasi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("catatan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("bpdasId").get("namaBpdas")), searchPattern)));
        }

        Page<Bimtek> page = repository.findAll(spec, pageable);

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

        return new BimtekPageDTO(page, pageMetadata, links);
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