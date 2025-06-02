package com.kehutanan.pepdas.serahterima.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.pepdas.serahterima.model.BastPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BastFileDTO implements Serializable {
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
    
    public BastFileDTO(BastPdf pdf) {
        this.id = pdf.getId(); // Assuming BastPdf has UUID id
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
}