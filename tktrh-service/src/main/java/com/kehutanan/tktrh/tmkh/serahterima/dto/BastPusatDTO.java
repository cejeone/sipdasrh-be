package com.kehutanan.tktrh.tmkh.serahterima.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.Provinsi;
import com.kehutanan.tktrh.tmkh.program.model.Program;
import com.kehutanan.tktrh.tmkh.serahterima.model.BastPusat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BastPusatDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    // Program information
    private Long programId;
    private String programNama;
    
    // BPDAS information
    private Long bpdasId;
    private String bpdasNama;
    
    // Provinsi information
    private Long provinsiId;
    private String provinsiNama;
    
    // Fungsi Kawasan information
    private Long fungsiKawasanId;
    private String fungsiKawasanNama;
    
    // Fields
    private Integer targetLuas;
    private Integer realisasiLuas;
    
    // Status information
    private Long statusId;
    private String statusNama;
    
    private String keterangan;
    
    // Constructor to convert from Entity
    public BastPusatDTO(BastPusat entity) {
        this.id = entity.getId();
        
        if (entity.getProgramId() != null) {
            this.programId = entity.getProgramId().getId();
            this.programNama = entity.getProgramId().getNamaProgram();
        }
        
        if (entity.getBpdasId() != null) {
            this.bpdasId = entity.getBpdasId().getId();
            this.bpdasNama = entity.getBpdasId().getNamaBpdas();
        }
        
        if (entity.getProvinsiId() != null) {
            this.provinsiId = entity.getProvinsiId().getId();
            this.provinsiNama = entity.getProvinsiId().getNamaProvinsi();
        }
        
        if (entity.getFungsiKawasan() != null) {
            this.fungsiKawasanId = entity.getFungsiKawasan().getId();
            this.fungsiKawasanNama = entity.getFungsiKawasan().getNama();
        }
        
        this.targetLuas = entity.getTargetLuas();
        this.realisasiLuas = entity.getRealisasiLuas();
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNama();
        }
        
        this.keterangan = entity.getKeterangan();
    }
}
