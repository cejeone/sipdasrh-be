package com.kehutanan.rm.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.master.model.PenggunaFoto;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PenggunaFotoDTO {
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public PenggunaFotoDTO(PenggunaFoto penggunaFoto) {
        if (penggunaFoto != null) {
            this.id = penggunaFoto.getId();
            this.namaFile = penggunaFoto.getNamaFile();
            this.namaAsli = penggunaFoto.getNamaAsli();
            this.pathFile = penggunaFoto.getPathFile();
            this.ukuranMb = penggunaFoto.getUkuranMb();
            this.contentType = penggunaFoto.getContentType();
            this.uploadedAt = penggunaFoto.getUploadedAt();
            this.viewUrl = penggunaFoto.getViewUrl();
            this.downloadUrl = penggunaFoto.getDownloadUrl();
        }
    }
}