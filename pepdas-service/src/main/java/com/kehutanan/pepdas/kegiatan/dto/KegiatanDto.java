package com.kehutanan.pepdas.kegiatan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for Kegiatan entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDto {
    private UUID id;
    private String subDirektorat;
    private UUID programId;
    private String programNama;
    private String jenisKegiatan;
    private String namaKegiatan;
    private String bpdas;
    private String das;
    
    // Lokasi fields
    private String lokusProvinsi;
    private String lokusKabupatenKota;
    private String lokusKecamatan;
    private String lokusKelurahanDesa;
    private String lokusAlamat;

    // Detail fields
    private String detailSkema;
    private Integer detailTahunKegiatan;
    private String detailSumberAnggaran;
    private String detailPenerimaManfaat;
    private String detailPelaksana;
    

    // Kontrak fields
    private String kontrakNomor;
    private Integer kontrakNilai;
    private String kontrakTipe;
    private String kontrakPenerimaManfaat;
    private LocalDate kontrakTanggalKontrak;
    private String kontrakStatus;

    // Dokumentasi fields
    private String dokumentasiCatatanFoto;
    private String dokumentasiCatatanVideo;
}