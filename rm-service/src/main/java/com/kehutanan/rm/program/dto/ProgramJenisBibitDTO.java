package com.kehutanan.rm.program.dto;

import java.io.Serializable;

import com.kehutanan.rm.program.model.ProgramJenisBibit;
import com.kehutanan.rm.program.model.Program;
import com.kehutanan.rm.master.model.Lov;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramJenisBibitDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long programId;
    private String programNama;
    
    private Long kategoriId;
    private String kategoriNama;
    
    private Long namaBibitId;
    private String namaBibitNama;
    
    private Long sumberBibitId;
    private String sumberBibitNama;
    
    private Long statusId;
    private String statusNama;
    
    private String keterangan;
    
    // Constructor to convert from Entity
    public ProgramJenisBibitDTO(ProgramJenisBibit entity) {
        this.id = entity.getId();
        
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNama();
        }
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = entity.getKategoriId().getId();
            this.kategoriNama = entity.getKategoriId().getNilai();
        }
        
        if (entity.getNamaBibitId() != null) {
            this.namaBibitId = entity.getNamaBibitId().getId();
            this.namaBibitNama = entity.getNamaBibitId().getNilai();
        }
        
        if (entity.getSumberBibitId() != null) {
            this.sumberBibitId = entity.getSumberBibitId().getId();
            this.sumberBibitNama = entity.getSumberBibitId().getNilai();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        this.keterangan = entity.getKeterangan();
    }
}