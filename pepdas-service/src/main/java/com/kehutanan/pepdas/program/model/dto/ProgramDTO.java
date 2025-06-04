package com.kehutanan.pepdas.program.model.dto;

import com.kehutanan.pepdas.master.model.dto.Eselon1DTO;
import com.kehutanan.pepdas.master.model.dto.LovDTO;
import com.kehutanan.pepdas.program.model.Program;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO {
    
    private Long id;
    private Eselon1DTO eselon1;
    private LovDTO kategori;
    private LovDTO status;
    private String nama;
    private Integer tahunRencana;
    private Double totalAnggaran;
    private List<ProgramPaguDTO> pagus;
    
    public ProgramDTO(Program program) {
        if (program != null) {
            this.id = program.getId();
            if (program.getEselon1() != null) {
                this.eselon1 = new Eselon1DTO(program.getEselon1());
            }
            if (program.getKategori() != null) {
                this.kategori = new LovDTO(program.getKategori());
            }
            if (program.getStatus() != null) {
                this.status = new LovDTO(program.getStatus());
            }
            this.nama = program.getNama();
            this.tahunRencana = program.getTahunRencana();
            this.totalAnggaran = program.getTotalAnggaran();
            
            if (program.getPagus() != null) {
                this.pagus = program.getPagus().stream()
                    .map(ProgramPaguDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.pagus = new ArrayList<>();
            }
        }
    }
}