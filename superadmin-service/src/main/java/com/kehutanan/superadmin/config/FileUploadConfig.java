package com.kehutanan.superadmin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "file.upload")
@Data
public class FileUploadConfig {
    
    private MaxSize maxSize = new MaxSize();
    
    @Data
    public static class MaxSize {
        private int pdf = 100;    // default 10MB
        private int image = 50;   // default 50MB
        private int video = 500;  // default 500MB
        private int shp = 50;    // default 50MB
    }
}