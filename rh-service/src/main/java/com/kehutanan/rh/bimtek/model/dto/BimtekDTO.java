package com.kehutanan.rh.bimtek.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.master.model.dto.BpdasDTO;
import com.kehutanan.rh.program.model.dto.ProgramDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekDTO implements Serializable {

    private Long id;
    private ProgramDTO program;
    private BpdasDTO bpdas;
    private String namaBimtek;
    private String subjek;
    private String tempat;
    private String tanggal;
    private String audience;
    private List<BimtekPdfDTO> bimtekPdfs = new ArrayList<>();
    private List<BimtekVideoDTO> bimtekVideos = new ArrayList<>();
    private List<BimtekFotoDTO> bimtekFotos = new ArrayList<>();
    private String evaluasi;
    private String catatan;
    
    public BimtekDTO(Bimtek bimtek) {
        this.id = bimtek.getId();
        
        // Handle the Program relationship
        if (bimtek.getProgram() != null) {
            this.program = new ProgramDTO(bimtek.getProgram());
        }
        
        // Handle the Bpdas relationship
        if (bimtek.getBpdasId() != null) {
            this.bpdas = new BpdasDTO(bimtek.getBpdasId());
        }
        
        this.namaBimtek = bimtek.getNamaBimtek();
        this.subjek = bimtek.getSubjek();
        this.tempat = bimtek.getTempat();
        
        // Convert LocalDate to String
        if (bimtek.getTanggal() != null) {
            this.tanggal = bimtek.getTanggal().toString();
        }
        
        this.audience = bimtek.getAudience();
        
        // Convert related lists
        if (bimtek.getBimtekPdfs() != null) {
            this.bimtekPdfs = bimtek.getBimtekPdfs().stream()
                                  .map(BimtekPdfDTO::new)
                                  .collect(Collectors.toList());
        }
        
        if (bimtek.getBimtekVideos() != null) {
            this.bimtekVideos = bimtek.getBimtekVideos().stream()
                                    .map(BimtekVideoDTO::new)
                                    .collect(Collectors.toList());
        }
        
        if (bimtek.getBimtekFotos() != null) {
            this.bimtekFotos = bimtek.getBimtekFotos().stream()
                                   .map(BimtekFotoDTO::new)
                                   .collect(Collectors.toList());
        }
        
        this.evaluasi = bimtek.getEvaluasi();
        this.catatan = bimtek.getCatatan();
    }
}