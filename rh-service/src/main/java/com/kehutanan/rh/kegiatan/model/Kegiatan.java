package com.kehutanan.rh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.rh.program.model.Program;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "rh_kegiatan")
@NoArgsConstructor
@AllArgsConstructor
public class Kegiatan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sub_direktorat", nullable = false)
    private String subDirektorat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(name = "jenis_kegiatan", nullable = false)
    private String jenisKegiatan;

    @Column(name = "ref_po")
    private String refPo;

    @Column(name = "nama_kegiatan", nullable = false)
    private String namaKegiatan;

    @Column(name = "detail_pola")
    private String detailPola;

    @Column(name = "detail_tahun_kegiatan")
    private Integer detailTahunKegiatan;

    @Column(name = "detail_sumber_anggaran")
    private String detailSumberAnggaran;

    @Column(name = "detail_total_bibit")
    private Integer detailTotalBibit;

    @Column(name = "detail_total_luas_ha")
    private Integer detailTotalLuasHa;

    @Column(name = "detail_pemangku_kawasan")
    private String detailPemangkuKawasan;

    @Column(name = "detail_pelaksanaan")
    private String detailPelaksanaan;

    @Column(name = "kontrak_nomor")
    private String kontrakNomor;

    @Column(name = "kontrak_nilai")
    private Integer kontrakNilai;

    @Column(name = "kontrak_tipe")
    private String kontrakTipe;

    @Column(name = "kontrak_pelaksanaan")
    private String kontrakPelaksanaan;

    @Column(name = "kontrak_tanggal_kontrak")
    private LocalDate kontrakTanggalKontrak;

    @Column(name = "kontrak_status")
    private String kontrakStatus;

    @Column(name = "dokumentasi_catatan_foto", columnDefinition = "TEXT")
    private String dokumentasiCatatanFoto;

    @Column(name = "dokumentasi_catatan_video", columnDefinition = "TEXT")
    private String dokumentasiCatatanVideo;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanLokus> lokusList = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPemeliharaanSulaman> sulamanList = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPemeliharaanPemupukan> pemupukanList = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanSerahTerima> serahTerimaList = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanMonev> monevList = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisPdf> kegiatanRancanganTeknisPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisFoto> kegiatanRancanganTeknisFotos = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisVideo> kegiatanRancanganTeknisVideos = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanKontrakPdf> kegiatanKontrakPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanDokumentasiFoto> dokumentasiFotos = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanDokumentasiVideo> dokumentasiVideos = new ArrayList<>();

}