package com.kehutanan.rh.dokumen.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rh_dokumen_file")
public class DokumenFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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