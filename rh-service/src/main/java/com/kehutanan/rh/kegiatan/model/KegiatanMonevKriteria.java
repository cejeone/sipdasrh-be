package com.kehutanan.rh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.rh.master.model.Lov;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "trx_rh_kegiatan_monev_kriteria")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevKriteria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "kegiatan_monev_id", referencedColumnName = "id")
    private KegiatanMonev kegiatanMonev;
    
    @ManyToOne
    @JoinColumn(name = "aktivitas_id", referencedColumnName = "id")
    private Lov aktivitasId;
    
    @Column(name = "target_luas")
    private Double targetLuas;
    
    @Column(name = "realisasi_luas")
    private Double realisasiLuas;
    
    private String catatan;
}