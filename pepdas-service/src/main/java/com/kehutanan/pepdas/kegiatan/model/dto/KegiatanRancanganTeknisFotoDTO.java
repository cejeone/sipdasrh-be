package com.kehutanan.pepdas.kegiatan.model.dto;

import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisFoto;
import lombok.Data;

import java.util.UUID;

@Data
public class KegiatanRancanganTeknisFotoDTO {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public KegiatanRancanganTeknisFotoDTO(KegiatanRancanganTeknisFoto kegiatanRancanganTeknisFoto) {
        this.id = kegiatanRancanganTeknisFoto.getId();
        this.namaFile = kegiatanRancanganTeknisFoto.getNamaFile();
        this.namaAsli = kegiatanRancanganTeknisFoto.getNamaAsli();
        this.pathFile = kegiatanRancanganTeknisFoto.getPathFile();
        this.ukuranMb = kegiatanRancanganTeknisFoto.getUkuranMb();
        this.contentType = kegiatanRancanganTeknisFoto.getContentType();
        this.uploadedAt = kegiatanRancanganTeknisFoto.getUploadedAt() != null ? 
                kegiatanRancanganTeknisFoto.getUploadedAt().toString() : null;
        this.viewUrl = kegiatanRancanganTeknisFoto.getViewUrl();
        this.downloadUrl = kegiatanRancanganTeknisFoto.getDownloadUrl();
    }
}