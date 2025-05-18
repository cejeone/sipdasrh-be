package com.kehutanan.rh.bimtek.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "rh_bimtek")
@NoArgsConstructor
@AllArgsConstructor
public class Bimtek {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "nama_bimtek", nullable = false)
    private String namaBimtek;
    
    @Column(nullable = false)
    private String subjek;
    
    @Column(nullable = false)
    private String program;
    
    @Column(nullable = false)
    private String bpdas;
    
    @Column(nullable = false)
    private String tempat;
    
    @Column(nullable = false)
    private LocalDate tanggal;
    
    @Column(nullable = false)
    private String audience;
    
    private String evaluasi;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
}