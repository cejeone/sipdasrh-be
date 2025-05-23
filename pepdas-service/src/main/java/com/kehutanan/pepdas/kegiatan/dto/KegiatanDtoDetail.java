package com.kehutanan.pepdas.kegiatan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.pepdas.kegiatan.model.KegiatanLokus;
import com.kehutanan.pepdas.kegiatan.model.KegiatanMonev;
import com.kehutanan.pepdas.kegiatan.model.KegiatanPemeliharaanPemupukan;
import com.kehutanan.pepdas.kegiatan.model.KegiatanPemeliharaanSulaman;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanSerahTerima;
import com.kehutanan.pepdas.program.model.Program;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDtoDetail {

    private UUID id;
    private String subDirektorat;
    private UUID programId;
    private String programName; // For display purposes
    private String jenisKegiatan;
    private String refPo;
    private String namaKegiatan;
    private List<KegiatanLokus> kegiatanLokus;
    private String detailPola;
    private Integer detailTahunKegiatan;
    private String detailSumberAnggaran;
    private Integer detailTotalBibit;
    private Integer detailTotalLuasHa;
    private String detailPemangkuKawasan;
    private String detailPelaksana;
    private List<KegiatanFungsiKawasan> fungsiKawasans;
    private List<KegiatanFileDto> rancanganTeknisPdfs;
    private List<KegiatanFileDto> rancanganTeknisFotos ;
    private List<KegiatanFileDto> rancanganTeknisVideos;
    private String kontrakNomor;
    private Integer kontrakNilai;
    private String kontrakTipe;
    private String kontrakPelaksana;
    private LocalDate kontrakTanggalKontrak;
    private List<KegiatanFileDto> kontrakPdfs ;
    private String kontrakStatus;
    private List<KegiatanPemeliharaanSulaman> sulamanList;
    private List<KegiatanPemeliharaanPemupukan> pemupukanList;
    private List<KegiatanFileDto> dokumentasiFotos ;
    private String dokumentasiCatatanFoto;
    private List<KegiatanFileDto> dokumentasiVideos;
    private String dokumentasiCatatanVideo;
    private List<KegiatanMonev> monevList;
    private List<KegiatanSerahTerima> serahTerimaList ;
}