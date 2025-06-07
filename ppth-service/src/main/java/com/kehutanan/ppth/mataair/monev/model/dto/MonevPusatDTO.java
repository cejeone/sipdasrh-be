package com.kehutanan.ppth.mataair.monev.model.dto;

import com.kehutanan.ppth.master.model.dto.BpdasDTO;
import com.kehutanan.ppth.mataair.monev.model.MonevPusat;
import com.kehutanan.ppth.mataair.program.model.dto.ProgramDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonevPusatDTO {
    private Long id;
    private ProgramDTO program;
    private BpdasDTO bpdasId;
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
            
            if (monevPusat.getProgram() != null) {
                this.program = new ProgramDTO(monevPusat.getProgram());
            }
            
            if (monevPusat.getBpdasId() != null) {
                this.bpdasId = new BpdasDTO(monevPusat.getBpdasId());
            }
        }
    }
}
