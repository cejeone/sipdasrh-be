package com.kehutanan.tktrh.bkta.kegiatan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.master.model.Eselon3;
import com.kehutanan.tktrh.master.model.Lov;

/**
 * Entity class representing the trx_bkta_kegiatan table
 * This table stores information about BKTA activities
 */
@Data
@Entity
@Table(name = "trx_bkta_kegiatan")
@NoArgsConstructor
@AllArgsConstructor
public class Kegiatan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sub_direktorat_id", referencedColumnName = "id")
    private Eselon3 subDirektorat;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private Program program;

    @Column(name = "nama_kegiatan", columnDefinition = "TEXT")
    private String namaKegiatan;

    @ManyToOne
    @JoinColumn(name = "sumber_anggaran_id", referencedColumnName = "id")
    private Lov sumberAnggaran;

    @Column(name = "total_anggaran")
    private Double totalAnggaran;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanLokus> lokus = new ArrayList<>();

    @Column(name = "fungsi_kawasan_hk")
    private Boolean fungsiKawasanHk;

    @Column(name = "fungsi_kawasan_hl")
    private Boolean fungsiKawasanHl;

    @Column(name = "fungsi_kawasan_apl")
    private Boolean fungsiKawasanApl;

    @Column(name = "fungsi_kawasan_hpt")
    private Boolean fungsiKawasanHpt;

    @ManyToOne
    @JoinColumn(name = "skema_id", referencedColumnName = "id")
    private Lov skema;

    @Column(name = "tahun_kegiatan", columnDefinition = "TEXT")
    private String tahunKegiatan;

    @Column(name = "total_dpn")
    private Double totalDpn;

    @Column(name = "total_gully_plug")
    private Double totalGullyPlug;

    @Column(name = "pemangku_kawasan", columnDefinition = "TEXT")
    private String pemangkuKawasan;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisPdf> kegiatanRancanganTeknisPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisFoto> kegiatanRancanganTeknisFotos = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRancanganTeknisShp> kegiatanRancanganTeknisShps = new ArrayList<>();

    @Column(name = "nomor", columnDefinition = "TEXT")
    private String nomor;

    @Column(name = "nilai", columnDefinition = "TEXT")
    private String nilai;

    @Column(name = "tanggal_kontrak")
    private Date tanggalKontrak;

    @Column(name = "tanggal_berakhir_kontrak")
    private Date tanggalBerakhirKontrak;

    @ManyToOne
    @JoinColumn(name = "pelaksana_id", referencedColumnName = "id")
    private Lov pelaksana;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanKontrakPdf> kegiatanKontrakPdfs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanDokumentasiFoto> dokumentasiFotos = new ArrayList<>();

        @Column(name = "catatan_dokumentasi_foto", columnDefinition = "TEXT")
    private String catatanDokumentasiFoto;

    
    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanDokumentasiVideo> dokumentasiVideos = new ArrayList<>();

        @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanMonev> monevs = new ArrayList<>();


    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanBast> basts = new ArrayList<>();


}