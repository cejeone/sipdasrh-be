package com.kehutanan.pepdas.program.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.pepdas.dokumen.model.DokumenFile;

@Data
@Entity
@Table(name = "pepdas_program")
public class Program {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String direktorat;

        
    @Column( nullable = false)
    private String kategori;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String nama;
    
    @Column(name = "tahun_rencana", nullable = false)
    private Integer tahunRencana;

    @Column(name = "total_anggaran", precision = 19, scale = 2)
    private BigDecimal totalAnggaran;
    
    
    @Column(nullable = false)
    private String status;

    @JsonBackReference
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaguAnggaran> files = new ArrayList<>();


}