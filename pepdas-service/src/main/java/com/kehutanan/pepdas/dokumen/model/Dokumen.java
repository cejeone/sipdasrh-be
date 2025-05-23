package com.kehutanan.pepdas.dokumen.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_dokumen")
public class Dokumen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String tipe;
    
    @Column(nullable = false)
    private String namaDokumen;
    
    private String status;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    private LocalDateTime uploadedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "dokumen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DokumenFile> files = new ArrayList<>();
}