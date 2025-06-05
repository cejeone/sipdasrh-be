package com.kehutanan.tktrh.perijinan.model.dto;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.kehutanan.tktrh.perijinan.model.PerijinanDokumenBastPdf;

import lombok.Data;

@Data
public class PerijinanDokumenBastPdfDTO implements Serializable {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public PerijinanDokumenBastPdfDTO(PerijinanDokumenBastPdf entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.namaFile = entity.getNamaFile();
            this.namaAsli = entity.getNamaAsli();
            this.pathFile = entity.getPathFile();
            this.ukuranMb = entity.getUkuranMb();
            this.contentType = entity.getContentType();
            this.uploadedAt = entity.getUploadedAt() != null ? 
                entity.getUploadedAt().format(DateTimeFormatter.ISO_DATE_TIME) : null;
            this.viewUrl = entity.getViewUrl();
            this.downloadUrl = entity.getDownloadUrl();
        }
    }
}
