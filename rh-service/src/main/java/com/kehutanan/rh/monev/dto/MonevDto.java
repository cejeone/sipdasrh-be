package com.kehutanan.rh.monev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevDto {
    
    private String program;
    
    private String bpdas;
    
    private Integer totalTarget;
    
    private Integer totalRealisasi;
    
    private Integer totalT1;
    
    private Integer realisasiT1;
    
    private Integer totalP0;
    
    private Integer realisasiP0;
    
    private Integer totalP1;
    
    private Integer realisasiP1;
    
    private Integer totalP2;
    
    private Integer realisasiP2;
    
    private Integer totalBast;
    
    private Integer realisasiBast;
    
    private String keterangan;
}