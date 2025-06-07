package com.kehutanan.ppth.penghijauan.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.ppth.master.model.Lov;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "penghijauanKegiatanMonevKriteria")
@Table(name = "trx_ppth_kegiatan_penghijauan_monev_kriteria")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevKriteria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "kegiatan_monev_id", referencedColumnName = "id")
    @JsonBackReference
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