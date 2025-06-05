package com.kehutanan.tktrh.ppkh.serahterima.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.kehutanan.tktrh.master.model.dto.BpdasDTO;
import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.master.model.dto.ProvinsiDTO;
import com.kehutanan.tktrh.ppkh.program.model.dto.ProgramDTO;
import com.kehutanan.tktrh.ppkh.serahterima.model.BastPusat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BastPusatDTO {
    
    private Long id;
    private ProgramDTO programId;
    private BpdasDTO bpdasId;
    private ProvinsiDTO provinsiId;
    private LovDTO fungsiKawasan;
    private Integer targetLuas;
    private Integer realisasiLuas;
    private LovDTO statusId;
    private String keterangan;
    
    public BastPusatDTO(BastPusat bastPusat) {
        if (bastPusat != null) {
            this.id = bastPusat.getId();
            
            if (bastPusat.getProgramId() != null) {
                this.programId = new ProgramDTO(bastPusat.getProgramId());
            }
            
            if (bastPusat.getBpdasId() != null) {
                this.bpdasId = new BpdasDTO(bastPusat.getBpdasId());
            }
            
            if (bastPusat.getProvinsiId() != null) {
                this.provinsiId = new ProvinsiDTO(bastPusat.getProvinsiId());
            }
            
            if (bastPusat.getFungsiKawasan() != null) {
                this.fungsiKawasan = new LovDTO(bastPusat.getFungsiKawasan());
            }
            
            this.targetLuas = bastPusat.getTargetLuas();
            this.realisasiLuas = bastPusat.getRealisasiLuas();
            
            if (bastPusat.getStatusId() != null) {
                this.statusId = new LovDTO(bastPusat.getStatusId());
            }
            
            this.keterangan = bastPusat.getKeterangan();
        }
    }
}
