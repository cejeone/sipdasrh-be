package com.kehutanan.rh.program.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "rh_pagu_anggaran")
@NoArgsConstructor
@AllArgsConstructor
public class PaguAnggaran {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "sumber_anggaran", nullable = false)
    private String sumberAnggaran;
    
    @Column(name = "tahun_anggaran", nullable = false)
    private Integer tahunAnggaran;
    
    @Column(precision = 19, scale = 2)
    private BigDecimal pagu;
    
    @Column(nullable = false)
    private String status;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rh_program_id", nullable = false)
    @JsonIgnore
    private Program program;
}