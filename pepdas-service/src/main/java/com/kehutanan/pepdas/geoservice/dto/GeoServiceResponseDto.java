package com.kehutanan.pepdas.geoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoServiceResponseDto {
    
    private UUID id;
    private String direktorat;
    private String bpdas;
    private String geoserviceId;
    private String url;
    private String tipe;
    private String service;
    private String status;
}