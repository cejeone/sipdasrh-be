package com.kehutanan.tktrh.ppkh.serahterima.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.ppkh.serahterima.model.BastPusat;

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
            this.programNama = entity.getProgramId().getNama();
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
            this.fungsiKawasanNama = entity.getFungsiKawasan().getNilai();
        }
        
        this.targetLuas = entity.getTargetLuas();
        this.realisasiLuas = entity.getRealisasiLuas();
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        this.keterangan = entity.getKeterangan();
    }
}