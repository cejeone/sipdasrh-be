package com.kehutanan.ppth.mataair.serahterima.model.dto;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.kehutanan.ppth.mataair.serahterima.model.BastPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BastPdfDTO {

    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public BastPdfDTO(BastPdf bastPdf) {
        if (bastPdf != null) {
            this.id = bastPdf.getId();
            this.namaFile = bastPdf.getNamaFile();
            this.namaAsli = bastPdf.getNamaAsli();
            this.pathFile = bastPdf.getPathFile();
            this.ukuranMb = bastPdf.getUkuranMb();
            this.contentType = bastPdf.getContentType();
            this.uploadedAt = bastPdf.getUploadedAt() != null ? 
                bastPdf.getUploadedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            this.viewUrl = bastPdf.getViewUrl();
            this.downloadUrl = bastPdf.getDownloadUrl();
        }
    }
}