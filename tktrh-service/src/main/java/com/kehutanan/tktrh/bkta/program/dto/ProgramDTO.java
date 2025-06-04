package com.kehutanan.tktrh.bkta.program.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.bkta.program.model.ProgramPaguAnggaran;

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
    private Integer targetGp;
    private Integer targetDpn;
    
    // Related entity IDs instead of full entities
    private Long eselon2Id;
    private String eselon2Nama;
    private Long statusId;
    private String statusNama;
    
    // Lists for pagu anggaran
    private List<ProgramPaguAnggaranDTO> paguAnggarans = new ArrayList<>();
    
    // Constructor to convert from Entity
    public ProgramDTO(Program entity) {
        this.id = entity.getId();
        this.nama = entity.getNama();
        this.tahunPelaksana = entity.getTahunPelaksana();
        this.totalAnggaran = entity.getTotalAnggaran();
        this.targetGp = entity.getTargetGp();
        this.targetDpn = entity.getTargetDpn();
        
        // Set related entity IDs and names
        if (entity.getEselon2() != null) {
            this.eselon2Id = entity.getEselon2().getId();
            this.eselon2Nama = entity.getEselon2().getNama();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
        }
        
        // Convert pagu anggaran list
        if (entity.getPaguAnggarans() != null) {
            for (ProgramPaguAnggaran paguAnggaran : entity.getPaguAnggarans()) {
                this.paguAnggarans.add(new ProgramPaguAnggaranDTO(paguAnggaran));
            }
        }
    }
}