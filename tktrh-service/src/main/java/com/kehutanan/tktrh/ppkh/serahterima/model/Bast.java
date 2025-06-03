package com.kehutanan.tktrh.ppkh.serahterima.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.ppkh.program.model.Program;

@Data
@Entity
@Table(name = "trx_ppkh_bast")
@NoArgsConstructor
@AllArgsConstructor
public class Bast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program programId;

    @Column(name = "tahun")
    private Integer tahun;

    @ManyToOne
    @JoinColumn(name = "fungsi_kawasan_id", referencedColumnName = "id")
    private Lov fungsiKawasan;

    private Integer luas;

    @Column(name = "tahapan_rantek")
    private Integer tahapanRantek;

    @ManyToOne
    @JoinColumn(name = "status_serah_terima", referencedColumnName = "id")
    private Lov statusSerahTerima;

}