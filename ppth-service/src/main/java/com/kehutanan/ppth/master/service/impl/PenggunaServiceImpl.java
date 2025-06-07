package com.kehutanan.ppth.master.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.ppth.common.service.MinioStorageService;
import com.kehutanan.ppth.master.model.Pengguna;
import com.kehutanan.ppth.master.model.PenggunaFoto;
import com.kehutanan.ppth.master.repository.PenggunaRepository;
import com.kehutanan.ppth.master.service.PenggunaService;
import com.kehutanan.ppth.util.FileValidationUtil;

import jakarta.persistence.EntityNotFoundException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PenggunaServiceImpl implements PenggunaService {
    private final PenggunaRepository repository;
    private final FileValidationUtil fileValidationUtil;
    private final MinioStorageService minioStorageService;
    private String FOLDER_PREFIX = "Master/Pengguna/";

    @Autowired
    public PenggunaServiceImpl(PenggunaRepository repository, FileValidationUtil fileValidationUtil,
            MinioStorageService minioStorageService) {
        this.repository = repository;
        this.fileValidationUtil = fileValidationUtil;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public Page<Pengguna> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Pengguna> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "penggunaCache", key = "#id")
    public Pengguna findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pengguna not found with id: " + id));
    }

    @Override
    public Pengguna save(Pengguna pengguna) {
        return repository.save(pengguna);
    }

    @Override
    @CachePut(value = "penggunaCache", key = "#id")
    public Pengguna update(Long id, Pengguna pengguna) {
        return repository.save(pengguna);
    }

    @Override
    @CacheEvict(value = "penggunaCache", allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        // Find the entity first to get all associated files
        Pengguna pengguna = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pengguna not found with id: " + id));
        
        // Delete all profile photos from MinIO storage
        if (pengguna.getPenggunaFotoList() != null) {
            for (PenggunaFoto foto : pengguna.getPenggunaFotoList()) {
                try {
                    minioStorageService.deleteFile("", foto.getPathFile());
                } catch (Exception e) {
                    // Log error but continue deleting other files
                    System.err.println("Failed to delete photo file: " + foto.getPathFile() + " - " + e.getMessage());
                }
            }
        }
        
        // Finally delete the entity from the database
        repository.deleteById(id);
    }

    @Override
    public Page<Pengguna> findByFilters(String username, String namaLengkap, String email, Long peranId, Long statusId,
            Pageable pageable) {
        Specification<Pengguna> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for username if provided
        if (username != null && !username.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")),
                    "%" + username.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for namaLengkap if provided
        if (namaLengkap != null && !namaLengkap.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaLengkap")),
                    "%" + namaLengkap.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for email if provided
        if (email != null && !email.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%"));
        }

        // Add equals filter for peranId if provided
        if (peranId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("peran").get("id"), peranId));
        }

        // Add equals filter for statusId if provided
        if (statusId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("status").get("id"), statusId));
        }

        return repository.findAll(spec, pageable);
    }

    @Override
    @CacheEvict(value = "penggunaCache", allEntries = true, beforeInvocation = true)
    public Pengguna uploadPenggunaFoto(Long id, List<MultipartFile> files) {
        Pengguna pengguna = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            UUID idfile = UUID.randomUUID();

            String fileName = idfile + "_" + file.getOriginalFilename();
            String folderName = FOLDER_PREFIX + pengguna.getId() + "/foto/";
            String filePath = folderName + fileName;

            PenggunaFoto penggunaFoto = new PenggunaFoto();
            penggunaFoto.setId(idfile);
            penggunaFoto.setPengguna(pengguna);
            penggunaFoto.setNamaAsli(file.getOriginalFilename());
            penggunaFoto.setNamaFile(fileName);
            penggunaFoto.setPathFile(filePath);
            String encodedPath;
            try {
                 encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("Nama File tidak sesuai: " + e.getMessage(), e);
            }

            penggunaFoto.setViewUrl("/file/view?fileName=" + encodedPath);
            penggunaFoto.setDownloadUrl("/file/download?fileName=" + encodedPath);
            penggunaFoto.setContentType(file.getContentType());
            penggunaFoto.setUkuranMb((double) file.getSize() / (1024 * 1024));
            penggunaFoto.setContentType(file.getContentType());
            penggunaFoto.setUploadedAt(LocalDateTime.now());

            pengguna.getPenggunaFotoList().add(penggunaFoto);

            try {
                minioStorageService.uploadFile(folderName, fileName, file.getInputStream(), file.getContentType());
            } catch (Exception e) {
                pengguna.getPenggunaFotoList().removeIf(foto -> foto.getId().equals(idfile));
                throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
            }
        }

        return repository.save(pengguna);
    }

    @Override
    @Transactional
    @CacheEvict(value = "penggunaCache", allEntries = true, beforeInvocation = true)
    public Pengguna deletePenggunaFoto(Long id, List<String> uuidFotoProfiles) {
        Pengguna pengguna = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pengguna not found with id: " + id));
           
        pengguna.getPenggunaFotoList().removeIf(file->{
            System.out.println("Checking photo: " + file.getId());
            if(uuidFotoProfiles.contains(file.getId().toString())) {
                System.out.println("Photo with UUID " + file.getId() + " will be deleted.");
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
        return repository.save(pengguna);
    }
}