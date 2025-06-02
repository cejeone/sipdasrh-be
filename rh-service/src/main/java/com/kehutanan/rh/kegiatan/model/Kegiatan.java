package com.kehutanan.rh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.rh.master.model.Eselon3;
import com.kehutanan.rh.master.model.Lov;
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
@Table(name = "trx_rh_kegiatan")
@NoArgsConstructor
@AllArgsConstructor
public class Kegiatan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sub_direktorat_id", referencedColumnName = "id")
    private Eselon3 Eselon3;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    @JsonBackReference
    private Program program;

    @ManyToOne
    @JoinColumn(name = "jenis_kegiatan_id", referencedColumnName = "id")
    private Lov jenisKegiatanId;

    @ManyToOne
    @JoinColumn(name = "referensi_p0_id", referencedColumnName = "id")
    private Lov referensiP0Id;

    @ManyToOne
    @JoinColumn(name = "pola_id", referencedColumnName = "id")
    private Lov polaId;

    @ManyToOne
    @JoinColumn(name = "sumber_anggaran_id", referencedColumnName = "id")
    private Lov sumberAnggaranId;

    @ManyToOne
    @JoinColumn(name = "pelaksana_id", referencedColumnName = "id")
    private Lov pelaksanaId;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanFungsiKawasan> fungsiKawasans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "tipe_id", referencedColumnName = "id")
    private Lov tipeId;

    @ManyToOne
    @JoinColumn(name = "penerima_manfaat_id", referencedColumnName = "id")
    private Lov penerimaManfaatId;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPemeliharaanSulaman> sulamanList = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPemeliharaanPemupukan> pemupukanList = new ArrayList<>();

    @Column(name = "pemangku_kawasan")
    private String pemangkuKawasan;

    @Column(name = "nama_kegiatan")
    private String namaKegiatan;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanLokus> kegiatanLokus = new ArrayList<>();

    @Column(name = "tahun_kegiatan")
    private Integer tahunKegiatan;

    @Column(name = "total_bibit")
    private Integer totalBibit;

    @Column(name = "total_luas")
    private Double totalLuas;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisPdf> kegiatanRancanganTeknisPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisFoto> kegiatanRancanganTeknisFotos = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisVideo> kegiatanRancanganTeknisVideos = new ArrayList<>();

    @Column(name = "nomor")
    private String nomor;

    @Column(name = "nilai")
    private Double nilai;

    @Column(name = "tanggal_kontrak")
    private String tanggalKontrak;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanKontrakPdf> kegiatanKontrakPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanDokumentasiFoto> kegiatanDokumentasiFotos = new ArrayList<>();

    @Column(name = "dokumentasi_catatan_foto", columnDefinition = "TEXT")
    private String dokumentasiCatatanFoto;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanDokumentasiVideo> kegiatanDokumentasiVideos = new ArrayList<>();

    @Column(name = "dokumentasi_catatan_video", columnDefinition = "TEXT")
    private String dokumentasiCatatanVideo;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanMonev> monevList = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanBast> serahTerimaList = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL)
    // private List<KegiatanFungsiKawasan> fungsiKawasans = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL)
    // private List<KegiatanLokus> lokuses = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL)
    // private List<KegiatanSulaman> sulamans = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL)
    // private List<KegiatanPemupukan> pemupukans = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL)
    // private List<KegiatanMonev> monevs = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL)
    // private List<KegiatanBast> basts = new ArrayList<>();

    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // private UUID id;

    // @Column(name = "sub_direktorat", nullable = false)
    // private String subDirektorat;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "program_id", referencedColumnName = "id"))
    // private Program program;

    // @Column(name = "jenis_kegiatan", nullable = false)
    // private String jenisKegiatan;

    // @Column(name = "ref_po")
    // private String refPo;

    // @Column(name = "nama_kegiatan", nullable = false)
    // private String namaKegiatan;

    // @Column(name = "detail_pola")
    // private String detailPola;

    // @Column(name = "detail_tahun_kegiatan")
    // private Integer detailTahunKegiatan;

    // @Column(name = "detail_sumber_anggaran")
    // private String detailSumberAnggaran;

    // @Column(name = "detail_total_bibit")
    // private Integer detailTotalBibit;

    // @Column(name = "detail_total_luas_ha")
    // private Integer detailTotalLuasHa;

    // @Column(name = "detail_pemangku_kawasan")
    // private String detailPemangkuKawasan;

    // @Column(name = "detail_pelaksana")
    // private String detailPelaksana;

    // @Column(name = "kontrak_nomor")
    // private String kontrakNomor;

    // @Column(name = "kontrak_nilai")
    // private Integer kontrakNilai;

    // @Column(name = "kontrak_tipe")
    // private String kontrakTipe;

    // @Column(name = "kontrak_pelaksana")
    // private String kontrakPelaksana;

    // @Column(name = "kontrak_tanggal_kontrak")
    // private LocalDate kontrakTanggalKontrak;

    // @Column(name = "kontrak_status")
    // private String kontrakStatus;

    // @Column(name = "dokumentasi_catatan_foto", columnDefinition = "TEXT")
    // private String dokumentasiCatatanFoto;

    // @Column(name = "dokumentasi_catatan_video", columnDefinition = "TEXT")
    // private String dokumentasiCatatanVideo;

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanLokus> kegiatanLokus = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanFungsiKawasan> fungsiKawasans = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanPemeliharaanSulaman> sulamanList = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanPemeliharaanPemupukan> pemupukanList = new
    // ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanSerahTerima> serahTerimaList = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanMonev> monevList = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanRancanganTeknisPdf> kegiatanRancanganTeknisPdfs = new
    // ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanRancanganTeknisFoto> kegiatanRancanganTeknisFotos = new
    // ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanRancanganTeknisVideo> kegiatanRancanganTeknisVideos =
    // new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanKontrakPdf> kegiatanKontrakPdfs = new ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanDokumentasiFoto> kegiatanDokumentasiFotos = new
    // ArrayList<>();

    // @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // @JsonManagedReference
    // private List<KegiatanDokumentasiVideo> kegiatanDokumentasiVideos = new
    // ArrayList<>();

}