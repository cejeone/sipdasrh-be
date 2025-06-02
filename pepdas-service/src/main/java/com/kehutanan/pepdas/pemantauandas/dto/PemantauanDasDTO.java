package com.kehutanan.pepdas.pemantauandas.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PemantauanDasDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    // Related entity IDs and names
    private Long bpdasId;
    private String bpdasNama;
    
    private Long dasId;
    private String dasNama;
    
    private Long spasId;
    private String spasNama;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String tanggalDanWaktu;
    
    private Integer nilaiTma;
    private Integer nilaiCurahHujan;
    private Integer teganganBaterai;
    
    // Constructor to convert from Entity
    public PemantauanDasDTO(PemantauanDas entity) {
        this.id = entity.getId();
        
        // Set related entity IDs and names
        if (entity.getBpdas() != null) {
            this.bpdasId = entity.getBpdas().getId();
            this.bpdasNama = entity.getBpdas().getNamaBpdas();
        }
        
        if (entity.getDas() != null) {
            this.dasId = entity.getDas().getId();
            this.dasNama = entity.getDas().getNamaDas();
        }
        
        if (entity.getSpas() != null) {
            this.spasId = entity.getSpas().getId();
            this.spasNama = entity.getSpas().getSpas();
        }
        
        // Convert LocalDateTime to String
        if (entity.getTanggalDanWaktu() != null) {
            this.tanggalDanWaktu = entity.getTanggalDanWaktu().toString();
        }
        
        this.nilaiTma = entity.getNilaiTma();
        this.nilaiCurahHujan = entity.getNilaiCurahHujan();
        this.teganganBaterai = entity.getTeganganBaterai();
    }
}