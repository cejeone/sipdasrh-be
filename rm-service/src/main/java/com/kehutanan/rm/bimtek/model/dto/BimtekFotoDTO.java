package com.kehutanan.rm.bimtek.model.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.bimtek.model.BimtekFoto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekFotoDTO {

    private UUID id;
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
        this.namaFile = bimtekFoto.getNamaFile();
        this.namaAsli = bimtekFoto.getNamaAsli();
        this.pathFile = bimtekFoto.getPathFile();
        this.ukuranMb = bimtekFoto.getUkuranMb();
        this.contentType = bimtekFoto.getContentType();
        this.uploadedAt = bimtekFoto.getUploadedAt() != null ? bimtekFoto.getUploadedAt().toString() : null;
        this.viewUrl = bimtekFoto.getViewUrl();
        this.downloadUrl = bimtekFoto.getDownloadUrl();
    }
}
