package com.kehutanan.pepdas.geoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoServiceRequestDto {
    
    @NotBlank(message = "Direktorat tidak boleh kosong")
    @Size(max = 255, message = "Direktorat maksimal 255 karakter")
    private String direktorat;
    
    @NotBlank(message = "BPDAS tidak boleh kosong")
    @Size(max = 255, message = "BPDAS maksimal 255 karakter")
    private String bpdas;
    
    @Size(max = 255, message = "GeoService ID maksimal 255 karakter")
    private String geoserviceId;
    
    @Size(max = 500, message = "URL maksimal 500 karakter")
    private String url;
    
    @Size(max = 100, message = "Tipe maksimal 100 karakter")
    private String tipe;
    
    @Size(max = 100, message = "Service maksimal 100 karakter")
    private String service;
    
    @Size(max = 50, message = "Status maksimal 50 karakter")
    private String status;
}