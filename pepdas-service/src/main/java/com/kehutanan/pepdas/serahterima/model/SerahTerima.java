package com.kehutanan.pepdas.serahterima.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_serah_terima")
public class SerahTerima {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "program")
    private String program;
    
    @Column(name = "bpdas")
    private String bpdas;
    
    @Column(name = "provinsi")
    private String provinsi;
    
    @Column(name = "fungsi_kawasan")
    private String fungsiKawasan;
    
    @Column(name = "realisasi_luas")
    private String realisasiLuas;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
}