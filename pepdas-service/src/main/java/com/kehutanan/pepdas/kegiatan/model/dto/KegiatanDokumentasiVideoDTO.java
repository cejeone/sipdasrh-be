package com.kehutanan.pepdas.kegiatan.model.dto;

import com.kehutanan.pepdas.kegiatan.model.KegiatanDokumentasiVideo;
import lombok.Data;

import java.util.UUID;

@Data
public class KegiatanDokumentasiVideoDTO {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public KegiatanDokumentasiVideoDTO(KegiatanDokumentasiVideo kegiatanDokumentasiVideo) {
        this.id = kegiatanDokumentasiVideo.getId();
        this.namaFile = kegiatanDokumentasiVideo.getNamaFile();
        this.namaAsli = kegiatanDokumentasiVideo.getNamaAsli();
        this.pathFile = kegiatanDokumentasiVideo.getPathFile();
        this.ukuranMb = kegiatanDokumentasiVideo.getUkuranMb();
        this.contentType = kegiatanDokumentasiVideo.getContentType();
        this.uploadedAt = kegiatanDokumentasiVideo.getUploadedAt() != null ? 
                kegiatanDokumentasiVideo.getUploadedAt().toString() : null;
        this.viewUrl = kegiatanDokumentasiVideo.getViewUrl();
        this.downloadUrl = kegiatanDokumentasiVideo.getDownloadUrl();
    }
}