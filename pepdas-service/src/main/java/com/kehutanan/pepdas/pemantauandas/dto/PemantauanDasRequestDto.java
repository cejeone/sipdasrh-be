package com.kehutanan.pepdas.pemantauandas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PemantauanDasRequestDto {
    
    @NotBlank(message = "BPDAS tidak boleh kosong")
    @Size(max = 255, message = "BPDAS maksimal 255 karakter")
    private String bpdas;
    
    @NotBlank(message = "DAS tidak boleh kosong")
    @Size(max = 255, message = "DAS maksimal 255 karakter")
    private String das;
    
    @NotBlank(message = "SPAS ID tidak boleh kosong")
    @Size(max = 255, message = "SPAS ID maksimal 255 karakter")
    private String spasId;
    
    @NotNull(message = "Tanggal dan waktu tidak boleh kosong")
    private LocalDateTime tanggalWaktu;
    
    private Double nilaiTma;
    
    private Double nilaiCurahHujan;
    
    private Double teganganBaterai;
}