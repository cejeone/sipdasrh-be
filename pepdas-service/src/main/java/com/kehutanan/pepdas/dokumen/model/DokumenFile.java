package com.kehutanan.pepdas.dokumen.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_dokumen_file")
public class DokumenFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dokumen_id", nullable = false)
    private Dokumen dokumen;
    
    @Column(nullable = false)
    private String namaFile;
    
    private String namaAsli;
    
    private Double ukuranMb;
    
    private String contentType;
    
    private LocalDateTime uploadedAt;
}