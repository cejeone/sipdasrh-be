package com.kehutanan.pepdas.program.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pepdas_pagu_anggaran")
@Data
@JsonIgnoreProperties({"program"})
@NoArgsConstructor
public class PaguAnggaran {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String kategori;
    private String sumberAnggaran;
    private Integer tahunAnggaran;
    private BigDecimal pagu;
    private String status;
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pepdas_program_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Program program;
}