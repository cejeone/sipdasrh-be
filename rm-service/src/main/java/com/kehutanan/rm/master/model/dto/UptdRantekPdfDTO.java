package com.kehutanan.rm.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.master.model.UptdRantekPdf;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptdRantekPdfDTO {
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public UptdRantekPdfDTO(UptdRantekPdf uptdRantekPdf) {
        if (uptdRantekPdf != null) {
            this.id = uptdRantekPdf.getId();
            this.namaFile = uptdRantekPdf.getNamaFile();
            this.namaAsli = uptdRantekPdf.getNamaAsli();
            this.pathFile = uptdRantekPdf.getPathFile();
            this.ukuranMb = uptdRantekPdf.getUkuranMb();
            this.contentType = uptdRantekPdf.getContentType();
            this.uploadedAt = uptdRantekPdf.getUploadedAt();
            this.viewUrl = uptdRantekPdf.getViewUrl();
            this.downloadUrl = uptdRantekPdf.getDownloadUrl();
        }
    }
}