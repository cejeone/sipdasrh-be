package com.kehutanan.rh.dokumen.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "rh_dokumen")
public class Dokumen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String tipe;
    
    @Column(nullable = false)
    private String namaDokumen;
    
    private String status;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    private LocalDateTime uploadedAt;

    @OneToMany(mappedBy = "dokumen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DokumenFile> files = new ArrayList<>();
}