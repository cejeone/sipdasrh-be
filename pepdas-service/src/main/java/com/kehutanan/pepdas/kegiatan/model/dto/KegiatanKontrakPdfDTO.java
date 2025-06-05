package com.kehutanan.pepdas.kegiatan.model.dto;

import com.kehutanan.pepdas.kegiatan.model.KegiatanKontrakPdf;
import lombok.Data;

import java.util.UUID;

@Data
public class KegiatanKontrakPdfDTO {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public KegiatanKontrakPdfDTO(KegiatanKontrakPdf kegiatanKontrakPdf) {
        this.id = kegiatanKontrakPdf.getId();
        this.namaFile = kegiatanKontrakPdf.getNamaFile();
        this.namaAsli = kegiatanKontrakPdf.getNamaAsli();
        this.pathFile = kegiatanKontrakPdf.getPathFile();
        this.ukuranMb = kegiatanKontrakPdf.getUkuranMb();
        this.contentType = kegiatanKontrakPdf.getContentType();
        this.uploadedAt = kegiatanKontrakPdf.getUploadedAt() != null ? 
                kegiatanKontrakPdf.getUploadedAt().toString() : null;
        this.viewUrl = kegiatanKontrakPdf.getViewUrl();
        this.downloadUrl = kegiatanKontrakPdf.getDownloadUrl();
    }
}