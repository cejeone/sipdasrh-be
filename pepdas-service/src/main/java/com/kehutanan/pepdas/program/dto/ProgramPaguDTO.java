package com.kehutanan.pepdas.program.dto;

import com.kehutanan.pepdas.program.model.ProgramPagu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long programId;
    private Integer tahunAnggaran;
    private Double pagu;
    private String keterangan;
    
    // Related entity IDs and names
    private Long kategoriId;
    private String kategoriNama;
    private Long sumberAnggaranId;
    private String sumberAnggaranNama;
    private Long statusId;
    private String statusNama;
    
    // Constructor to convert from Entity
    public ProgramPaguDTO(ProgramPagu entity) {
        this.id = entity.getId();
        this.tahunAnggaran = entity.getTahunAnggaran();
        this.pagu = entity.getPagu();
        this.keterangan = entity.getKeterangan();
        
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
        }
        
        if (entity.getKategori() != null) {
            this.kategoriId = entity.getKategori().getId();
            this.kategoriNama = entity.getKategori().getNilai();
        }
        
        if (entity.getSumberAnggaran() != null) {
            this.sumberAnggaranId = entity.getSumberAnggaran().getId();
            this.sumberAnggaranNama = entity.getSumberAnggaran().getNilai();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
        }
    }
}