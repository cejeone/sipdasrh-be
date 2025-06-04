package com.kehutanan.pepdas.kegiatan.model.dto;

import com.kehutanan.pepdas.kegiatan.model.KegiatanLokusShp;
import lombok.Data;

import java.util.UUID;

@Data
public class KegiatanLokusShpDTO {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public KegiatanLokusShpDTO(KegiatanLokusShp kegiatanLokusShp) {
        this.id = kegiatanLokusShp.getId();
        this.namaFile = kegiatanLokusShp.getNamaFile();
        this.namaAsli = kegiatanLokusShp.getNamaAsli();
        this.pathFile = kegiatanLokusShp.getPathFile();
        this.ukuranMb = kegiatanLokusShp.getUkuranMb();
        this.contentType = kegiatanLokusShp.getContentType();
        this.uploadedAt = kegiatanLokusShp.getUploadedAt() != null ? 
                kegiatanLokusShp.getUploadedAt().toString() : null;
        this.viewUrl = kegiatanLokusShp.getViewUrl();
        this.downloadUrl = kegiatanLokusShp.getDownloadUrl();
    }
}