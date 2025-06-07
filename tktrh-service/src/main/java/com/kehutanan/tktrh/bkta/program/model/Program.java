package com.kehutanan.tktrh.bkta.program.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.tktrh.master.model.Eselon2;
import com.kehutanan.tktrh.master.model.Lov;

/**
 * Entity class representing the trx_bkta_program table
 * This table stores information about BKTA programs
 */
@Data
@Entity(name = "bktaProgram")
@Table(name = "trx_bkta_program")
@NoArgsConstructor
@AllArgsConstructor
public class Program implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eselon2_id", referencedColumnName = "id")
    private Eselon2 eselon2;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @Column(name = "nama", columnDefinition = "TEXT")
    private String nama;

    @Column(name = "tahun_pelaksana")
    private Integer tahunPelaksana;

    @Column(name = "total_anggaran")
    private Double totalAnggaran;

    @Column(name = "target_gp")
    private Integer targetGp;

    @Column(name = "target_dpn")
    private Integer targetDpn;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramPaguAnggaran> paguAnggarans = new ArrayList<>();

}