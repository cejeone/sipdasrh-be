package com.kehutanan.tktrh.bkta.monev.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;

import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.master.model.Bpdas;

/**
 * Entity class representing the trx_bkta_monev_pusat table
 * This table stores information about central monitoring and evaluation of BKTA programs
 */
@Data
@Entity(name = "bktaMonevPusat")
@Table(name = "trx_bkta_monev_pusat")
@NoArgsConstructor
@AllArgsConstructor
public class MonevPusat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdas;

    @Column(name = "target_total_dpn")
    private Integer targetTotalDpn;

    @Column(name = "target_total_gp")
    private Integer targetTotalGp;

    @Column(name = "target_tambahan_dpn")
    private Integer targetTambahanDpn;

    @Column(name = "target_tambahan_gp")
    private Integer targetTambahanGp;

    @Column(name = "realisasi_rantek_dpn")
    private Integer realisasiRantekDpn;

    @Column(name = "realisasi_rantek_gp")
    private Integer realisasiRantekGp;

    @Column(name = "realisasi_spks_dpn")
    private Integer realisasiSpksDpn;

    @Column(name = "realisasi_spks_gp")
    private Integer realisasiSpksGp;

    @Column(name = "realisasi_fisik_dpn")
    private Integer realisasiFisikDpn;

    @Column(name = "realisasi_fisik_gp")
    private Integer realisasiFisikGp;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
}