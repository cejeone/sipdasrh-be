package com.kehutanan.tktrh.tmkh.kegiatan.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokus;
import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.Eselon3;
import com.kehutanan.tktrh.master.model.KabupatenKota;
import com.kehutanan.tktrh.master.model.Kecamatan;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.Provinsi;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRantekPdf;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRencanaRealisasi;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "trx_tmkh_kegiatan")
@NoArgsConstructor
@AllArgsConstructor
public class Kegiatan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Informasi Umum Kegiatan

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private Program program;

    @Column(name = "nama_kegiatan", columnDefinition = "TEXT")
    private String namaKegiatan;

    @Column(name = "tahun")
    private Integer tahun;

    @ManyToOne
    @JoinColumn(name = "provinsi_id", referencedColumnName = "id")
    private Provinsi provinsi;

    @ManyToOne
    @JoinColumn(name = "kabupaten_kota_id", referencedColumnName = "id")
    private KabupatenKota kabupatenKota;

    @ManyToOne
    @JoinColumn(name = "kecamatan_id", referencedColumnName = "id")
    private Kecamatan kecamatan;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdas;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    // Perijinan

    @ManyToOne
    @JoinColumn(name = "pemegang_ijin_id", referencedColumnName = "id")
    private Lov pemegangIjinId;

    @ManyToOne
    @JoinColumn(name = "peruntukan_id", referencedColumnName = "id")
    private Lov peruntukan;
   
    @Column(name = "nama_peruntukan", columnDefinition = "TEXT")
    private Lov namaPeruntukan;

    @ManyToOne
    @JoinColumn(name = "jenis_ijin_id", referencedColumnName = "id")
    private Lov jenisIjin;

    @Column(name = "nomor_sk_perijinan")
    private String nomorSkPerijinan;

    @Column(name = "tanggal_sk_perijinan")
    private LocalDate tanggalSkPerijinan;

    @Column(name = "tanggal_berakhir_sk_perijinan")
    private LocalDate tanggalBerakhirSkPerijinan;

    @Column(name = "luas_sesuai_sk_perijinan_ha")
    private Double luasSesuaiSkPerijinanHa;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPerijinanPdf> kegiatanPerijinanPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRiwayatSk> RiwayatSks = new ArrayList<>();

    // PBAK

    @Column(name = "nomor_sk_pbak")
    private String nomorSkPbak;

    @Column(name = "tanggal_sk_pbak")
    private LocalDate tanggalSkPbak;

    @Column(name = "tanggal_berakhir_sk_pbak")
    private LocalDate tanggalBerakhirSkPbak;

    @Column(name = "luas_yang_ditetapkan_sesuai_sk_pbak")
    private Double luasSesuaiSkPbakHa;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPbakPdfShp> kegiatanPakPdfShps = new ArrayList<>();

    // Lahan Pengganti

    @Column(name = "nomor_sk_lahan_pengganti")
    private String nomorSkLahanPengganti;

    @Column(name = "tanggal_sk_lahan_pengganti")
    private LocalDate tanggalSkLahanPengganti;

    @Column(name = "tanggal_berakhir_sk_lahan_pengganti")
    private LocalDate tanggalBerakhirSkLahanPengganti;

    @Column(name = "luas_sk_lahan_pengganti")
    private Double luasSkLahanPengganti;

    @Column(name = "keterangan_lahan_pengganti", columnDefinition = "TEXT")
    private String keteranganLahanPengganti;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanFungsiKawasanLahanPengganti> fungsiKawasanLahanPenggantis = new ArrayList<>();

    // Rehabilitasi

    @Column(name = "nomor_sk_rehabilitasi")
    private String nomorSkRehabilitasi;

    @Column(name = "tanggal_sk_rehabilitasi")
    private LocalDate tanggalSkRehabilitasi;

    @Column(name = "tanggal_berakhir_sk_rehabilitasi")
    private LocalDate tanggalBerakhirSkRehabilitasi;

    @Column(name = "luas_sk_rehab_das_ha")
    private Double luasSkRehabilitasi;

    @ManyToOne
    @JoinColumn(name = "bpdas_rehab_id", referencedColumnName = "id")
    private Bpdas bpdasRehabId;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanFungsiKawasanRehab> fungsiKawasanRehabs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRehabPdf> kegiatanRehabPdfs = new ArrayList<>();

    // Kinerja

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRealisasiReboisasi> kegiatanRealisasiReboisasis = new ArrayList<>();

    // Monev

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanMonev> kegiatanMonevs = new ArrayList<>();

    // BAST


    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanBastRehabDas> bastReboRehabs = new ArrayList<>();

    @Column(name = "nomor_bast_ppkh_ke_dirjen")
    private String nomorBastPpkhKeDirjen;

    @Column(name = "tanggal_bast_ppkh_ke_dirjen")
    private LocalDate tanggalBastPpkhKeDirjen;

    @Column(name = "nomor_bast_dirjen_ke_dishut")
    private String nomorBastDirjenKeDishut;

    @Column(name = "tanggal_bast_dirjen_ke_dishut")
    private LocalDate tanggalBastDirjenKeDishut;

    @Column(name = "nomor_bast_dishut_ke_pengelola")
    private String nomorBastDishutKePengelola;

    @Column(name = "tanggal_bast_dishut_ke_pengelola")
    private LocalDate tanggalBastDishutKePengelola;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanBastZip> kegiatanBastZips = new ArrayList<>();

    @Column(name = "nama")
    private String namaPic;

    @Column(name = "nomor")
    private String nomorPic;

    @Column(name = "email")
    private String emailPic;

}
