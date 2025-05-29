package com.kehutanan.superadmin.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "mst_kelompok_masyarakat")
@NoArgsConstructor
@AllArgsConstructor
public class KelompokMasyarakat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_kelompok_masyarakat", length = 255)
    private String namaKelompokMasyarakat;

    @Column(name = "nomor_sk_penetapan", length = 255)
    private String nomorSkPenetapan;

    @Column(name = "tanggal_sk_penetapan")
    private LocalDate tanggalSkPenetapan;

    @Column(name = "alamat", columnDefinition = "TEXT")
    private String alamat;

    @Column(name = "pic", length = 255)
    private String pic;

    @Column(name = "telepon")
    private String telepon;

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