package com.kehutanan.tktrh.tmkh.program.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.tktrh.tmkh.program.model.Program;
import com.kehutanan.tktrh.tmkh.program.model.ProgramPaguAnggaran;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nama;
    private Integer tahunPelaksana;
    private Double totalAnggaran;
    private Integer totalLuas;
    
    // Related entity IDs instead of full entities
    private Long statusId;
    private String statusNama;
    
    // Lists for related data
    private List<ProgramPaguAnggaranDTO> paguAnggarans = new ArrayList<>();
    
    // Constructor to convert from Entity
    public ProgramDTO(Program entity) {
        System.out.println("Converting Program entity to DTO: " + entity.getId());
        this.id = entity.getId();
        this.nama = entity.getNama();
        this.tahunPelaksana = entity.getTahunPelaksana();
        this.totalAnggaran = entity.getTotalAnggaran();
        this.totalLuas = entity.getTotalLuas();
        
        // Set related entity IDs and names
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getStatus();
        }
        
        // Convert pagu anggaran lists
        if (entity.getPaguAnggarans() != null) {
            for (ProgramPaguAnggaran paguAnggaran : entity.getPaguAnggarans()) {
                this.paguAnggarans.add(new ProgramPaguAnggaranDTO(paguAnggaran));
            }
        }
    }
}