package com.kehutanan.rm.bimtek.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rm.bimtek.model.BimtekFoto;
import com.kehutanan.rm.bimtek.model.BimtekPdf;
import com.kehutanan.rm.bimtek.model.BimtekVideo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String namaAsli;
    private String namaFile;
    private String pathFile;
    private String viewUrl;
    private String downloadUrl;
    private String contentType;
    private Double ukuranMb;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String uploadedAt;
    
    public BimtekFileDTO(BimtekPdf pdf) {
        this.id = pdf.getId();
        this.namaAsli = pdf.getNamaAsli();
        this.namaFile = pdf.getNamaFile();
        this.pathFile = pdf.getPathFile();
        this.viewUrl = pdf.getViewUrl();
        this.downloadUrl = pdf.getDownloadUrl();
        this.contentType = pdf.getContentType();
        this.ukuranMb = pdf.getUkuranMb();
        if (pdf.getUploadedAt() != null) {
            this.uploadedAt = pdf.getUploadedAt().toString();
        }
    }
    
    public BimtekFileDTO(BimtekFoto foto) {
        this.id = foto.getId();
        this.namaAsli = foto.getNamaAsli();
        this.namaFile = foto.getNamaFile();
        this.pathFile = foto.getPathFile();
        this.viewUrl = foto.getViewUrl();
        this.downloadUrl = foto.getDownloadUrl();
        this.contentType = foto.getContentType();
        this.ukuranMb = foto.getUkuranMb();
        if (foto.getUploadedAt() != null) {
            this.uploadedAt = foto.getUploadedAt().toString();
        }
    }
    
    public BimtekFileDTO(BimtekVideo video) {
        this.id = video.getId();
        this.namaAsli = video.getNamaAsli();
        this.namaFile = video.getNamaFile();
        this.pathFile = video.getPathFile();
        this.viewUrl = video.getViewUrl();
        this.downloadUrl = video.getDownloadUrl();
        this.contentType = video.getContentType();
        this.ukuranMb = video.getUkuranMb();
        if (video.getUploadedAt() != null) {
            this.uploadedAt = video.getUploadedAt().toString();
        }
    }
}