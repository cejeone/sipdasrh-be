package com.kehutanan.pepdas.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Entity class representing the mst_bpth table
 * This table stores information about Forest Plant Nursery Centers (BPTH)
 */
@Data
@Entity
@Table(name = "mst_bpth")
@NoArgsConstructor
@AllArgsConstructor
public class Bpth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_bpth", length = 255)
    private String namaBpth;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "telepon", length = 50)
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