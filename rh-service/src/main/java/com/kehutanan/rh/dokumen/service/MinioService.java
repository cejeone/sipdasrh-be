package com.kehutanan.rh.dokumen.service;

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
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName;
    private static final String FOLDER_PREFIX = "Dokumen/";

    public MinioService(
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
                BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (!bucketExists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("Bucket {} created successfully", bucketName);
            }
        } catch (Exception e) {
            log.error("Error initializing MinIO bucket: {}", e.getMessage(), e);
            throw new RuntimeException("Could not initialize MinIO bucket", e);
        }
    }

    public void uploadFile(String fileName, InputStream inputStream, String contentType) throws Exception {
        try {
            String objectName = FOLDER_PREFIX + fileName;
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build()
            );
            log.info("File {} uploaded successfully to {}", fileName, objectName);
        } catch (Exception e) {
            log.error("Error uploading file {}: {}", fileName, e.getMessage(), e);
            throw new Exception("Could not upload file to MinIO", e);
        }
    }

    public String getPresignedUrl(String fileName) throws Exception {
        try {
            String objectName = FOLDER_PREFIX + fileName;
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .method(Method.GET)
                    .expiry(24, TimeUnit.HOURS)
                    .build()
            );
        } catch (Exception e) {
            log.error("Error generating presigned URL for file {}: {}", fileName, e.getMessage(), e);
            throw new Exception("Could not generate presigned URL", e);
        }
    }

    /**
     * Gets the file data as byte array from MinIO
     * 
     * @param fileName The name of the file in MinIO storage
     * @return byte array containing file data
     * @throws Exception If there's an error retrieving the file
     */
    public byte[] getFileData(String fileName) throws Exception {
        String objectName = FOLDER_PREFIX + fileName;
        try {
            log.info("Retrieving file from MinIO: {}", objectName);
            
            GetObjectResponse response = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)  // Make sure this matches how files are stored
                    .build()
            );
            
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

    public void deleteFile(String fileName) throws Exception {
        try {
            String objectName = FOLDER_PREFIX + fileName;
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            log.info("File {} deleted successfully", objectName);
        } catch (Exception e) {
            log.error("Error deleting file {}: {}", fileName, e.getMessage(), e);
            throw new Exception("Could not delete file from MinIO", e);
        }
    }

    public boolean fileExists(String fileName) throws Exception {
        try {
            String objectName = FOLDER_PREFIX + fileName;
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}