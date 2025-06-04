package com.kehutanan.rm.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rm.kegiatan.model.KegiatanPemeliharaanSulaman;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanSulamanDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    
    private Long kategoriId;
    private String kategoriNama;
    
    private Long namaBibitId;
    private String namaBibitNama;
    
    private Long sumberBibitId;
    private String sumberBibitNama;
    
    private Long kondisiTanamanId;
    private String kondisiTanamanNama;
    
    private Long statusId;
    private String statusNama;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate waktuPenyulaman;
    
    private Integer jumlahBibit;
    private Integer jumlahTanamanHidup;
    private Integer jumlahHokPerempuan;
    private Integer jumlahHokLakiLaki;
    private String keterangan;
    
    // Constructor to convert from Entity
    public KegiatanPemeliharaanSulamanDTO(KegiatanPemeliharaanSulaman entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = entity.getKategoriId().getId();
            this.kategoriNama = entity.getKategoriId().getNilai();
        }
        
        if (entity.getNamaBibitId() != null) {
            this.namaBibitId = entity.getNamaBibitId().getId();
            this.namaBibitNama = entity.getNamaBibitId().getNilai();
        }
        
        if (entity.getSumberBibitId() != null) {
            this.sumberBibitId = entity.getSumberBibitId().getId();
            this.sumberBibitNama = entity.getSumberBibitId().getNilai();
        }
        
        if (entity.getKondisiTanamanId() != null) {
            this.kondisiTanamanId = entity.getKondisiTanamanId().getId();
            this.kondisiTanamanNama = entity.getKondisiTanamanId().getNilai();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        this.waktuPenyulaman = entity.getWaktuPenyulaman();
        this.jumlahBibit = entity.getJumlahBibit();
        this.jumlahTanamanHidup = entity.getJumlahTanamanHidup();
        this.jumlahHokPerempuan = entity.getJumlahHokPerempuan();
        this.jumlahHokLakiLaki = entity.getJumlahHokLakiLaki();
        this.keterangan = entity.getKeterangan();
    }
}