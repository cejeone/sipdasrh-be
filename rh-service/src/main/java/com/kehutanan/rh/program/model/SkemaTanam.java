package com.kehutanan.rh.program.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

@Data
@Entity
@Table(name = "rh_skema_tanam")
@NoArgsConstructor
@AllArgsConstructor
public class SkemaTanam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String pola;
    
    @Column(name = "jumlah_btg_ha")
    private Integer jumlahBtgHa;
    
    @Column(name = "target_luas")
    private Integer targetLuas;
    
    @Column(nullable = false)
    private String status;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rh_program_id", nullable = false)
    @JsonIgnore
    private Program program;
}