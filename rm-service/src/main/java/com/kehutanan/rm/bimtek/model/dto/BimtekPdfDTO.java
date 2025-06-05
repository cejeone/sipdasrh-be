package com.kehutanan.rm.bimtek.model.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.bimtek.model.BimtekPdf;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekPdfDTO {

    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public BimtekPdfDTO(BimtekPdf bimtekPdf) {
        this.id = bimtekPdf.getId();
        this.namaFile = bimtekPdf.getNamaFile();
        this.namaAsli = bimtekPdf.getNamaAsli();
        this.pathFile = bimtekPdf.getPathFile();
        this.ukuranMb = bimtekPdf.getUkuranMb();
        this.contentType = bimtekPdf.getContentType();
        this.uploadedAt = bimtekPdf.getUploadedAt() != null ? bimtekPdf.getUploadedAt().toString() : null;
        this.viewUrl = bimtekPdf.getViewUrl();
        this.downloadUrl = bimtekPdf.getDownloadUrl();
    }
}
