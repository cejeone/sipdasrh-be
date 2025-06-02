package com.kehutanan.pepdas.monev.dto;

import java.io.Serializable;
import java.util.UUID;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.pepdas.monev.model.MonevPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevFileDTO implements Serializable {
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
    private LocalDateTime uploadedAt;
    
    public MonevFileDTO(MonevPdf pdf) {
        this.id = pdf.getId();
        this.namaAsli = pdf.getNamaAsli();
        this.namaFile = pdf.getNamaFile();
        this.pathFile = pdf.getPathFile();
        this.viewUrl = pdf.getViewUrl();
        this.downloadUrl = pdf.getDownloadUrl();
        this.contentType = pdf.getContentType();
        this.ukuranMb = pdf.getUkuranMb();
        this.uploadedAt = pdf.getUploadedAt();
    }
}