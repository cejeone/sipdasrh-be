package com.kehutanan.tktrh.ppkh.monev.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.tktrh.bkta.program.model.dto.ProgramDTO;
import com.kehutanan.tktrh.master.model.dto.BpdasDTO;
import com.kehutanan.tktrh.ppkh.monev.model.Monev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevDTO {
    private Long id;
    private ProgramDTO program;
    private BpdasDTO bpdas;
    private String tanggal;
    private String subjek;
    private String audiensi;
    private String isuTindakLanjut;
    private List<MonevPdfDTO> monevPdfs = new ArrayList<>();
    
    public MonevDTO(Monev monev) {
        this.id = monev.getId();
        this.program = monev.getProgram() != null ? new ProgramDTO(monev.getProgram()) : null;
        this.bpdas = monev.getBpdas() != null ? new BpdasDTO(monev.getBpdas()) : null;
        this.tanggal = monev.getTanggal() != null ? monev.getTanggal().toString() : null;
        this.subjek = monev.getSubjek();
        this.audiensi = monev.getAudiensi();
        this.isuTindakLanjut = monev.getIsuTindakLanjut();
        
        if (monev.getMonevPdfs() != null) {
            this.monevPdfs = monev.getMonevPdfs().stream()
                .map(MonevPdfDTO::new)
                .collect(Collectors.toList());
        }
    }
}
