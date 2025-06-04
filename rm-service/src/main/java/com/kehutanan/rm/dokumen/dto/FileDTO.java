package com.kehutanan.rm.dokumen.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rm.dokumen.model.DokumenFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String namaAsli;
    private String namaFile;
    private String pathFile;
    private String viewUrl;
    private String downloadUrl;
    private String contentType;
    private Double ukuranMb;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime uploadedAt;
    
    public FileDTO(DokumenFile entity) {
        this.id = entity.getId();
        this.namaAsli = entity.getNamaAsli();
        this.namaFile = entity.getNamaFile();
        this.pathFile = entity.getPathFile();
        this.viewUrl = entity.getViewUrl();
        this.downloadUrl = entity.getDownloadUrl();
        this.contentType = entity.getContentType();
        this.ukuranMb = entity.getUkuranMb();
        this.uploadedAt = entity.getUploadedAt();
    }
}