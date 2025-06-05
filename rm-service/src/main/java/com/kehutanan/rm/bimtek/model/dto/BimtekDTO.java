package com.kehutanan.rm.bimtek.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.bimtek.model.Bimtek;
import com.kehutanan.rm.master.model.dto.BpdasDTO;
import com.kehutanan.rm.program.model.dto.ProgramDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekDTO {

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
        
        // Handle program if not null
        if (bimtek.getProgram() != null) {
            this.program = new ProgramDTO(bimtek.getProgram());
        }
        
        // Handle bpdas if not null
        if (bimtek.getBpdasId() != null) {
            this.bpdas = new BpdasDTO(bimtek.getBpdasId());
        }
        
        this.namaBimtek = bimtek.getNamaBimtek();
        this.subjek = bimtek.getSubjek();
        this.tempat = bimtek.getTempat();
        this.tanggal = bimtek.getTanggal() != null ? bimtek.getTanggal().toString() : null;
        this.audience = bimtek.getAudience();
        
        // Convert lists if they are not null
        if (bimtek.getBimtekPdfs() != null && !bimtek.getBimtekPdfs().isEmpty()) {
            this.bimtekPdfs = bimtek.getBimtekPdfs().stream()
                .map(BimtekPdfDTO::new)
                .collect(Collectors.toList());
        }
        
        if (bimtek.getBimtekVideos() != null && !bimtek.getBimtekVideos().isEmpty()) {
            this.bimtekVideos = bimtek.getBimtekVideos().stream()
                .map(BimtekVideoDTO::new)
                .collect(Collectors.toList());
        }
        
        if (bimtek.getBimtekFotos() != null && !bimtek.getBimtekFotos().isEmpty()) {
            this.bimtekFotos = bimtek.getBimtekFotos().stream()
                .map(BimtekFotoDTO::new)
                .collect(Collectors.toList());
        }
        
        this.evaluasi = bimtek.getEvaluasi();
        this.catatan = bimtek.getCatatan();
    }
}
