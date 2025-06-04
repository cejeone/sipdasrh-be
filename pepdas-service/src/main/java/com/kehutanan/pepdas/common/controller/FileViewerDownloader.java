package com.kehutanan.pepdas.common.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;

import com.kehutanan.pepdas.common.service.MinioStorageService;

@RestController
@RequestMapping("/file")
public class FileViewerDownloader {
    private final MinioStorageService minioStorageService;

    public FileViewerDownloader(MinioStorageService minioStorageService) {
        this.minioStorageService = minioStorageService;
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(
            @RequestParam(required = true) String fileName) throws Exception {
        System.out.println("Downloading file: " + fileName);
        // Get the file data from MinIO
        byte[] data = minioStorageService.getFileData("", fileName);

        // Extract the original filename from the path
        String originalFilename = extractFilename(fileName);

        // Set up the response headers
        HttpHeaders headers = new HttpHeaders();

        // Determine content type based on file extension
        MediaType mediaType = determineMediaType(originalFilename);
        headers.setContentType(mediaType);

        // Set content disposition for download
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(originalFilename)
                .build());

        headers.setContentLength(data.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }

    @GetMapping("/view")
    public ResponseEntity<?> viewFile(
            @RequestParam(required = true) String fileName) throws Exception {
        System.out.println("Viewing file: " + fileName);
        
        // Extract the original filename from the path
        String originalFilename = extractFilename(fileName);
        
        // Determine content type based on file extension
        MediaType mediaType = determineMediaType(originalFilename);
        
        // Special handling for video files to enable streaming
        if (isVideoFile(originalFilename)) {
            // Get file stream instead of loading entire file into memory
            InputStream videoStream = minioStorageService.getFileStream("", fileName);
            
            // Set up the response headers for video streaming
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            
            // Set content disposition to inline for browser viewing
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename(originalFilename)
                    .build());
            
            // These headers help with video streaming
            headers.add("Accept-Ranges", "bytes");
            headers.setCacheControl("public, max-age=3600");
            
            // Return streamed response
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(videoStream));
        } 
        // For non-video files, load the data directly
        else {
            // Get the file data from MinIO
            byte[] data = minioStorageService.getFileData("", fileName);
            
            // Set up the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename(originalFilename)
                    .build());
            headers.setContentLength(data.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);
        }
    }

    private String extractFilename(String path) {
        if (path == null || path.isEmpty()) {
            return "unknown_file";
        }

        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            // No slash found, return the original string
            return path;
        } else {
            // Return everything after the last slash
            return path.substring(lastSlashIndex + 1);
        }
    }

    /**
     * Determines if the file is a video based on its extension
     */
    private boolean isVideoFile(String filename) {
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i + 1).toLowerCase();
        }

        return extension.equals("mp4") ||
                extension.equals("webm") ||
                extension.equals("ogg") ||
                extension.equals("mov");
    }

    /**
     * Determines the MediaType based on file extension
     */
    private MediaType determineMediaType(String filename) {
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i + 1).toLowerCase();
        }

        switch (extension) {
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "mp4":
                return new MediaType("video", "mp4");
            case "webm":
                return new MediaType("video", "webm");
            case "ogg":
                return new MediaType("video", "ogg");
            case "mov":
                return new MediaType("video", "quicktime");
            case "doc":
                return new MediaType("application", "msword");
            case "docx":
                return new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
            case "xls":
                return new MediaType("application", "vnd.ms-excel");
            case "xlsx":
                return new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
