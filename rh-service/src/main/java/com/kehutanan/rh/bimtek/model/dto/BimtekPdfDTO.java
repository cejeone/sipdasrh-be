package com.kehutanan.rh.bimtek.model.dto;

import java.io.Serializable;
import java.util.UUID;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekPdfDTO implements Serializable {

    private UUID id;
    private Long bimtekId;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public BimtekPdfDTO(BimtekPdf bimtekPdf) {
        this.id = bimtekPdf.getId();
        if (bimtekPdf.getBimtek() != null) {
            this.bimtekId = bimtekPdf.getBimtek().getId();
        }
        this.namaFile = bimtekPdf.getNamaFile();
        this.namaAsli = bimtekPdf.getNamaAsli();
        this.pathFile = bimtekPdf.getPathFile();
        this.ukuranMb = bimtekPdf.getUkuranMb();
        this.contentType = bimtekPdf.getContentType();
        if (bimtekPdf.getUploadedAt() != null) {
            this.uploadedAt = bimtekPdf.getUploadedAt().toString();
        }
        this.viewUrl = bimtekPdf.getViewUrl();
        this.downloadUrl = bimtekPdf.getDownloadUrl();
    }
}