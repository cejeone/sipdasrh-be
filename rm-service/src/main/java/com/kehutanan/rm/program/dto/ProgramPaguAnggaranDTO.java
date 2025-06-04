package com.kehutanan.rm.program.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.kehutanan.rm.program.model.Program;
import com.kehutanan.rm.program.model.ProgramPaguAnggaran;
import com.kehutanan.rm.master.model.Lov;

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
    private String programName;
    
    private Long kategoriId;
    private String kategoriName;
    
    private Long sumberAnggaranId;
    private String sumberAnggaranName;
    
    private Long statusId;
    private String statusName;
    
    private Integer tahunAnggaran;
    private Double pagu;
    private String keterangan;
    
    // Constructor to convert from Entity
    public ProgramPaguAnggaranDTO(ProgramPaguAnggaran entity) {
        this.id = entity.getId();
        
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programName = entity.getProgram().getNama();
        }
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = entity.getKategoriId().getId();
            this.kategoriName = entity.getKategoriId().getNilai();
        }
        
        if (entity.getSumberAnggaranId() != null) {
            this.sumberAnggaranId = entity.getSumberAnggaranId().getId();
            this.sumberAnggaranName = entity.getSumberAnggaranId().getNilai();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusName = entity.getStatusId().getNilai();
        }
        
        this.tahunAnggaran = entity.getTahunAnggaran();
        this.pagu = entity.getPagu();
        this.keterangan = entity.getKeterangan();
    }
}