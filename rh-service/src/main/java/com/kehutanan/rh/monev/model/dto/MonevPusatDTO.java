package com.kehutanan.rh.monev.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.kehutanan.rh.master.model.dto.BpdasDTO;
import com.kehutanan.rh.program.model.dto.ProgramDTO;
import com.kehutanan.rh.monev.model.MonevPusat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevPusatDTO {
    private Long id;
    private ProgramDTO program;
    private BpdasDTO bpdas;
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
    
    public MonevPusatDTO(MonevPusat monevPusat) {
        if (monevPusat != null) {
            this.id = monevPusat.getId();
            
            if (monevPusat.getProgram() != null) {
                this.program = new ProgramDTO(monevPusat.getProgram());
            }
            
            if (monevPusat.getBpdasId() != null) {
                this.bpdas = new BpdasDTO(monevPusat.getBpdasId());
            }
            
            this.luasTotalTarget = monevPusat.getLuasTotalTarget();
            this.luasTotalRealisasi = monevPusat.getLuasTotalRealisasi();
            this.targetT1 = monevPusat.getTargetT1();
            this.realisasiT1 = monevPusat.getRealisasiT1();
            this.targetP0 = monevPusat.getTargetP0();
            this.realisasiP0 = monevPusat.getRealisasiP0();
            this.targetP1 = monevPusat.getTargetP1();
            this.realisasiP1 = monevPusat.getRealisasiP1();
            this.targetP2 = monevPusat.getTargetP2();
            this.realisasiP2 = monevPusat.getRealisasiP2();
            this.targetBast = monevPusat.getTargetBast();
            this.realisasiBast = monevPusat.getRealisasiBast();
            this.keterangan = monevPusat.getKeterangan();
        }
    }
}