package com.kehutanan.tktrh.util;

import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.config.FileUploadConfig;
import com.kehutanan.tktrh.exception.FileSizeLimitExceededException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileValidationUtil {

    private final FileUploadConfig fileUploadConfig;

    private static final List<String> IMAGE_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/jpg");

    private static final List<String> PDF_CONTENT_TYPES = Arrays.asList(
            "application/pdf");

    private static final List<String> VIDEO_CONTENT_TYPES = Arrays.asList(
            "video/mp4", "video/mpeg", "video/quicktime", "video/x-msvideo");

    private static final List<String> SHP_CONTENT_TYPES = Arrays.asList(
            "application/x-esri-shape", "application/shapefile", "application/x-shapefile",
            "application/octet-stream");

    private static final List<String> ZIP_CONTENT_TYPES = Arrays.asList(
            "application/zip", "application/x-zip-compressed", "multipart/x-zip",
            "application/x-compressed");

    public void validateFileType(MultipartFile file, String type) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        
        if (contentType == null) {
            throw new FileSizeLimitExceededException("Tipe konten file tidak dikenali");
        }
        System.out.println("File content type: " + contentType);
        
        if (type.equalsIgnoreCase("image") && !isImageFile(contentType)) {
            throw new FileSizeLimitExceededException("File bukan gambar");
        } else if (type.equalsIgnoreCase("pdf") && !isPdfFile(contentType)) {
            throw new FileSizeLimitExceededException("File bukan PDF");
        } else if (type.equalsIgnoreCase("video") && !isVideoFile(contentType)) {
            throw new FileSizeLimitExceededException("File bukan video");
        } else if (type.equalsIgnoreCase("shp") && !isShpFile(contentType, filename)) {
            throw new FileSizeLimitExceededException("File bukan Shapefile");
        } else if (type.equalsIgnoreCase("zip") && !isZipFile(contentType, filename)) {
            throw new FileSizeLimitExceededException("File bukan ZIP");
        }
    }

    public void validateFileSize(MultipartFile file) {
        String contentType = file.getContentType();
        double fileSizeMB = file.getSize() / (1024.0 * 1024.0);
        
        if (contentType == null) {
            throw new FileSizeLimitExceededException("Tipe konten file tidak dikenali");
        }
        System.out.println("File content type: " + contentType);    
        if (isImageFile(contentType)) {
            if (fileSizeMB > fileUploadConfig.getMaxSize().getImage()) {
                throw new FileSizeLimitExceededException(
                    String.format("Ukuran gambar melebihi batas maksimum %d MB", 
                        fileUploadConfig.getMaxSize().getImage()));
            }
        } else if (isPdfFile(contentType)) {
            if (fileSizeMB > fileUploadConfig.getMaxSize().getPdf()) {
                throw new FileSizeLimitExceededException(
                    String.format("Ukuran PDF melebihi batas maksimum %d MB", 
                        fileUploadConfig.getMaxSize().getPdf()));
            }
        } else if (isVideoFile(contentType)) {
            if (fileSizeMB > fileUploadConfig.getMaxSize().getVideo()) {
                throw new FileSizeLimitExceededException(
                    String.format("Ukuran video melebihi batas maksimum %d MB", 
                        fileUploadConfig.getMaxSize().getVideo()));
            }
        } else if (isShpFile(contentType)) {
            if (fileSizeMB > fileUploadConfig.getMaxSize().getShp()) {
                throw new FileSizeLimitExceededException(
                    String.format("Ukuran Shapefile melebihi batas maksimum %d MB", 
                        fileUploadConfig.getMaxSize().getShp()));
            }
        } else if (isZipFile(contentType)) {
            if (fileSizeMB > fileUploadConfig.getMaxSize().getZip()) {
                throw new FileSizeLimitExceededException(
                    String.format("Ukuran ZIP melebihi batas maksimum %d MB", 
                        fileUploadConfig.getMaxSize().getZip()));
            }
        } else {
            log.warn("File dengan content type {} tidak divalidasi ukurannya", contentType);
        }
    }

    public boolean isImageFile(String contentType) {
        return IMAGE_CONTENT_TYPES.contains(contentType);
    }

    public boolean isPdfFile(String contentType) {
        return PDF_CONTENT_TYPES.contains(contentType);
    }

    public boolean isVideoFile(String contentType) {
        return VIDEO_CONTENT_TYPES.contains(contentType);
    }

    public boolean isShpFile(String contentType) {
        return SHP_CONTENT_TYPES.contains(contentType);
    }

    public boolean isShpFile(String contentType, String filename) {
        if (filename != null && filename.toLowerCase().endsWith(".shp")) {
            return true;
        }
        return SHP_CONTENT_TYPES.contains(contentType);
    }

    public boolean isZipFile(String contentType) {
        return ZIP_CONTENT_TYPES.contains(contentType);
    }

    public boolean isZipFile(String contentType, String filename) {
        if (filename != null && filename.toLowerCase().endsWith(".zip")) {
            return true;
        }
        return ZIP_CONTENT_TYPES.contains(contentType);
    }
}