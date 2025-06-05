package com.kehutanan.rh.bimtek.model.dto;

import java.io.Serializable;
import java.util.UUID;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekFoto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekFotoDTO implements Serializable {

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
    
    public BimtekFotoDTO(BimtekFoto bimtekFoto) {
        this.id = bimtekFoto.getId();
        if (bimtekFoto.getBimtek() != null) {
            this.bimtekId = bimtekFoto.getBimtek().getId();
        }
        this.namaFile = bimtekFoto.getNamaFile();
        this.namaAsli = bimtekFoto.getNamaAsli();
        this.pathFile = bimtekFoto.getPathFile();
        this.ukuranMb = bimtekFoto.getUkuranMb();
        this.contentType = bimtekFoto.getContentType();
        if (bimtekFoto.getUploadedAt() != null) {
            this.uploadedAt = bimtekFoto.getUploadedAt().toString();
        }
        this.viewUrl = bimtekFoto.getViewUrl();
        this.downloadUrl = bimtekFoto.getDownloadUrl();
    }
}