package com.kehutanan.pepdas.kegiatan.model.dto;

import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisPdf;
import lombok.Data;

import java.util.UUID;

@Data
public class KegiatanRancanganTeknisPdfDTO {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public KegiatanRancanganTeknisPdfDTO(KegiatanRancanganTeknisPdf kegiatanRancanganTeknisPdf) {
        this.id = kegiatanRancanganTeknisPdf.getId();
        this.namaFile = kegiatanRancanganTeknisPdf.getNamaFile();
        this.namaAsli = kegiatanRancanganTeknisPdf.getNamaAsli();
        this.pathFile = kegiatanRancanganTeknisPdf.getPathFile();
        this.ukuranMb = kegiatanRancanganTeknisPdf.getUkuranMb();
        this.contentType = kegiatanRancanganTeknisPdf.getContentType();
        this.uploadedAt = kegiatanRancanganTeknisPdf.getUploadedAt() != null ? 
                kegiatanRancanganTeknisPdf.getUploadedAt().toString() : null;
        this.viewUrl = kegiatanRancanganTeknisPdf.getViewUrl();
        this.downloadUrl = kegiatanRancanganTeknisPdf.getDownloadUrl();
    }
}