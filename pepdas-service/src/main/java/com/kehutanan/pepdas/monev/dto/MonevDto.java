package com.kehutanan.pepdas.monev.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevDto {
    
    
    private UUID kegiatanId;
    
    private String nomorMonev;

    private String kontrak;
    
    private String rantek;
   
    private String pelaksana;

    private LocalDate tanggal;
        
    private String deskripsi;

    private String status;
}