package com.kehutanan.tktrh.ppkh.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastReboRehab;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastZip;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanPakPdfShp;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanPerijinanPdf;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRantekPdf;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRencanaRealisasi;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRiwayatSk;

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
    private LocalDateTime uploadedAt;
    
    public KegiatanFileDTO(KegiatanPerijinanPdf perijinanPdf) {
        this.id = perijinanPdf.getId();
        this.namaAsli = perijinanPdf.getNamaAsli();
        this.namaFile = perijinanPdf.getNamaFile();
        this.pathFile = perijinanPdf.getPathFile();
        this.viewUrl = perijinanPdf.getViewUrl();
        this.downloadUrl = perijinanPdf.getDownloadUrl();
        this.contentType = perijinanPdf.getContentType();
        this.ukuranMb = perijinanPdf.getUkuranMb();
        this.uploadedAt = perijinanPdf.getUploadedAt();
    }
    
    public KegiatanFileDTO(KegiatanRiwayatSk riwayatSk) {
        this.id = riwayatSk.getId();
        this.namaAsli = riwayatSk.getNamaAsli();
        this.namaFile = riwayatSk.getNamaFile();
        this.pathFile = riwayatSk.getPathFile();
        this.viewUrl = riwayatSk.getViewUrl();
        this.downloadUrl = riwayatSk.getDownloadUrl();
        this.contentType = riwayatSk.getContentType();
        this.ukuranMb = riwayatSk.getUkuranMb();
        this.uploadedAt = riwayatSk.getUploadedAt();
    }
    
    public KegiatanFileDTO(KegiatanPakPdfShp pakPdfShp) {
        this.id = pakPdfShp.getId();
        this.namaAsli = pakPdfShp.getNamaAsli();
        this.namaFile = pakPdfShp.getNamaFile();
        this.pathFile = pakPdfShp.getPathFile();
        this.viewUrl = pakPdfShp.getViewUrl();
        this.downloadUrl = pakPdfShp.getDownloadUrl();
        this.contentType = pakPdfShp.getContentType();
        this.ukuranMb = pakPdfShp.getUkuranMb();
        this.uploadedAt = pakPdfShp.getUploadedAt();
    }
    
    public KegiatanFileDTO(KegiatanFungsiKawasan fungsiKawasan) {
        this.id = fungsiKawasan.getId();
        this.namaAsli = fungsiKawasan.getNamaAsli();
        this.namaFile = fungsiKawasan.getNamaFile();
        this.pathFile = fungsiKawasan.getPathFile();
        this.viewUrl = fungsiKawasan.getViewUrl();
        this.downloadUrl = fungsiKawasan.getDownloadUrl();
        this.contentType = fungsiKawasan.getContentType();
        this.ukuranMb = fungsiKawasan.getUkuranMb();
        this.uploadedAt = fungsiKawasan.getUploadedAt();
    }
    
    public KegiatanFileDTO(KegiatanRantekPdf rantekPdf) {
        this.id = rantekPdf.getId();
        this.namaAsli = rantekPdf.getNamaAsli();
        this.namaFile = rantekPdf.getNamaFile();
        this.pathFile = rantekPdf.getPathFile();
        this.viewUrl = rantekPdf.getViewUrl();
        this.downloadUrl = rantekPdf.getDownloadUrl();
        this.contentType = rantekPdf.getContentType();
        this.ukuranMb = rantekPdf.getUkuranMb();
        this.uploadedAt = rantekPdf.getUploadedAt();
    }
    
    public KegiatanFileDTO(KegiatanRencanaRealisasi rencanaRealisasi) {
        this.id = rencanaRealisasi.getId();
        this.namaAsli = rencanaRealisasi.getNamaAsli();
        this.namaFile = rencanaRealisasi.getNamaFile();
        this.pathFile = rencanaRealisasi.getPathFile();
        this.viewUrl = rencanaRealisasi.getViewUrl();
        this.downloadUrl = rencanaRealisasi.getDownloadUrl();
        this.contentType = rencanaRealisasi.getContentType();
        this.ukuranMb = rencanaRealisasi.getUkuranMb();
        this.uploadedAt = rencanaRealisasi.getUploadedAt();
    }
    
    public KegiatanFileDTO(KegiatanBastReboRehab bastReboRehab) {
        this.id = bastReboRehab.getId();
        this.namaAsli = bastReboRehab.getNamaAsli();
        this.namaFile = bastReboRehab.getNamaFile();
        this.pathFile = bastReboRehab.getPathFile();
        this.viewUrl = bastReboRehab.getViewUrl();
        this.downloadUrl = bastReboRehab.getDownloadUrl();
        this.contentType = bastReboRehab.getContentType();
        this.ukuranMb = bastReboRehab.getUkuranMb();
        this.uploadedAt = bastReboRehab.getUploadedAt();
    }
    
    public KegiatanFileDTO(KegiatanBastZip bastZip) {
        this.id = bastZip.getId();
        this.namaAsli = bastZip.getNamaAsli();
        this.namaFile = bastZip.getNamaFile();
        this.pathFile = bastZip.getPathFile();
        this.viewUrl = bastZip.getViewUrl();
        this.downloadUrl = bastZip.getDownloadUrl();
        this.contentType = bastZip.getContentType();
        this.ukuranMb = bastZip.getUkuranMb();
        this.uploadedAt = bastZip.getUploadedAt();
    }
}
