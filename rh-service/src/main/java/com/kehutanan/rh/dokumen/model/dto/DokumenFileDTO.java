package com.kehutanan.rh.dokumen.model.dto;

import java.util.UUID;

import com.kehutanan.rh.dokumen.model.DokumenFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DokumenFileDTO {
    private UUID id;
    private String namaAsli;
    private String namaFile;
    private String pathFile;
    private String viewUrl;
    private String downloadUrl;
    private String contentType;
    private Double ukuranMb;
    private String uploadedAt; // Changed from LocalDateTime to String
    
    // Constructor from entity
    public DokumenFileDTO(DokumenFile dokumenFile) {
        if (dokumenFile != null) {
            this.id = dokumenFile.getId();
            this.namaAsli = dokumenFile.getNamaAsli();
            this.namaFile = dokumenFile.getNamaFile();
            this.pathFile = dokumenFile.getPathFile();
            this.viewUrl = dokumenFile.getViewUrl();
            this.downloadUrl = dokumenFile.getDownloadUrl();
            this.contentType = dokumenFile.getContentType();
            this.ukuranMb = dokumenFile.getUkuranMb();
            this.uploadedAt = dokumenFile.getUploadedAt() != null ? 
                dokumenFile.getUploadedAt().toString() : null;
        }
    }
}