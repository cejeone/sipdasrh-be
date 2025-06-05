package com.kehutanan.rm.serahterima.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.kehutanan.rm.master.model.Bpdas;
import com.kehutanan.rm.master.model.Lov;
import com.kehutanan.rm.master.model.Provinsi;
import com.kehutanan.rm.program.model.Program;

@Data
@Entity
@Table(name = "trx_rm_bast_pusat")
@NoArgsConstructor
@AllArgsConstructor
public class BastPusat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program programId;
    
    @ManyToOne
    @JoinColumn(name = "bpdas_id",referencedColumnName = "id")  
    private Bpdas bpdasId;

    @ManyToOne
    @JoinColumn(name = "provinsi_id", referencedColumnName = "id")
    private Provinsi provinsiId;

    @ManyToOne
    @JoinColumn(name = "fungsi_kawasan_id", referencedColumnName = "id")
    private Lov fungsiKawasan;

    private Integer targetLuas;

    private Integer realisasiLuas;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
    
}