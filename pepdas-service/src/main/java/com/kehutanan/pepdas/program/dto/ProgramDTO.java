package com.kehutanan.pepdas.program.dto;

import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.model.ProgramPagu;
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
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nama;
    private Integer tahunRencana;
    private Double totalAnggaran;

    // Related entity IDs and names
    private Long eselon1Id;
    private String eselon1Nama;
    private Long kategoriId;
    private String kategoriNama;
    private Long statusId;
    private String statusNama;

    // Pagus list
    private List<ProgramPaguDTO> pagusList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public ProgramDTO(Program entity) {
        this.id = entity.getId();
        this.nama = entity.getNama();
        this.tahunRencana = entity.getTahunRencana();
        this.totalAnggaran = entity.getTotalAnggaran();
        
        // Set related entity IDs and names
        if (entity.getEselon1() != null) {
            this.eselon1Id = entity.getEselon1().getId();
            this.eselon1Nama = entity.getEselon1().getNama();
        }
        
        if (entity.getKategori() != null) {
            this.kategoriId = entity.getKategori().getId();
            this.kategoriNama = entity.getKategori().getNilai();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
        }
        
        // Convert pagus list
        if (entity.getPagus() != null) {
            this.pagusList = entity.getPagus().stream()
                .map(ProgramPaguDTO::new)
                .collect(Collectors.toList());
        }
    }
}