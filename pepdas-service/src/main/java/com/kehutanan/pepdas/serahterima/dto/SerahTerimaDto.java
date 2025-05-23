package com.kehutanan.pepdas.serahterima.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerahTerimaDto {
    

    private String program;
    
    private String bpdas;
    
    private String provinsi;
    
    private String fungsiKawasan;
    
    private String realisasiLuas;
    
    private String status;
    
    private String keterangan;
}