package com.kehutanan.rh.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Entity class representing the mst_institusi table
 * This table stores information about institutions related to the forestry system
 */
@Data
@Entity
@Table(name = "mst_institusi")
@NoArgsConstructor
@AllArgsConstructor
public class Institusi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama", length = 255)
    private String nama;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "nomor_telepon", length = 50)
    private String nomorTelepon;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "kode_pos", length = 20)
    private String kodePos;

     @ManyToOne
    @JoinColumn(name = "tipe_institusi_id", referencedColumnName = "id")
    private Lov tipeInstitusi;

    @ManyToOne
    @JoinColumn(name = "tipe_akreditasi_id", referencedColumnName = "id")
    private Lov tipeAkreditasi;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @ManyToOne
    @JoinColumn(name = "provinsi_id", referencedColumnName = "id")
    private Provinsi provinsi;

    @ManyToOne
    @JoinColumn(name = "kabupaten_kota_id", referencedColumnName = "id")
    private KabupatenKota kabupatenKota;

    @ManyToOne
    @JoinColumn(name = "kecamatan_id", referencedColumnName = "id")
    private Kecamatan kecamatan;
}