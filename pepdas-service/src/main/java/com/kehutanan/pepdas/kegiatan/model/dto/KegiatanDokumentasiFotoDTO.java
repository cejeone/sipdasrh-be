package com.kehutanan.pepdas.kegiatan.model.dto;

import com.kehutanan.pepdas.kegiatan.model.KegiatanDokumentasiFoto;
import lombok.Data;

import java.util.UUID;

@Data
public class KegiatanDokumentasiFotoDTO {
    
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public KegiatanDokumentasiFotoDTO(KegiatanDokumentasiFoto kegiatanDokumentasiFoto) {
        this.id = kegiatanDokumentasiFoto.getId();
        this.namaFile = kegiatanDokumentasiFoto.getNamaFile();
        this.namaAsli = kegiatanDokumentasiFoto.getNamaAsli();
        this.pathFile = kegiatanDokumentasiFoto.getPathFile();
        this.ukuranMb = kegiatanDokumentasiFoto.getUkuranMb();
        this.contentType = kegiatanDokumentasiFoto.getContentType();
        this.uploadedAt = kegiatanDokumentasiFoto.getUploadedAt() != null ? 
                kegiatanDokumentasiFoto.getUploadedAt().toString() : null;
        this.viewUrl = kegiatanDokumentasiFoto.getViewUrl();
        this.downloadUrl = kegiatanDokumentasiFoto.getDownloadUrl();
    }
}