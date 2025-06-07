package com.kehutanan.tktrh.bkta.program.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;

import com.kehutanan.tktrh.master.model.Lov;

/**
 * Entity class representing the trx_bkta_program_pagu_anggaran table
 * This table stores information about budget allocations for BKTA programs
 */
@Data
@Entity(name = "bktaProgramPaguAnggaran")
@Table(name = "trx_bkta_program_pagu_anggaran")
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguAnggaran implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "sumber_anggaran_id", referencedColumnName = "id")
    private Lov sumberAnggaran;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @Column(name = "tahun_anggaran")
    private Integer tahunAnggaran;

    @Column(name = "pagu")
    private Double pagu;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
}