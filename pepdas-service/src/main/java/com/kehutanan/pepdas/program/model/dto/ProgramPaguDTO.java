package com.kehutanan.pepdas.program.model.dto;

import com.kehutanan.pepdas.master.model.dto.LovDTO;
import com.kehutanan.pepdas.program.model.ProgramPagu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguDTO {
    
    private Long id;
    private Long programId; // Only store program ID to avoid circular reference
    private LovDTO kategori;
    private LovDTO sumberAnggaran;
    private LovDTO status;
    private Integer tahunAnggaran;
    private Double pagu;
    private String keterangan;
    
    public ProgramPaguDTO(ProgramPagu pagu) {
        if (pagu != null) {
            this.id = pagu.getId();
            if (pagu.getProgram() != null) {
                this.programId = pagu.getProgram().getId();
            }
            if (pagu.getKategori() != null) {
                this.kategori = new LovDTO(pagu.getKategori());
            }
            if (pagu.getSumberAnggaran() != null) {
                this.sumberAnggaran = new LovDTO(pagu.getSumberAnggaran());
            }
            if (pagu.getStatus() != null) {
                this.status = new LovDTO(pagu.getStatus());
            }
            this.tahunAnggaran = pagu.getTahunAnggaran();
            this.pagu = pagu.getPagu();
            this.keterangan = pagu.getKeterangan();
        }
    }
}