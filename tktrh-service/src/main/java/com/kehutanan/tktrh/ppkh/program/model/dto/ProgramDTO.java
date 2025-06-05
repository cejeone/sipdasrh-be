package com.kehutanan.tktrh.ppkh.program.model.dto;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.ppkh.program.model.Program;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO implements Serializable {
    
    private Long id;
    private String nama;
    private Integer tahunPelaksana;
    private Double totalAnggaran;
    private Integer totalLuas;
    private LovDTO status;
    private List<ProgramPaguAnggaranDTO> paguAnggarans = new ArrayList<>();
    
    public ProgramDTO(Program entity) {
        if (entity == null) return;
        
        this.id = entity.getId();
        this.nama = entity.getNama();
        this.tahunPelaksana = entity.getTahunPelaksana();
        this.totalAnggaran = entity.getTotalAnggaran();
        this.totalLuas = entity.getTotalLuas();
        if (entity.getStatus() != null) {
            this.status = new LovDTO(entity.getStatus());
        }
        if (entity.getPaguAnggarans() != null) {
            this.paguAnggarans = entity.getPaguAnggarans().stream()
                .map(ProgramPaguAnggaranDTO::new)
                .collect(Collectors.toList());
        }
    }
}
