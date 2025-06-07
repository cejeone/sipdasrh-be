package com.kehutanan.tktrh.bkta.kegiatan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Entity class representing the trx_bkta_kegiatan_monev_kriteria table
 * This table stores information about criteria for monitoring and evaluation of BKTA activities
 */
@Data
@Entity(name = "bktaKegiatanMonevKriteria")
@Table(name = "trx_bkta_kegiatan_monev_kriteria")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevKriteria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "monitoring_evaluasi_id", referencedColumnName = "id")
    private KegiatanMonev monitoringEvaluasi;

    @Column(name = "aktivitas", columnDefinition = "TEXT")
    private String aktivitas;

    @Column(name = "target", columnDefinition = "TEXT")
    private String target;

    @Column(name = "realisasi", columnDefinition = "TEXT")
    private String realisasi;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;
}