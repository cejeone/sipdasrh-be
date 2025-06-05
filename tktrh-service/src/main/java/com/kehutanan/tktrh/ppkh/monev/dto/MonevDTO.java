package com.kehutanan.tktrh.ppkh.monev.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.ppkh.monev.model.Monev;
import com.kehutanan.tktrh.ppkh.monev.model.MonevPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String tanggal;
    
    private String subjek;
    private String audiensi;
    private String isuTindakLanjut;
    
    // Related entity IDs instead of full entities
    private Long programId;
    private String programName;
    private Long bpdasId;
    private String bpdasName;
    
    // Lists for file references
    private List<MonevFileDTO> monevPdfList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public MonevDTO(Monev entity) {
        this.id = entity.getId();
        
        // Convert LocalDate to String
        if (entity.getTanggal() != null) {
            this.tanggal = entity.getTanggal().toString();
        }
        
        this.subjek = entity.getSubjek();
        this.audiensi = entity.getAudiensi();
        this.isuTindakLanjut = entity.getIsuTindakLanjut();
        
        // Set related entity IDs and names
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programName = entity.getProgram().getNama();
        }
        
        if (entity.getBpdas() != null) {
            this.bpdasId = entity.getBpdas().getId();
            this.bpdasName = entity.getBpdas().getNamaBpdas();
        }
        
        // Convert file lists
        if (entity.getMonevPdfs() != null) {
            for (MonevPdf pdf : entity.getMonevPdfs()) {
                this.monevPdfList.add(new MonevFileDTO(pdf));
            }
        }
    }
}
