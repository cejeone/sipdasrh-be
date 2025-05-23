package com.kehutanan.pepdas.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_kegiatan_monev_kriteria_evaluasi")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevKriteriaEvaluasi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String aktivitas;
    
    private Integer target;
    
    private Integer realisasi;
    
    @Column(columnDefinition = "TEXT")
    private String catatan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_monev_id", nullable = false)
    @JsonBackReference
    private KegiatanMonev kegiatanMonev;
}