package com.kehutanan.tktrh.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entity class representing the mst_spas table
 * This table stores information about River Water Monitoring Stations (SPAS)
 * used in forest and land rehabilitation activities
 */
@Data
@Entity(name = "mstSpas")
@Table(name = "mst_spas")
@NoArgsConstructor
@AllArgsConstructor
public class Spas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spas", length = 255)
    private String spas;

    @Column(name = "keterangan", length = 255)
    private String keterangan;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "lintang", precision = 11, scale = 6)  // Increase precision for latitude
    private BigDecimal lintang;

    @Column(name = "bujur", precision = 11, scale = 6)    // Increase precision for longitude
    private BigDecimal bujur;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdas;

    @ManyToOne
    @JoinColumn(name = "das_id", referencedColumnName = "id")
    private Das das;

    @ManyToOne
    @JoinColumn(name = "tipe_spas_id", referencedColumnName = "id")
    private Lov tipeSpas;

    @ManyToOne
    @JoinColumn(name = "frekuensi_pengiriman_data_id", referencedColumnName = "id")
    private Lov frekuensiPengirimanData;

    @ManyToOne
    @JoinColumn(name = "kanal_data_id", referencedColumnName = "id")
    private Lov kanalData;

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

    @ManyToOne
    @JoinColumn(name = "kelurahan_desa_id", referencedColumnName = "id")
    private KelurahanDesa kelurahanDesa;
}