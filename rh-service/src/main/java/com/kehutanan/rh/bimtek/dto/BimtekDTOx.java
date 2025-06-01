package com.kehutanan.rh.bimtek.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekFoto;
import com.kehutanan.rh.bimtek.model.BimtekPdf;
import com.kehutanan.rh.bimtek.model.BimtekVideo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekDTOx implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String namaBimtek;
    private String subjek;
    private String tempat;
    private LocalDate tanggal;
    private String audience;
    private String evaluasi;
    private String catatan;
    
    // Related entity IDs instead of full entities
    private Long programId;
    private String programNama;
    private Long bpdasId;
    private String bpdasNama;
    
    // Lists for file references
    private List<BimtekFileDTO> bimtekPdfList = new ArrayList<>();
    private List<BimtekFileDTO> bimtekFotoList = new ArrayList<>();
    private List<BimtekFileDTO> bimtekVideoList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public BimtekDTO(Bimtek entity) {
        this.id = entity.getId();
        this.namaBimtek = entity.getNamaBimtek();
        this.subjek = entity.getSubjek();
        this.tempat = entity.getTempat();
        this.tanggal = entity.getTanggal();
        this.audience = entity.getAudience();
        this.evaluasi = entity.getEvaluasi();
        this.catatan = entity.getCatatan();
        
        // Set related entity IDs and names
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNama();
        }
        
        if (entity.getBpdasId() != null) {
            this.bpdasId = entity.getBpdasId().getId();
            this.bpdasNama = entity.getBpdasId().getNamaBpdas();
        }
        
        // Convert file lists
        if (entity.getBimtekPdfs() != null) {
            for (BimtekPdf pdf : entity.getBimtekPdfs()) {
                this.bimtekPdfList.add(new BimtekFileDTO(pdf));
            }
        }
        
        if (entity.getBimtekFotos() != null) {
            for (BimtekFoto foto : entity.getBimtekFotos()) {
                this.bimtekFotoList.add(new BimtekFileDTO(foto));
            }
        }
        
        if (entity.getBimtekVideos() != null) {
            for (BimtekVideo video : entity.getBimtekVideos()) {
                this.bimtekVideoList.add(new BimtekFileDTO(video));
            }
        }
    }
}