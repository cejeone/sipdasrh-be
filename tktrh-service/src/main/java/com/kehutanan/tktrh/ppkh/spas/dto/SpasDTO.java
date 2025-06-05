package com.kehutanan.tktrh.ppkh.spas.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.ppkh.spas.model.Spas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpasDTO implements Serializable {
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
    private String tanggal;
    
    private Double nilaiTma;
    private Double nilaiCurahHujan;
    private Double teganganBaterai;
    
    // Constructor to convert from Entity
    public SpasDTO(Spas entity) {
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
        
        if (entity.getSpasId() != null) {
            this.spasId = entity.getSpasId().getId();
            this.spasNama = entity.getSpasId().getNama();
        }
        
        // Convert LocalDateTime to String
        if (entity.getTanggal() != null) {
            this.tanggal = entity.getTanggal().toString();
        }
        
        this.nilaiTma = entity.getNilaiTma();
        this.nilaiCurahHujan = entity.getNilaiCurahHujan();
        this.teganganBaterai = entity.getTeganganBaterai();
    }
}
