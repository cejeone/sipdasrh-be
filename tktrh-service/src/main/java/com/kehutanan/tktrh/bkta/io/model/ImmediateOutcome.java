package com.kehutanan.tktrh.bkta.io.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;

import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.KabupatenKota;
import com.kehutanan.tktrh.master.model.Kecamatan;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.Provinsi;

/**
 * Entity class representing the trx_bkta_io table
 * This table stores information about immediate outcomes of BKTA activities
 */
@Data
@Entity
@Table(name = "trx_bkta_io")
@NoArgsConstructor
@AllArgsConstructor
public class ImmediateOutcome implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
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

    @Column(name = "dta", columnDefinition = "TEXT")
    private String dta;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @ManyToOne
    @JoinColumn(name = "kondisi_id", referencedColumnName = "id")
    private Lov kondisi;

    @ManyToOne
    @JoinColumn(name = "efektivitas_id", referencedColumnName = "id")
    private Lov efektivitas;

    @ManyToOne
    @JoinColumn(name = "rancangan_teknis_id", referencedColumnName = "id")
    private Lov rancanganTeknis;

    @Column(name = "nama_kegiatan", columnDefinition = "TEXT")
    private String namaKegiatan;

    @Column(name = "tahun_kegiatan")
    private Integer tahunKegiatan;

    @Column(name = "spatial_x")
    private Double spatialX;

    @Column(name = "spatial_y")
    private Double spatialY;

    @Column(name = "volume_sedimen")
    private Double volumeSedimen;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
}