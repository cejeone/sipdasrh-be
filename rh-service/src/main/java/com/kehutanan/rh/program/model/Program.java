package com.kehutanan.rh.program.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "rh_program")
public class Program {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String kategori;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String nama;
    
    @Column(name = "tahun_pelaksanaan", nullable = false)
    private Integer tahunPelaksanaan;
    
    @Column(name = "total_anggaran", precision = 19, scale = 2)
    private BigDecimal totalAnggaran;
    
    @Column(name = "target_luas")
    private Integer targetLuas;
    
    @Column(nullable = false)
    private String status;
}