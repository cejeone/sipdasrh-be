package com.kehutanan.tktrh.ppkh.serahterima.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.Provinsi;
import com.kehutanan.tktrh.ppkh.program.model.Program;

@Data
@Entity(name = "ppkhBastPusat")
@Table(name = "trx_ppkh_bast_pusat")
@NoArgsConstructor
@AllArgsConstructor
public class BastPusat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "program_id",referencedColumnName = "id")  
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