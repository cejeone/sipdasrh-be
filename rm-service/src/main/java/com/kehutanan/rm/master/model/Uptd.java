package com.kehutanan.rm.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing the mst_uptd table
 * This table stores information about Regional Technical Implementation Units (UPTD)
 * responsible for forestry activities
 */
@Data
@Entity
@Table(name = "mst_uptd")
@NoArgsConstructor
@AllArgsConstructor
public class Uptd implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "telepon", length = 50)
    private String telepon;

    @Column(name = "sertifikasi_sumber_benih")
    private Boolean sertifikasiSumberBenih;

    @Column(name = "sertifikasi_mutu_benih")
    private Boolean sertifikasiMutuBenih;

    @Column(name = "sertifikasi_mutu_bibit")
    private Boolean sertifikasiMutuBibit;

    @Column(name = "jumlah_asesor_sumber_benih")
    private Integer jumlahAsesorSumberBenih;

    @Column(name = "jumlah_asesor_mutu_benih")
    private Integer jumlahAsesorMutuBenih;

    @Column(name = "jumlah_asesor_mutu_bibit")
    private Integer jumlahAsesorMutuBibit;

    @Column(name = "nomor_sk_penetapan", length = 255)
    private String nomorSkPenetapan;

    @Column(name = "tanggal")
    private LocalDateTime tanggal;

    @Column(name = "nama_kontak", length = 255)
    private String namaKontak;

    @Column(name = "nomor_telepon_kontak", length = 50)
    private String nomorTeleponKontak;

    @OneToMany(mappedBy = "uptd", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UptdRantekPdf> uptdRantekPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "uptd", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UptdDokumentasiFoto> uptdDokumentasiFotos = new ArrayList<>();

    @OneToMany(mappedBy = "uptd", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UptdDokumentasiVideo> uptdDokumentasiVideos = new ArrayList<>();

    @Column(name = "catatan_dokumen", columnDefinition = "TEXT")
    private String catatanDokumen;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    @Column(name = "lintang", precision = 11, scale = 6)  // Increase precision for latitude
    private BigDecimal lintang;

    @Column(name = "bujur", precision = 11, scale = 6)    // Increase precision for longitude
    private BigDecimal bujur;

    @Column(name = "luas", precision = 15, scale = 2)     // Increase precision for area
    private BigDecimal luas;

    @OneToMany(mappedBy = "uptd", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UptdPetaShp> uptdPetaShps = new ArrayList<>();

    @Column(name = "nama_uptd", length = 255)
    private String namaUptd;

    @ManyToOne
    @JoinColumn(name = "kode_bpdas_id", referencedColumnName = "id")
    private Bpdas bpdas;

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
}