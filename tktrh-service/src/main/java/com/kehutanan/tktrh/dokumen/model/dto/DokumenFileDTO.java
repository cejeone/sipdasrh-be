package com.kehutanan.tktrh.dokumen.model.dto;

import com.kehutanan.tktrh.dokumen.model.DokumenFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DokumenFileDTO {

    private UUID id;
    private String namaAsli;
    private String namaFile;
    private String pathFile;
    private String viewUrl;
    private String downloadUrl;
    private String contentType;
    private Double ukuranMb;
    private String uploadedAt;

    public DokumenFileDTO(DokumenFile dokumenFile) {
        if (dokumenFile != null) {
            this.id = dokumenFile.getId();
            this.namaAsli = dokumenFile.getNamaAsli();
            this.namaFile = dokumenFile.getNamaFile();
            this.pathFile = dokumenFile.getPathFile();
            this.viewUrl = dokumenFile.getViewUrl();
            this.downloadUrl = dokumenFile.getDownloadUrl();
            this.contentType = dokumenFile.getContentType();
            this.ukuranMb = dokumenFile.getUkuranMb();
            
            if (dokumenFile.getUploadedAt() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                this.uploadedAt = dokumenFile.getUploadedAt().format(formatter);
            }
        }
    }
}
