package com.kehutanan.rh.program.dto;

import java.io.Serializable;

import com.kehutanan.rh.program.model.ProgramSkema;
import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.master.model.Lov;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramSkemaDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long programId;
    private String programNama;
    private Long kategoriId;
    private String kategoriNama;
    private Long statusId;
    private String statusNama;
    private Integer skema;
    private Double targetLuas;
    private String keterangan;
    
    // Constructor to convert from Entity
    public ProgramSkemaDTO(ProgramSkema entity) {
        this.id = entity.getId();
        
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNama();
        }
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = entity.getKategoriId().getId();
            this.kategoriNama = entity.getKategoriId().getNilai();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        this.skema = entity.getSkema();
        this.targetLuas = entity.getTargetLuas();
        this.keterangan = entity.getKeterangan();
    }
}