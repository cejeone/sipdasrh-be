package com.kehutanan.pepdas.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.pepdas.master.model.Bpdas;
import com.kehutanan.pepdas.master.model.Das;
import com.kehutanan.pepdas.master.model.Eselon2;
import com.kehutanan.pepdas.master.model.KabupatenKota;
import com.kehutanan.pepdas.master.model.Kecamatan;
import com.kehutanan.pepdas.master.model.KelurahanDesa;
import com.kehutanan.pepdas.master.model.Lov;
import com.kehutanan.pepdas.master.model.Provinsi;
import com.kehutanan.pepdas.program.model.Program;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_pepdas_kegiatan")
@NoArgsConstructor
@AllArgsConstructor
public class Kegiatan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eselon_2_id", referencedColumnName = "id")
    private Eselon2 eselon2;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "jenis_kegiatan_id", referencedColumnName = "id")
    private Lov jenisKegiatan;

    @Column(name = "nama_kegiatan", columnDefinition = "TEXT")
    private String namaKegiatan;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdas;

    @ManyToOne
    @JoinColumn(name = "das_id", referencedColumnName = "id")
    private Das das;

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
    @JoinColumn(name = "kelurahan_desa_id", referencedColumnName = "id")
    private KelurahanDesa kelurahanDesa;

    @Column(name = "alamat", columnDefinition = "TEXT")
    private String alamat;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanLokusShp> kegiatanLokusShps = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "skema_id", referencedColumnName = "id")
    private Lov skema;

    @Column(name = "tahun_kegiatan")
    private Integer tahunKegiatan;

    @ManyToOne
    @JoinColumn(name = "sumber_anggaran_id", referencedColumnName = "id")
    private Lov sumberAnggaran;

    @ManyToOne
    @JoinColumn(name = "penerima_manfaat_kegiatan_id", referencedColumnName = "id")
    private Lov penerimaManfaatKegiatan;

    @ManyToOne
    @JoinColumn(name = "pelaksana_id", referencedColumnName = "id")
    private Lov detailPelaksana;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisPdf> kegiatanRancanganTeknisPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisFoto> kegiatanRancanganTeknisFotos = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisVideo> kegiatanRancanganTeknisVideos = new ArrayList<>();

    @Column(name = "nomor_kontrak")
    private String nomorKontrak;

    @Column(name = "nilai_kontrak")
    private Integer nilaiKontrak;

    @ManyToOne
    @JoinColumn(name = "tipe_id", referencedColumnName = "id")
    private Lov tipe;

    @ManyToOne
    @JoinColumn(name = "penerima_manfaat_kontrak_id", referencedColumnName = "id")
    private Lov penerimaManfaatKontrak;

    @Column(name = "tanggal_kontrak")
    private LocalDate tanggalKontrak;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanKontrakPdf> kegiatanKontrakPdfs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

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

}
