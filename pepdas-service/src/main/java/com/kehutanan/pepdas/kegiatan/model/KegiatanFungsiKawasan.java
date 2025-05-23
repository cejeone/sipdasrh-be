package com.kehutanan.pepdas.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_kegiatan_fungsi_kawasan")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanFungsiKawasan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "fungsi_kawasan", nullable = false)
    private String fungsiKawasan;
    
    @Column(name = "target_luas_ha")
    private Integer targetLuasHa;
    
    @Column(name = "realisasi_luas")
    private Integer realisasiLuas;
    
    private Integer tahun;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    private String status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_id", nullable = false)
    @JsonBackReference
    private Kegiatan kegiatan;
}