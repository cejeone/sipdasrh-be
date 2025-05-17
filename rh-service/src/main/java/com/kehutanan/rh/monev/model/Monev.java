package com.kehutanan.rh.monev.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Entity
@Table(name = "rh_monev")
@NoArgsConstructor
@AllArgsConstructor
public class Monev {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String program;
    
    @Column(nullable = false)
    private String bpdas;
    
    @Column(name = "total_target")
    private Integer totalTarget;
    
    @Column(name = "total_realisasi")
    private Integer totalRealisasi;
    
    @Column(name = "total_t1")
    private Integer totalT1;
    
    @Column(name = "realisasi_t1")
    private Integer realisasiT1;
    
    @Column(name = "total_p0")
    private Integer totalP0;
    
    @Column(name = "realisasi_p0")
    private Integer realisasiP0;
    
    @Column(name = "total_p1")
    private Integer totalP1;
    
    @Column(name = "realisasi_p1")
    private Integer realisasiP1;
    
    @Column(name = "total_p2")
    private Integer totalP2;
    
    @Column(name = "realisasi_p2")
    private Integer realisasiP2;
    
    @Column(name = "total_bast")
    private Integer totalBast;
    
    @Column(name = "realisasi_bast")
    private Integer realisasiBast;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
}