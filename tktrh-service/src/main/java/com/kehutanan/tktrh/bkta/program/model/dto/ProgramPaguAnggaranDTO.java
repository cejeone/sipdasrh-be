package com.kehutanan.tktrh.bkta.program.model.dto;

import com.kehutanan.tktrh.bkta.program.model.ProgramPaguAnggaran;
import com.kehutanan.tktrh.master.model.dto.LovDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguAnggaranDTO implements Serializable {

    private Long id;
    private Long programId; // Only store program ID to avoid circular dependency
    private LovDTO sumberAnggaran;
    private LovDTO status;
    private Integer tahunAnggaran;
    private Double pagu;
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
            if (entity.getStatus() != null) {
                this.status = new LovDTO(entity.getStatus());
            }
            this.tahunAnggaran = entity.getTahunAnggaran();
            this.pagu = entity.getPagu();
            this.keterangan = entity.getKeterangan();
        }
    }
}
