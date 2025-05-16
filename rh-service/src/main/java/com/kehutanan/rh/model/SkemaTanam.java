package com.kehutanan.rh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
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
    private String kategori;
    
    @Column(name = "skema_btg_ha", precision = 10, scale = 2)
    private BigDecimal skemaBtgHa;
    
    @Column(name = "target_luas", precision = 10, scale = 2)
    private BigDecimal targetLuas;
    
    @Column(nullable = false)
    private String status;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    @ManyToOne
    @JoinColumn(name = "rh_program_id", nullable = false)
    @JsonIgnore
    private Program program;

    @JsonProperty("rh_program_id")
    public UUID getProgramId() {
        return program != null ? program.getId() : null;
    }
}