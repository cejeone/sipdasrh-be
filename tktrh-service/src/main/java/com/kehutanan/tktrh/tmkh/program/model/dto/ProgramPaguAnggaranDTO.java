package com.kehutanan.tktrh.tmkh.program.model.dto;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.tmkh.program.model.ProgramPaguAnggaran;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguAnggaranDTO {
    private Long id;
    private Long programId; // Just the ID to avoid circular dependency
    private LovDTO sumberAnggaran;
    private Integer tahunAnggaran;
    private Double pagu;
    private LovDTO status;
    private String keterangan;
    
    public ProgramPaguAnggaranDTO(ProgramPaguAnggaran entity) {
        if (entity != null) {
            this.id = entity.getId();
            if (entity.getProgram() != null) {
                this.programId = entity.getProgram().getId();
            }
            if (entity.getSumberAnggaran() != null) {
                this.sumberAnggaran = new LovDTO(entity.getSumberAnggaran());
            }
            this.tahunAnggaran = entity.getTahunAnggaran();
            this.pagu = entity.getPagu();
            if (entity.getStatus() != null) {
                this.status = new LovDTO(entity.getStatus());
            }
            this.keterangan = entity.getKeterangan();
        }
    }
}
