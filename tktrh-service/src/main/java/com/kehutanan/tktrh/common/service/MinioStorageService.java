package com.kehutanan.tktrh.common.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MinioStorageService {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioStorageService(
            @Value("${minio.endpoint}") String endpoint,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${minio.bucket}") String bucketName) {

        this.bucketName = bucketName;
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        initializeBucket();
    }

    private void initializeBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());

            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket {} created successfully", bucketName);
            }
        } catch (Exception e) {
            log.error("Error initializing MinIO bucket: {}", e.getMessage(), e);
            throw new RuntimeException("Could not initialize MinIO bucket", e);
        }
    }

    public void uploadFile(String folderPrefix, String fileName, InputStream inputStream, String contentType) throws Exception {
        String objectName = buildObjectName(folderPrefix, fileName);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build());
            log.info("File {} uploaded successfully to {}", fileName, objectName);
        } catch (Exception e) {
            log.error("Error uploading file {}: {}", fileName, e.getMessage(), e);
            throw new Exception("Could not upload file to MinIO", e);
        }
    }

    public String getPresignedUrl(String folderPrefix, String fileName) throws Exception {
        String objectName = buildObjectName(folderPrefix, fileName);
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(24, TimeUnit.HOURS)
                            .build());
        } catch (Exception e) {
            log.error("Error generating presigned URL for file {}: {}", fileName, e.getMessage(), e);
            throw new Exception("Could not generate presigned URL", e);
        }
    }

    public byte[] getFileData(String folderPrefix, String fileName) throws Exception {
        String objectName = buildObjectName(folderPrefix, fileName);
        try {
            log.info("Retrieving file from MinIO: {}", objectName);

            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = response.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Error getting file data from MinIO: {}", fileName, e);
            throw new Exception("Failed to retrieve file from storage", e);
        }
    }

    public void deleteFile(String folderPrefix, String fileName) throws Exception {
        String objectName = buildObjectName(folderPrefix, fileName);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            log.info("File {} deleted successfully", objectName);
        } catch (Exception e) {
            log.error("Error deleting file {}: {}", fileName, e.getMessage(), e);
            throw new Exception("Could not delete file from MinIO", e);
        }
    }

    public boolean fileExists(String folderPrefix, String fileName) throws Exception {
        String objectName = buildObjectName(folderPrefix, fileName);
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public InputStream getFileStream(String folderPrefix, String fileName) throws Exception {
        String objectName = buildObjectName(folderPrefix, fileName);
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("Error getting file stream from MinIO: {}", e.getMessage());
            throw new RuntimeException("Could not get file stream from storage", e);
        }
    }

    private String buildObjectName(String folderPrefix, String fileName) {
        if (folderPrefix == null || folderPrefix.isEmpty()) {
            return fileName;
        }
        // Pastikan ada trailing slash untuk prefix
        return folderPrefix.endsWith("/") ? folderPrefix + fileName : folderPrefix + "/" + fileName;
    }
}