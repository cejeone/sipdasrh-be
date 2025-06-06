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

import com.kehutanan.tktrh.common.service.MinioStorageService;
import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanRancanganTeknisShp;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.tktrh.bkta.kegiatan.repository.KegiatanRepository;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanServiceImpl implements KegiatanService {
    
    private final KegiatanRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "BKTA/Kegiatan/";
    
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
        return repository.save(kegiatan);
    }
    
    @Override
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        // Delete all Rancangan Teknis PDF files from MinIO storage
        if (kegiatan.getKegiatanRancanganTeknisPdfs() != null) {
            for (KegiatanRancanganTeknisPdf pdf : kegiatan.getKegiatanRancanganTeknisPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }
        
        // Delete all Rancangan Teknis Foto files from MinIO storage
        if (kegiatan.getKegiatanRancanganTeknisFotos() != null) {
            for (KegiatanRancanganTeknisFoto foto : kegiatan.getKegiatanRancanganTeknisFotos()) {
                try {
                    minioStorageService.deleteFile("", foto.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete photo file: " + foto.getPathFile() + " - " + e.getMessage());
                }
            }
        }
        
        // Delete all Rancangan Teknis SHP files from MinIO storage
        if (kegiatan.getKegiatanRancanganTeknisShps() != null) {
            for (KegiatanRancanganTeknisShp shp : kegiatan.getKegiatanRancanganTeknisShps()) {
                try {
                    minioStorageService.deleteFile("", shp.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete SHP file: " + shp.getPathFile() + " - " + e.getMessage());
                }
            }
        }
        
        // Delete all Kontrak PDF files from MinIO storage
        if (kegiatan.getKegiatanKontrakPdfs() != null) {
            for (KegiatanKontrakPdf pdf : kegiatan.getKegiatanKontrakPdfs()) {
                try {
                    minioStorageService.deleteFile("", pdf.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete PDF file: " + pdf.getPathFile() + " - " + e.getMessage());
                }
            }
        }
        
        // Delete all Dokumentasi Foto files from MinIO storage
        if (kegiatan.getDokumentasiFotos() != null) {
            for (KegiatanDokumentasiFoto foto : kegiatan.getDokumentasiFotos()) {
                try {
                    minioStorageService.deleteFile("", foto.getPathFile());
                } catch (Exception e) {
                    System.err.println("Failed to delete photo file: " + foto.getPathFile() + " - " + e.getMessage());
                }
            }
        }
        
        // Delete all Dokumentasi Video files from MinIO storage
        if (kegiatan.getDokumentasiVideos() != null) {
            for (KegiatanDokumentasiVideo video : kegiatan.getDokumentasiVideos()) {
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
    public Kegiatan uploadRancanganTeknisPdf(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();
            
            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/rancangan-teknis/pdf/";
            String filePath = folderName + fileName;
            
            KegiatanRancanganTeknisPdf rancanganPdf = new KegiatanRancanganTeknisPdf();
            rancanganPdf.setId(idfile);
            rancanganPdf.setKegiatan(kegiatan);
            rancanganPdf.setNamaAsli(file.getOriginalFilename());
            rancanganPdf.setNamaFile(fileName);
            rancanganPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }
            
            rancanganPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            rancanganPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            rancanganPdf.setContentType(file.getContentType());
            rancanganPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            rancanganPdf.setUploadedAt(LocalDateTime.now());
            
            kegiatan.getKegiatanRancanganTeknisPdfs().add(rancanganPdf);
            
            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanRancanganTeknisPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }
        
        return repository.save(kegiatan);
    }
    
    @Override
    public Kegiatan uploadRancanganTeknisFoto(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();
            
            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/rancangan-teknis/foto/";
            String filePath = folderName + fileName;
            
            KegiatanRancanganTeknisFoto rancanganFoto = new KegiatanRancanganTeknisFoto();
            rancanganFoto.setId(idfile);
            rancanganFoto.setKegiatan(kegiatan);
            rancanganFoto.setNamaAsli(file.getOriginalFilename());
            rancanganFoto.setNamaFile(fileName);
            rancanganFoto.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }
            
            rancanganFoto.setViewUrl("/file/view?fileName=" + encodedPath);
            rancanganFoto.setDownloadUrl("/file/download?fileName=" + encodedPath);
            rancanganFoto.setContentType(file.getContentType());
            rancanganFoto.setUkuranMb((double) file.getSize() / (1024 * 1024));
            rancanganFoto.setUploadedAt(LocalDateTime.now());
            
            kegiatan.getKegiatanRancanganTeknisFotos().add(rancanganFoto);
            
            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanRancanganTeknisFotos().removeIf(foto -> foto.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }
        
        return repository.save(kegiatan);
    }
    
    @Override
    public Kegiatan uploadRancanganTeknisShp(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "shp");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();
            
            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/rancangan-teknis/shp/";
            String filePath = folderName + fileName;
            
            KegiatanRancanganTeknisShp rancanganShp = new KegiatanRancanganTeknisShp();
            rancanganShp.setId(idfile);
            rancanganShp.setKegiatan(kegiatan);
            rancanganShp.setNamaAsli(file.getOriginalFilename());
            rancanganShp.setNamaFile(fileName);
            rancanganShp.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }
            
            rancanganShp.setViewUrl("/file/view?fileName=" + encodedPath);
            rancanganShp.setDownloadUrl("/file/download?fileName=" + encodedPath);
            rancanganShp.setContentType(file.getContentType());
            rancanganShp.setUkuranMb((double) file.getSize() / (1024 * 1024));
            rancanganShp.setUploadedAt(LocalDateTime.now());
            
            kegiatan.getKegiatanRancanganTeknisShps().add(rancanganShp);
            
            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanRancanganTeknisShps().removeIf(shp -> shp.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }
        
        return repository.save(kegiatan);
    }
    
    @Override
    public Kegiatan uploadKontrakPdf(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();
            
            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/kontrak/pdf/";
            String filePath = folderName + fileName;
            
            KegiatanKontrakPdf kontrakPdf = new KegiatanKontrakPdf();
            kontrakPdf.setId(idfile);
            kontrakPdf.setKegiatan(kegiatan);
            kontrakPdf.setNamaAsli(file.getOriginalFilename());
            kontrakPdf.setNamaFile(fileName);
            kontrakPdf.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }
            
            kontrakPdf.setViewUrl("/file/view?fileName=" + encodedPath);
            kontrakPdf.setDownloadUrl("/file/download?fileName=" + encodedPath);
            kontrakPdf.setContentType(file.getContentType());
            kontrakPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kontrakPdf.setUploadedAt(LocalDateTime.now());
            
            kegiatan.getKegiatanKontrakPdfs().add(kontrakPdf);
            
            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getKegiatanKontrakPdfs().removeIf(pdf -> pdf.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }
        
        return repository.save(kegiatan);
    }
    
    @Override
    public Kegiatan uploadDokumentasiFoto(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();
            
            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/dokumentasi/foto/";
            String filePath = folderName + fileName;
            
            KegiatanDokumentasiFoto dokumentasiFoto = new KegiatanDokumentasiFoto();
            dokumentasiFoto.setId(idfile);
            dokumentasiFoto.setKegiatan(kegiatan);
            dokumentasiFoto.setNamaAsli(file.getOriginalFilename());
            dokumentasiFoto.setNamaFile(fileName);
            dokumentasiFoto.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }
            
            dokumentasiFoto.setViewUrl("/file/view?fileName=" + encodedPath);
            dokumentasiFoto.setDownloadUrl("/file/download?fileName=" + encodedPath);
            dokumentasiFoto.setContentType(file.getContentType());
            dokumentasiFoto.setUkuranMb((double) file.getSize() / (1024 * 1024));
            dokumentasiFoto.setUploadedAt(LocalDateTime.now());
            
            kegiatan.getDokumentasiFotos().add(dokumentasiFoto);
            
            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getDokumentasiFotos().removeIf(foto -> foto.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }
        
        return repository.save(kegiatan);
    }
    
    @Override
    public Kegiatan uploadDokumentasiVideo(Long id, List<MultipartFile> files) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "video");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();
            
            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + kegiatan.getId() + "/dokumentasi/video/";
            String filePath = folderName + fileName;
            
            KegiatanDokumentasiVideo dokumentasiVideo = new KegiatanDokumentasiVideo();
            dokumentasiVideo.setId(idfile);
            dokumentasiVideo.setKegiatan(kegiatan);
            dokumentasiVideo.setNamaAsli(file.getOriginalFilename());
            dokumentasiVideo.setNamaFile(fileName);
            dokumentasiVideo.setPathFile(filePath);
            String encodedPath;
            try {
                encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }
            
            dokumentasiVideo.setViewUrl("/file/view?fileName=" + encodedPath);
            dokumentasiVideo.setDownloadUrl("/file/download?fileName=" + encodedPath);
            dokumentasiVideo.setContentType(file.getContentType());
            dokumentasiVideo.setUkuranMb((double) file.getSize() / (1024 * 1024));
            dokumentasiVideo.setUploadedAt(LocalDateTime.now());
            
            kegiatan.getDokumentasiVideos().add(dokumentasiVideo);
            
            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                kegiatan.getDokumentasiVideos().removeIf(video -> video.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }
        
        return repository.save(kegiatan);
    }
    
    @Override
    @Transactional
    public Kegiatan deleteRancanganTeknisPdf(Long id, List<String> uuidPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        kegiatan.getKegiatanRancanganTeknisPdfs().removeIf(file -> {
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
        return repository.save(kegiatan);
    }
    
    @Override
    @Transactional
    public Kegiatan deleteRancanganTeknisFoto(Long id, List<String> uuidFoto) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        kegiatan.getKegiatanRancanganTeknisFotos().removeIf(file -> {
            if (uuidFoto.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this foto from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this foto in the list
            }
        });
        return repository.save(kegiatan);
    }
    
    @Override
    @Transactional
    public Kegiatan deleteRancanganTeknisShp(Long id, List<String> uuidShp) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        kegiatan.getKegiatanRancanganTeknisShps().removeIf(file -> {
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
        return repository.save(kegiatan);
    }
    
    @Override
    @Transactional
    public Kegiatan deleteKontrakPdf(Long id, List<String> uuidPdf) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        kegiatan.getKegiatanKontrakPdfs().removeIf(file -> {
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
        return repository.save(kegiatan);
    }
    
    @Override
    @Transactional
    public Kegiatan deleteDokumentasiFoto(Long id, List<String> uuidFoto) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        kegiatan.getDokumentasiFotos().removeIf(file -> {
            if (uuidFoto.contains(file.getId().toString())) {
                try {
                    // Delete file from MinIO storage
                    minioStorageService.deleteFile("", file.getPathFile());
                    return true; // Remove this foto from the list
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
                }
            } else {
                return false; // Keep this foto in the list
            }
        });
        return repository.save(kegiatan);
    }
    
    @Override
    @Transactional
    public Kegiatan deleteDokumentasiVideo(Long id, List<String> uuidVideo) {
        Kegiatan kegiatan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));
                
        kegiatan.getDokumentasiVideos().removeIf(file -> {
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
    public KegiatanPageDTO findByFiltersWithCache(String namaProgram, String namaKegiatan, List<String> programList,
            Pageable pageable, String baseUrl) {
            
        // Create specification based on filters
        Specification<Kegiatan> spec = Specification.where(null);
        
        // Add case-insensitive LIKE filter for namaProgram if provided
        if (namaProgram != null && !namaProgram.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("program").get("namaProgram")),
                    "%" + namaProgram.toLowerCase() + "%"));
        }
        
        // Add case-insensitive LIKE filter for namaKegiatan if provided
        if (namaKegiatan != null && !namaKegiatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaKegiatan")),
                    "%" + namaKegiatan.toLowerCase() + "%"));
        }
        
        // Add filter for programList if provided
        if (programList != null && !programList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("program").get("id").in(programList));
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
        if (namaProgram != null && !namaProgram.isEmpty()) {
            builder.queryParam("namaProgram", namaProgram);
        }
        if (namaKegiatan != null && !namaKegiatan.isEmpty()) {
            builder.queryParam("namaKegiatan", namaKegiatan);
        }
        if (programList != null && !programList.isEmpty()) {
            for (String programId : programList) {
                builder.queryParam("programList", programId);
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
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lokasi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("program").get("namaProgram")), searchPattern)));
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