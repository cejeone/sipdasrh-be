package com.kehutanan.pepdas.dokumen.model.dto;

import com.kehutanan.pepdas.dokumen.model.DokumenFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DokumenFileDto {
    
    private UUID id;
    private String namaAsli;
    private String namaFile;
    private String pathFile;
    private String viewUrl;
    private String downloadUrl;
    private String contentType;
    private Double ukuranMb;
    private String uploadedAt;
    
    public DokumenFileDto(DokumenFile dokumenFile) {
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