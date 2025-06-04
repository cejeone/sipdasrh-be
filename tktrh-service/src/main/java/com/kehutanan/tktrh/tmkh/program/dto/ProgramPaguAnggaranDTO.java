package com.kehutanan.tktrh.tmkh.program.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.tmkh.program.model.ProgramPaguAnggaran;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguAnggaranDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long programId;
    private String programNama;
    private Long sumberAnggaranId;
    private String sumberAnggaranNama;
    private Integer tahunAnggaran;
    private Double pagu;
    private Long statusId;
    private String statusNama;
    private String keterangan;
    
    // Constructor to convert from Entity
    public ProgramPaguAnggaranDTO(ProgramPaguAnggaran entity) {
        if (entity != null) {
            this.id = entity.getId();
            
            if (entity.getProgram() != null) {
                this.programId = entity.getProgram().getId();
                this.programNama = entity.getProgram().getNama();
            }
            
            if (entity.getSumberAnggaran() != null) {
                this.sumberAnggaranId = entity.getSumberAnggaran().getId();
                this.sumberAnggaranNama = entity.getSumberAnggaran().getNamaKategori();
            }
            
            this.tahunAnggaran = entity.getTahunAnggaran();
            this.pagu = entity.getPagu();
            
            if (entity.getStatus() != null) {
                this.statusId = entity.getStatus().getId();
                this.statusNama = entity.getStatus().getNamaKategori();
            }
            
            this.keterangan = entity.getKeterangan();
        }
    }
}