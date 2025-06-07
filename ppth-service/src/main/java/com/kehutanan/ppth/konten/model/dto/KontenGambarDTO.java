package com.kehutanan.ppth.konten.model.dto;

import com.kehutanan.ppth.konten.model.KontenGambar;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
public class KontenGambarDTO {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public KontenGambarDTO(KontenGambar kontenGambar) {
        if (kontenGambar != null) {
            this.id = kontenGambar.getId();
            this.namaFile = kontenGambar.getNamaFile();
            this.namaAsli = kontenGambar.getNamaAsli();
            this.pathFile = kontenGambar.getPathFile();
            this.ukuranMb = kontenGambar.getUkuranMb();
            this.contentType = kontenGambar.getContentType();
            this.uploadedAt = kontenGambar.getUploadedAt() != null ? 
                              kontenGambar.getUploadedAt().toString() : null;
            this.viewUrl = kontenGambar.getViewUrl();
            this.downloadUrl = kontenGambar.getDownloadUrl();
        }
    }
}
