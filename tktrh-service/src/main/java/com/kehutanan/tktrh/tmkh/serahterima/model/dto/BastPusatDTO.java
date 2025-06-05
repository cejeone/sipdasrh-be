package com.kehutanan.tktrh.tmkh.serahterima.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.kehutanan.tktrh.master.model.dto.BpdasDTO;
import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.master.model.dto.ProvinsiDTO;
import com.kehutanan.tktrh.tmkh.program.model.dto.ProgramDTO;
import com.kehutanan.tktrh.tmkh.serahterima.model.BastPusat;

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
    
    public BastPusatDTO(BastPusat entity) {
        if (entity != null) {
            this.id = entity.getId();
            
            if (entity.getProgramId() != null) {
                this.programId = new ProgramDTO(entity.getProgramId());
            }
            
            if (entity.getBpdasId() != null) {
                this.bpdasId = new BpdasDTO(entity.getBpdasId());
            }
            
            if (entity.getProvinsiId() != null) {
                this.provinsiId = new ProvinsiDTO(entity.getProvinsiId());
            }
            
            if (entity.getFungsiKawasan() != null) {
                this.fungsiKawasan = new LovDTO(entity.getFungsiKawasan());
            }
            
            this.targetLuas = entity.getTargetLuas();
            this.realisasiLuas = entity.getRealisasiLuas();
            
            if (entity.getStatusId() != null) {
                this.statusId = new LovDTO(entity.getStatusId());
            }
            
            this.keterangan = entity.getKeterangan();
        }
    }
}
