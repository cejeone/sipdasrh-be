package com.kehutanan.rm.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Entity class representing the mst_pelaku_usaha table
 * This table stores information about business entities
 */
@Data
@Entity
@Table(name = "mst_pelaku_usaha")
@NoArgsConstructor
@AllArgsConstructor
public class PelakuUsaha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_badan_usaha", length = 255)
    private String namaBadanUsaha;

    @Column(name = "nomor_induk_berusaha_nib")
    private String nomorIndukBerusahaNib;

    @Column(name = "nomor_sertifikat_standar")
    private String nomorSertifikatStandar;

    @Column(name = "ruang_lingkup_usaha", length = 255)
    private String ruangLingkupUsaha;

    @Column(name = "nama_direktur", length = 255)
    private String namaDirektur;

    @Column(name = "nomor_hp_direktur")
    private String nomormpDirektur;

    @Column(name = "alamat", columnDefinition = "TEXT")
    private String alamat;

    @ManyToOne
    @JoinColumn(name = "kategori_pelaku_usaha_id", referencedColumnName = "id")
    private Lov kategoriPelakuUsaha;

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