package com.kehutanan.ppth.penghijauan.kegiatan.model.dto;

import java.util.UUID;
import com.kehutanan.ppth.penghijauan.kegiatan.model.KegiatanKontrakPdf;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    public KegiatanKontrakPdfDTO(KegiatanKontrakPdf entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.namaFile = entity.getNamaFile();
            this.namaAsli = entity.getNamaAsli();
            this.pathFile = entity.getPathFile();
            this.ukuranMb = entity.getUkuranMb();
            this.contentType = entity.getContentType();
            this.uploadedAt = entity.getUploadedAt() != null ? entity.getUploadedAt().toString() : null;
            this.viewUrl = entity.getViewUrl();
            this.downloadUrl = entity.getDownloadUrl();
        }
    }
}
