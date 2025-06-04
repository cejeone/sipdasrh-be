package com.kehutanan.rm.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.rm.master.model.Eselon3;
import com.kehutanan.rm.master.model.KabupatenKota;
import com.kehutanan.rm.master.model.Kecamatan;
import com.kehutanan.rm.master.model.KelurahanDesa;
import com.kehutanan.rm.master.model.Lov;
import com.kehutanan.rm.master.model.Provinsi;
import com.kehutanan.rm.master.model.UptdPetaShp;
import com.kehutanan.rm.program.model.Program;
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
@Table(name = "trx_rm_kegiatan")
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

    @Column(name = "nama_kegiatan")
    private String namaKegiatan;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Lov bpdasId;

    @ManyToOne
    @JoinColumn(name = "das_id", referencedColumnName = "id")
    private Lov dasId;

    @ManyToOne
    @JoinColumn(name = "provinsi_id", referencedColumnName = "id")
    private Provinsi provinsiId;

    @ManyToOne
    @JoinColumn(name = "kabupaten_kota_id", referencedColumnName = "id")
    private KabupatenKota kabupatenKota;

    @ManyToOne
    @JoinColumn(name = "kecamatan_id", referencedColumnName = "id")
    private Kecamatan kecamatanId;

    @ManyToOne
    @JoinColumn(name = "kelurahan_desa_id", referencedColumnName = "id")
    private KelurahanDesa kelurahanDesaId;

    @Column(name = "alamat", columnDefinition = "TEXT")
    private String alamat;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanLokusShp> kegiatanLokusShps = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "fungsi_kawasan_id", referencedColumnName = "id")
    private Lov fungsiKawasanId;

    @ManyToOne
    @JoinColumn(name = "skema_id", referencedColumnName = "id")
    private Lov skemaId;

    @Column(name = "tahun_kegiatan")
    private Integer tahunKegiatan;

    @ManyToOne
    @JoinColumn(name = "sumber_anggaran_id", referencedColumnName = "id")
    private Lov sumberAnggaranId;

    @Column(name = "total_bibit")
    private Integer totalBibit;

    @Column(name = "total_luas")
    private Double totalLuas;

    @ManyToOne
    @JoinColumn(name = "penerima_manfaat_id", referencedColumnName = "id")
    private Lov penerimaManfaatId;

    @ManyToOne
    @JoinColumn(name = "pelaksana_id", referencedColumnName = "id")
    private Lov pelaksanaId;

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

    @ManyToOne
    @JoinColumn(name = "tipe_id", referencedColumnName = "id")
    private Lov tipeId;

    @ManyToOne
    @JoinColumn(name = "penerima_manfaat_kegiatan_id", referencedColumnName = "id")
    private Lov penerimaManfaatKegiatanId;

    @Column(name = "tanggal_kontrak")
    private String tanggalKontrak;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanKontrakPdf> kegiatanKontrakPdfs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPemeliharaanSulaman> sulamanList = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanPemeliharaanPemupukan> pemupukanList = new ArrayList<>();

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



}