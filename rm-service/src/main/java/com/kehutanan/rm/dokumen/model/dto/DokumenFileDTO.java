package com.kehutanan.rm.dokumen.model.dto;

import java.util.UUID;
import com.kehutanan.rm.dokumen.model.DokumenFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DokumenFileDTO {
    private UUID id;
    private String namaAsli;
    private String namaFile;
    private String pathFile;
    private String viewUrl;
    private String downloadUrl;
    private String contentType;
    private Double ukuranMb;
    private String uploadedAt;
    
    // Constructor that takes DokumenFile entity
    public DokumenFileDTO(DokumenFile dokumenFile) {
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
        // We don't include the dokumen reference here to avoid circular dependency
    }
}
