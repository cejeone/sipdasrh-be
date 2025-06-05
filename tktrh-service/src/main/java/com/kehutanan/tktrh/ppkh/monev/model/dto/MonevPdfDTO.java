package com.kehutanan.tktrh.ppkh.monev.model.dto;

import java.util.UUID;
import com.kehutanan.tktrh.ppkh.monev.model.MonevPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevPdfDTO {
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public MonevPdfDTO(MonevPdf monevPdf) {
        this.id = monevPdf.getId();
        this.namaFile = monevPdf.getNamaFile();
        this.namaAsli = monevPdf.getNamaAsli();
        this.pathFile = monevPdf.getPathFile();
        this.ukuranMb = monevPdf.getUkuranMb();
        this.contentType = monevPdf.getContentType();
        this.uploadedAt = monevPdf.getUploadedAt() != null ? monevPdf.getUploadedAt().toString() : null;
        this.viewUrl = monevPdf.getViewUrl();
        this.downloadUrl = monevPdf.getDownloadUrl();
    }
}
