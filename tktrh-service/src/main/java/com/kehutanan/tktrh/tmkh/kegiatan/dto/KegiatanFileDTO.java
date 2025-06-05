package com.kehutanan.tktrh.tmkh.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.tmkh.kegiatan.model.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanFileDTO implements Serializable {
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
    
    // Constructors for different file types
    public KegiatanFileDTO(KegiatanPerijinanPdf pdf) {
        this.id = pdf.getId();
        this.namaAsli = pdf.getNamaAsli();
        this.namaFile = pdf.getNamaFile();
        this.pathFile = pdf.getPathFile();
        this.viewUrl = pdf.getViewUrl();
        this.downloadUrl = pdf.getDownloadUrl();
        this.contentType = pdf.getContentType();
        this.ukuranMb = pdf.getUkuranMb();
        if (pdf.getUploadedAt() != null) {
            this.uploadedAt = pdf.getUploadedAt().toString();
        }
    }
    
    public KegiatanFileDTO(KegiatanRiwayatSk sk) {
        this.id = sk.getId();
        this.namaAsli = sk.getNamaAsli();
        this.namaFile = sk.getNamaFile();
        this.pathFile = sk.getPathFile();
        this.viewUrl = sk.getViewUrl();
        this.downloadUrl = sk.getDownloadUrl();
        this.contentType = sk.getContentType();
        this.ukuranMb = sk.getUkuranMb();
        if (sk.getUploadedAt() != null) {
            this.uploadedAt = sk.getUploadedAt().toString();
        }
    }
    
    public KegiatanFileDTO(KegiatanPbakPdfShp pdfShp) {
        this.id = pdfShp.getId();
        this.namaAsli = pdfShp.getNamaAsli();
        this.namaFile = pdfShp.getNamaFile();
        this.pathFile = pdfShp.getPathFile();
        this.viewUrl = pdfShp.getViewUrl();
        this.downloadUrl = pdfShp.getDownloadUrl();
        this.contentType = pdfShp.getContentType();
        this.ukuranMb = pdfShp.getUkuranMb();
        if (pdfShp.getUploadedAt() != null) {
            this.uploadedAt = pdfShp.getUploadedAt().toString();
        }
    }
    
    public KegiatanFileDTO(KegiatanFungsiKawasanLahanPengganti pengganti) {
        this.id = pengganti.getId();
        this.namaAsli = pengganti.getNamaAsli();
        this.namaFile = pengganti.getNamaFile();
        this.pathFile = pengganti.getPathFile();
        this.viewUrl = pengganti.getViewUrl();
        this.downloadUrl = pengganti.getDownloadUrl();
        this.contentType = pengganti.getContentType();
        this.ukuranMb = pengganti.getUkuranMb();
        if (pengganti.getUploadedAt() != null) {
            this.uploadedAt = pengganti.getUploadedAt().toString();
        }
    }
    
    public KegiatanFileDTO(KegiatanFungsiKawasanRehab rehab) {
        this.id = rehab.getId();
        this.namaAsli = rehab.getNamaAsli();
        this.namaFile = rehab.getNamaFile();
        this.pathFile = rehab.getPathFile();
        this.viewUrl = rehab.getViewUrl();
        this.downloadUrl = rehab.getDownloadUrl();
        this.contentType = rehab.getContentType();
        this.ukuranMb = rehab.getUkuranMb();
        if (rehab.getUploadedAt() != null) {
            this.uploadedAt = rehab.getUploadedAt().toString();
        }
    }
    
    public KegiatanFileDTO(KegiatanRehabPdf pdf) {
        this.id = pdf.getId();
        this.namaAsli = pdf.getNamaAsli();
        this.namaFile = pdf.getNamaFile();
        this.pathFile = pdf.getPathFile();
        this.viewUrl = pdf.getViewUrl();
        this.downloadUrl = pdf.getDownloadUrl();
        this.contentType = pdf.getContentType();
        this.ukuranMb = pdf.getUkuranMb();
        if (pdf.getUploadedAt() != null) {
            this.uploadedAt = pdf.getUploadedAt().toString();
        }
    }
    
    public KegiatanFileDTO(KegiatanRealisasiReboisasi reboisasi) {
        this.id = reboisasi.getId();
        this.namaAsli = reboisasi.getNamaAsli();
        this.namaFile = reboisasi.getNamaFile();
        this.pathFile = reboisasi.getPathFile();
        this.viewUrl = reboisasi.getViewUrl();
        this.downloadUrl = reboisasi.getDownloadUrl();
        this.contentType = reboisasi.getContentType();
        this.ukuranMb = reboisasi.getUkuranMb();
        if (reboisasi.getUploadedAt() != null) {
            this.uploadedAt = reboisasi.getUploadedAt().toString();
        }
    }
    
    public KegiatanFileDTO(KegiatanMonev monev) {
        this.id = monev.getId();
        this.namaAsli = monev.getNamaAsli();
        this.namaFile = monev.getNamaFile();
        this.pathFile = monev.getPathFile();
        this.viewUrl = monev.getViewUrl();
        this.downloadUrl = monev.getDownloadUrl();
        this.contentType = monev.getContentType();
        this.ukuranMb = monev.getUkuranMb();
        if (monev.getUploadedAt() != null) {
            this.uploadedAt = monev.getUploadedAt().toString();
        }
    }
    
    public KegiatanFileDTO(KegiatanBastRehabDas bast) {
        this.id = bast.getId();
        this.namaAsli = bast.getNamaAsli();
        this.namaFile = bast.getNamaFile();
        this.pathFile = bast.getPathFile();
        this.viewUrl = bast.getViewUrl();
        this.downloadUrl = bast.getDownloadUrl();
        this.contentType = bast.getContentType();
        this.ukuranMb = bast.getUkuranMb();
        if (bast.getUploadedAt() != null) {
            this.uploadedAt = bast.getUploadedAt().toString();
        }
    }
    
    public KegiatanFileDTO(KegiatanBastZip zip) {
        this.id = zip.getId();
        this.namaAsli = zip.getNamaAsli();
        this.namaFile = zip.getNamaFile();
        this.pathFile = zip.getPathFile();
        this.viewUrl = zip.getViewUrl();
        this.downloadUrl = zip.getDownloadUrl();
        this.contentType = zip.getContentType();
        this.ukuranMb = zip.getUkuranMb();
        if (zip.getUploadedAt() != null) {
            this.uploadedAt = zip.getUploadedAt().toString();
        }
    }
}
