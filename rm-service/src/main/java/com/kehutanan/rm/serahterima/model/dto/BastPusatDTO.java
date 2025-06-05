package com.kehutanan.rm.serahterima.model.dto;

import com.kehutanan.rm.master.model.dto.BpdasDTO;
import com.kehutanan.rm.master.model.dto.LovDTO;
import com.kehutanan.rm.master.model.dto.ProvinsiDTO;
import com.kehutanan.rm.program.model.dto.ProgramDTO;
import com.kehutanan.rm.serahterima.model.BastPusat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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