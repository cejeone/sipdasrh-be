package com.kehutanan.rh.kegiatan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDto {
    
    private String subDirektorat;
    private UUID programId;
    private String programName; // For display purposes
    private String jenisKegiatan;
    private String refPo;
    private String namaKegiatan;
    private String detailPola;
    private Integer detailTahunKegiatan;
    private String detailSumberAnggaran;
    private Integer detailTotalBibit;
    private Integer detailTotalLuasHa;
    private String detailPemangkuKawasan;
    private String detailPelaksana;
    private String kontrakNomor;
    private Integer kontrakNilai;
    private String kontrakTipe;
    private String kontrakPelaksana;
    private LocalDate kontrakTanggalKontrak;
    private String kontrakStatus;
    private String dokumentasiCatatanFoto;
    private String dokumentasiCatatanVideo;
}


