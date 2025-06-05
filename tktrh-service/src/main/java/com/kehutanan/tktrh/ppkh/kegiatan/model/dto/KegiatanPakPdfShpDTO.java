package com.kehutanan.tktrh.ppkh.kegiatan.model.dto;

import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanPakPdfShp;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPakPdfShpDTO {
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public KegiatanPakPdfShpDTO(KegiatanPakPdfShp entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.namaFile = entity.getNamaFile();
            this.namaAsli = entity.getNamaAsli();
            this.pathFile = entity.getPathFile();
            this.ukuranMb = entity.getUkuranMb();
            this.contentType = entity.getContentType();
            this.uploadedAt = entity.getUploadedAt() != null ? 
                entity.getUploadedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            this.viewUrl = entity.getViewUrl();
            this.downloadUrl = entity.getDownloadUrl();
        }
    }
}
