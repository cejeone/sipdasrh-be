package com.kehutanan.tktrh.tmkh.monev.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.tmkh.monev.model.MonevPusat;
import com.kehutanan.tktrh.tmkh.program.model.Program;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevPusatDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Double luasTotalTarget;
    private Double luasTotalRealisasi;
    private Double targetT1;
    private Double realisasiT1;
    private Double targetP0;
    private Double realisasiP0;
    private Double targetP1;
    private Double realisasiP1;
    private Double targetP2;
    private Double realisasiP2;
    private Double targetBast;
    private Double realisasiBast;
    private String keterangan;
    
    // Related entity IDs and names instead of full entities
    private Long programId;
    private String programNama;
    private Long bpdasId;
    private String bpdasNama;
    
    // Constructor to convert from Entity
    public MonevPusatDTO(MonevPusat entity) {
        this.id = entity.getId();
        this.luasTotalTarget = entity.getLuasTotalTarget();
        this.luasTotalRealisasi = entity.getLuasTotalRealisasi();
        this.targetT1 = entity.getTargetT1();
        this.realisasiT1 = entity.getRealisasiT1();
        this.targetP0 = entity.getTargetP0();
        this.realisasiP0 = entity.getRealisasiP0();
        this.targetP1 = entity.getTargetP1();
        this.realisasiP1 = entity.getRealisasiP1();
        this.targetP2 = entity.getTargetP2();
        this.realisasiP2 = entity.getRealisasiP2();
        this.targetBast = entity.getTargetBast();
        this.realisasiBast = entity.getRealisasiBast();
        this.keterangan = entity.getKeterangan();
        
        // Set related entity IDs and names
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNama();
        }
        
        if (entity.getBpdasId() != null) {
            this.bpdasId = entity.getBpdasId().getId();
            this.bpdasNama = entity.getBpdasId().getNamaBpdas();
        }
    }
}