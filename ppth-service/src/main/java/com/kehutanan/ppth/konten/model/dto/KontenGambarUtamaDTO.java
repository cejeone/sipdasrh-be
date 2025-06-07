package com.kehutanan.ppth.konten.model.dto;

import com.kehutanan.ppth.konten.model.KontenGambarUtama;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
public class KontenGambarUtamaDTO {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public KontenGambarUtamaDTO(KontenGambarUtama kontenGambarUtama) {
        if (kontenGambarUtama != null) {
            this.id = kontenGambarUtama.getId();
            this.namaFile = kontenGambarUtama.getNamaFile();
            this.namaAsli = kontenGambarUtama.getNamaAsli();
            this.pathFile = kontenGambarUtama.getPathFile();
            this.ukuranMb = kontenGambarUtama.getUkuranMb();
            this.contentType = kontenGambarUtama.getContentType();
            this.uploadedAt = kontenGambarUtama.getUploadedAt() != null ? 
                             kontenGambarUtama.getUploadedAt().toString() : null;
            this.viewUrl = kontenGambarUtama.getViewUrl();
            this.downloadUrl = kontenGambarUtama.getDownloadUrl();
        }
    }
}
