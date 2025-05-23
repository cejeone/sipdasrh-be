package com.kehutanan.pepdas.program.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_jenis_bibit")
@NoArgsConstructor
@AllArgsConstructor
public class JenisBibit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String kategori;
    
    @Column(name = "nama_bibit", nullable = false)
    private String namaBibit;
    
    @Column(name = "sumber_bibit")
    private String sumberBibit;
    
    private Integer jumlah;
    
    @Column(nullable = false)
    private String status;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pepdas_program_id", nullable = false)
    @JsonIgnore
    private Program program;
}