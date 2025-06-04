package com.kehutanan.tktrh.bkta.monev.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.bkta.monev.model.MonevPusat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevPusatDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    // Program related fields
    private Long programId;
    private String programNama;
    
    // Bpdas related fields
    private Long bpdasId;
    private String bpdasNama;
    
    // Target fields
    private Integer targetTotalDpn;
    private Integer targetTotalGp;
    private Integer targetTambahanDpn;
    private Integer targetTambahanGp;
    
    // Realization fields
    private Integer realisasiRantekDpn;
    private Integer realisasiRantekGp;
    private Integer realisasiSpksDpn;
    private Integer realisasiSpksGp;
    private Integer realisasiFisikDpn;
    private Integer realisasiFisikGp;
    
    private String keterangan;
    
    // Constructor to convert from Entity
    public MonevPusatDTO(MonevPusat entity) {
        this.id = entity.getId();
        
        // Set program related data
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNama();
        }
        
        // Set BPDAS related data
        if (entity.getBpdas() != null) {
            this.bpdasId = entity.getBpdas().getId();
            this.bpdasNama = entity.getBpdas().getNamaBpdas();
        }
        
        // Set target data
        this.targetTotalDpn = entity.getTargetTotalDpn();
        this.targetTotalGp = entity.getTargetTotalGp();
        this.targetTambahanDpn = entity.getTargetTambahanDpn();
        this.targetTambahanGp = entity.getTargetTambahanGp();
        
        // Set realization data
        this.realisasiRantekDpn = entity.getRealisasiRantekDpn();
        this.realisasiRantekGp = entity.getRealisasiRantekGp();
        this.realisasiSpksDpn = entity.getRealisasiSpksDpn();
        this.realisasiSpksGp = entity.getRealisasiSpksGp();
        this.realisasiFisikDpn = entity.getRealisasiFisikDpn();
        this.realisasiFisikGp = entity.getRealisasiFisikGp();
        
        this.keterangan = entity.getKeterangan();
    }
}