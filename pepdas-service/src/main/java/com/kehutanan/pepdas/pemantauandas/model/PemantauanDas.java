package com.kehutanan.pepdas.pemantauandas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_pemantauan_das")
@NoArgsConstructor
@AllArgsConstructor
public class PemantauanDas {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "bpdas", nullable = false)
    private String bpdas;
    
    @Column(name = "das", nullable = false)
    private String das;
    
    @Column(name = "spas_id", nullable = false)
    private String spasId;
    
    @Column(name = "tanggal_waktu", nullable = false)
    private LocalDateTime tanggalWaktu;
    
    @Column(name = "nilai_tma")
    private Double nilaiTma;
    
    @Column(name = "nilai_curah_hujan")
    private Double nilaiCurahHujan;
    
    @Column(name = "tegangan_baterai")
    private Double teganganBaterai;
}