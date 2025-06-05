package com.kehutanan.pepdas.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.pepdas.master.model.UptdPetaShp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptdPetaShpDTO {
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public UptdPetaShpDTO(UptdPetaShp uptdPetaShp) {
        if (uptdPetaShp != null) {
            this.id = uptdPetaShp.getId();
            this.namaFile = uptdPetaShp.getNamaFile();
            this.namaAsli = uptdPetaShp.getNamaAsli();
            this.pathFile = uptdPetaShp.getPathFile();
            this.ukuranMb = uptdPetaShp.getUkuranMb();
            this.contentType = uptdPetaShp.getContentType();
            this.uploadedAt = uptdPetaShp.getUploadedAt();
            this.viewUrl = uptdPetaShp.getViewUrl();
            this.downloadUrl = uptdPetaShp.getDownloadUrl();
        }
    }
}