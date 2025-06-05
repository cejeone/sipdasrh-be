package com.kehutanan.tktrh.bkta.monev.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.kehutanan.tktrh.bkta.monev.model.MonevPusat;
import com.kehutanan.tktrh.bkta.program.model.dto.ProgramDTO;
import com.kehutanan.tktrh.master.model.dto.BpdasDTO;

import java.io.Serializable;

/**
 * DTO class for MonevPusat entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevPusatDTO implements Serializable {

    private Long id;
    private ProgramDTO program;
    private BpdasDTO bpdas;
    private Integer targetTotalDpn;
    private Integer targetTotalGp;
    private Integer targetTambahanDpn;
    private Integer targetTambahanGp;
    private Integer realisasiRantekDpn;
    private Integer realisasiRantekGp;
    private Integer realisasiSpksDpn;
    private Integer realisasiSpksGp;
    private Integer realisasiFisikDpn;
    private Integer realisasiFisikGp;
    private String keterangan;

    /**
     * Constructor that builds the DTO from the entity
     * 
     * @param monevPusat the entity to convert to DTO
     */
    public MonevPusatDTO(MonevPusat monevPusat) {
        if (monevPusat != null) {
            this.id = monevPusat.getId();
            
            if (monevPusat.getProgram() != null) {
                this.program = new ProgramDTO(monevPusat.getProgram());
            }
            
            if (monevPusat.getBpdas() != null) {
                this.bpdas = new BpdasDTO(monevPusat.getBpdas());
            }
            
            this.targetTotalDpn = monevPusat.getTargetTotalDpn();
            this.targetTotalGp = monevPusat.getTargetTotalGp();
            this.targetTambahanDpn = monevPusat.getTargetTambahanDpn();
            this.targetTambahanGp = monevPusat.getTargetTambahanGp();
            this.realisasiRantekDpn = monevPusat.getRealisasiRantekDpn();
            this.realisasiRantekGp = monevPusat.getRealisasiRantekGp();
            this.realisasiSpksDpn = monevPusat.getRealisasiSpksDpn();
            this.realisasiSpksGp = monevPusat.getRealisasiSpksGp();
            this.realisasiFisikDpn = monevPusat.getRealisasiFisikDpn();
            this.realisasiFisikGp = monevPusat.getRealisasiFisikGp();
            this.keterangan = monevPusat.getKeterangan();
        }
    }
}
