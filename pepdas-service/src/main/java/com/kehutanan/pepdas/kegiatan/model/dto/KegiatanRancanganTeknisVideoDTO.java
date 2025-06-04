package com.kehutanan.pepdas.kegiatan.model.dto;

import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisVideo;
import lombok.Data;

import java.util.UUID;

@Data
public class KegiatanRancanganTeknisVideoDTO {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public KegiatanRancanganTeknisVideoDTO(KegiatanRancanganTeknisVideo kegiatanRancanganTeknisVideo) {
        this.id = kegiatanRancanganTeknisVideo.getId();
        this.namaFile = kegiatanRancanganTeknisVideo.getNamaFile();
        this.namaAsli = kegiatanRancanganTeknisVideo.getNamaAsli();
        this.pathFile = kegiatanRancanganTeknisVideo.getPathFile();
        this.ukuranMb = kegiatanRancanganTeknisVideo.getUkuranMb();
        this.contentType = kegiatanRancanganTeknisVideo.getContentType();
        this.uploadedAt = kegiatanRancanganTeknisVideo.getUploadedAt() != null ? 
                kegiatanRancanganTeknisVideo.getUploadedAt().toString() : null;
        this.viewUrl = kegiatanRancanganTeknisVideo.getViewUrl();
        this.downloadUrl = kegiatanRancanganTeknisVideo.getDownloadUrl();
    }
}