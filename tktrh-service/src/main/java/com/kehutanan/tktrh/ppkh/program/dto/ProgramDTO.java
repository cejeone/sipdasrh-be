package com.kehutanan.tktrh.ppkh.program.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.tktrh.ppkh.program.model.Program;
import com.kehutanan.tktrh.ppkh.program.model.ProgramPaguAnggaran;

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
    
    // Status information
    private Long statusId;
    private String statusNama;
    
    // Lists for related entities
    private List<ProgramPaguAnggaranDTO> paguAnggarans = new ArrayList<>();
    
    // Constructor to convert from Entity
    public ProgramDTO(Program entity) {
        this.id = entity.getId();
        this.nama = entity.getNama();
        this.tahunPelaksana = entity.getTahunPelaksana();
        this.totalAnggaran = entity.getTotalAnggaran();
        this.totalLuas = entity.getTotalLuas();
        
        // Set status information
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNamaKategori();
        }
        
        // Convert pagu anggaran lists
        if (entity.getPaguAnggarans() != null) {
            for (ProgramPaguAnggaran pagu : entity.getPaguAnggarans()) {
                this.paguAnggarans.add(new ProgramPaguAnggaranDTO(pagu));
            }
        }
    }
}