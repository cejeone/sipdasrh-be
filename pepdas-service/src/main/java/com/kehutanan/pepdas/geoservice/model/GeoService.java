package com.kehutanan.pepdas.geoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_geoservice")
@NoArgsConstructor
@AllArgsConstructor
public class GeoService {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "direktorat", nullable = false)
    private String direktorat;
    
    @Column(name = "bpdas", nullable = false)
    private String bpdas;
    
    @Column(name = "geoservice_id")
    private String geoserviceId;
    
    @Column(name = "url")
    private String url;
    
    @Column(name = "tipe")
    private String tipe;
    
    @Column(name = "service")
    private String service;
    
    @Column(name = "status")
    private String status;
}