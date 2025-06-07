package com.kehutanan.ppth.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.ppth.master.model.UptdDokumentasiVideo;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptdDokumentasiVideoDTO {
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public UptdDokumentasiVideoDTO(UptdDokumentasiVideo uptdDokumentasiVideo) {
        if (uptdDokumentasiVideo != null) {
            this.id = uptdDokumentasiVideo.getId();
            this.namaFile = uptdDokumentasiVideo.getNamaFile();
            this.namaAsli = uptdDokumentasiVideo.getNamaAsli();
            this.pathFile = uptdDokumentasiVideo.getPathFile();
            this.ukuranMb = uptdDokumentasiVideo.getUkuranMb();
            this.contentType = uptdDokumentasiVideo.getContentType();
            this.uploadedAt = uptdDokumentasiVideo.getUploadedAt();
            this.viewUrl = uptdDokumentasiVideo.getViewUrl();
            this.downloadUrl = uptdDokumentasiVideo.getDownloadUrl();
        }
    }
}