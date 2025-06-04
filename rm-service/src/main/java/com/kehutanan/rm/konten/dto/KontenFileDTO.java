package com.kehutanan.rm.konten.dto;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rm.konten.model.KontenGambar;
import com.kehutanan.rm.konten.model.KontenGambarUtama;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KontenFileDTO implements Serializable {
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
    private String uploadedAt;
    
    public KontenFileDTO(KontenGambar gambar) {
        this.id = gambar.getId();
        this.namaAsli = gambar.getNamaAsli();
        this.namaFile = gambar.getNamaFile();
        this.pathFile = gambar.getPathFile();
        this.viewUrl = gambar.getViewUrl();
        this.downloadUrl = gambar.getDownloadUrl();
        this.contentType = gambar.getContentType();
        this.ukuranMb = gambar.getUkuranMb();
        if (gambar.getUploadedAt() != null) {
            this.uploadedAt = gambar.getUploadedAt().toString();
        }
    }
    
    public KontenFileDTO(KontenGambarUtama gambarUtama) {
        this.id = gambarUtama.getId();
        this.namaAsli = gambarUtama.getNamaAsli();
        this.namaFile = gambarUtama.getNamaFile();
        this.pathFile = gambarUtama.getPathFile();
        this.viewUrl = gambarUtama.getViewUrl();
        this.downloadUrl = gambarUtama.getDownloadUrl();
        this.contentType = gambarUtama.getContentType();
        this.ukuranMb = gambarUtama.getUkuranMb();
        if (gambarUtama.getUploadedAt() != null) {
            this.uploadedAt = gambarUtama.getUploadedAt().toString();
        }
    }
}