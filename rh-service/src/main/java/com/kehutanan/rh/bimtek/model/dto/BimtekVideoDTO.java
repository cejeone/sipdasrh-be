package com.kehutanan.rh.bimtek.model.dto;

import java.io.Serializable;
import java.util.UUID;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekVideo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekVideoDTO implements Serializable {

    private UUID id;
    private Long bimtekId;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public BimtekVideoDTO(BimtekVideo bimtekVideo) {
        this.id = bimtekVideo.getId();
        if (bimtekVideo.getBimtek() != null) {
            this.bimtekId = bimtekVideo.getBimtek().getId();
        }
        this.namaFile = bimtekVideo.getNamaFile();
        this.namaAsli = bimtekVideo.getNamaAsli();
        this.pathFile = bimtekVideo.getPathFile();
        this.ukuranMb = bimtekVideo.getUkuranMb();
        this.contentType = bimtekVideo.getContentType();
        if (bimtekVideo.getUploadedAt() != null) {
            this.uploadedAt = bimtekVideo.getUploadedAt().toString();
        }
        this.viewUrl = bimtekVideo.getViewUrl();
        this.downloadUrl = bimtekVideo.getDownloadUrl();
    }
}