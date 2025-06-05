package com.kehutanan.rh.serahterima.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.kehutanan.rh.master.model.dto.BpdasDTO;
import com.kehutanan.rh.master.model.dto.LovDTO;
import com.kehutanan.rh.master.model.dto.ProvinsiDTO;
import com.kehutanan.rh.program.model.dto.ProgramDTO;
import com.kehutanan.rh.serahterima.model.BastPusat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BastPusatDTO {
    private Long id;
    private ProgramDTO program;
    private BpdasDTO bpdas;
    private ProvinsiDTO provinsi;
    private LovDTO fungsiKawasan;
    private Integer targetLuas;
    private Integer realisasiLuas;
    private LovDTO status;
    private String keterangan;
    
    public BastPusatDTO(BastPusat bastPusat) {
        if (bastPusat != null) {
            this.id = bastPusat.getId();
            
            if (bastPusat.getProgramId() != null) {
                this.program = new ProgramDTO(bastPusat.getProgramId());
            }
            
            if (bastPusat.getBpdasId() != null) {
                this.bpdas = new BpdasDTO(bastPusat.getBpdasId());
            }
            
            if (bastPusat.getProvinsiId() != null) {
                this.provinsi = new ProvinsiDTO(bastPusat.getProvinsiId());
            }
            
            if (bastPusat.getFungsiKawasan() != null) {
                this.fungsiKawasan = new LovDTO(bastPusat.getFungsiKawasan());
            }
            
            this.targetLuas = bastPusat.getTargetLuas();
            this.realisasiLuas = bastPusat.getRealisasiLuas();
            
            if (bastPusat.getStatusId() != null) {
                this.status = new LovDTO(bastPusat.getStatusId());
            }
            
            this.keterangan = bastPusat.getKeterangan();
        }
    }
}