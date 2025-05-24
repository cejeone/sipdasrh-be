package com.kehutanan.pepdas.pemantauandas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PemantauanDasResponseDto {
    
    private UUID id;
    private String bpdas;
    private String das;
    private String spasId;
    private LocalDateTime tanggalWaktu;
    private Double nilaiTma;
    private Double nilaiCurahHujan;
    private Double teganganBaterai;
}