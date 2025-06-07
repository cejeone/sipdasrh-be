package com.kehutanan.tktrh.ppkh.kegiatan.model;

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

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "masterKegiatan")
@Table(name = "trx_ppkh_kegiatan")
@NoArgsConstructor
@AllArgsConstructor
public class Kegiatan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "sub_direktorat_id", referencedColumnName = "id")
    private Eselon3 subDirektorat;

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
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @ManyToOne
    @JoinColumn(name = "pemegang_ijin_id", referencedColumnName = "id")
    private Lov pemegangIjinId;

    @ManyToOne
    @JoinColumn(name = "peruntukan_id", referencedColumnName = "id")
    private Lov peruntukanId;

    @Column(name = "nama_peruntukan", columnDefinition = "TEXT")
    private String namaPeruntukan;

    @ManyToOne
    @JoinColumn(name = "jenis_ijin_id", referencedColumnName = "id")
    private Lov jenisIjinId;

    @Column(name = "nomor_sk_perijinan")
    private String nomorSkPerijinan;

    @Column(name = "tanggal_sk_perijinan")
    private LocalDate tanggalSkPerijinan;

    @Column(name = "tanggal_berakhir_sk_perijinan")
    private LocalDate tanggalBerakhirSkPerijinan;

    @Column(name = "luas_sesuai_sk_perijinan_ha")
    private Integer luasSesuaiSkPerijinanHa;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPerijinanPdf> kegiatanPerijinanPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRiwayatSk> RiwayatSks = new ArrayList<>();

    @Column(name = "nomor_sk_pak")
    private String nomorSkPak;

    @Column(name = "tanggal_sk_pak")
    private LocalDate tanggalSkPak;

    @Column(name = "tanggal_berakhir_sk_pak")
    private LocalDate tanggalBerakhirSkPak;

    @Column(name = "luas_yang_ditetapkan_sesuai_sk_pak_ha")
    private Integer luasSesuaiSkPakHa;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPakPdfShp> kegiatanPakPdfShps = new ArrayList<>();

    @Column(name = "nomor_sk_rehabilitasi")
    private String nomorSkRehabilitasi;

    @Column(name = "tanggal_sk_rehabilitasi")
    private LocalDate tanggalSkRehabilitasi;

    @Column(name = "tanggal_berakhir_sk_rehabilitasi")
    private LocalDate tanggalBerakhirSkRehabilitasi;

    @Column(name = "luas_sk_rehab_das")
    private Integer luasSkRehabDas;

    @ManyToOne
    @JoinColumn(name = "bpdas_rehab", referencedColumnName = "id")
    private Bpdas bpdasRehab;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanFungsiKawasan> FungsiKawasans = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRantekPdf> kegiatanRantekPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRencanaRealisasi> rencanaRealisasis = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanBastReboRehab> bastReboRehabs = new ArrayList<>();

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

    @Column(name = "nama_pic")
    private String namaPic;

    @Column(name = "nomor_pic")
    private String nomorPic;

    @Column(name = "email_pic")
    private String emailPic;

}
