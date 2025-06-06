package com.kehutanan.ppth.mataair.kegiatan.dto;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.ppth.mataair.kegiatan.model.KegiatanLokusShp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokusFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String namaAsli;
    private String namaFile;
    private String pathFile;
    private String viewUrl;
    private String downloadUrl;
    private String contentType;
    private Double ukuranMb;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String uploadedAt;
    
    public KegiatanLokusFileDTO(KegiatanLokusShp shp) {
        this.id = shp.getId();
        this.namaAsli = shp.getNamaAsli();
        this.namaFile = shp.getNamaFile();
        this.pathFile = shp.getPathFile();
        this.viewUrl = shp.getViewUrl();
        this.downloadUrl = shp.getDownloadUrl();
        this.contentType = shp.getContentType();
        this.ukuranMb = shp.getUkuranMb();
        if (shp.getUploadedAt() != null) {
            this.uploadedAt = shp.getUploadedAt().toString();
        }
    }
}