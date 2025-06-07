package com.kehutanan.rh.program.model.dto;

import com.kehutanan.rh.master.model.dto.Eselon2DTO;
import com.kehutanan.rh.master.model.dto.LovDTO;
import com.kehutanan.rh.program.model.Program;
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
    private Eselon2DTO eselon2;
    private LovDTO kategoriId;
    private LovDTO statusId;
    private String nama;
    private Integer tahunPelaksana;
    private Double totalAnggaran;
    private Double targetLuas;
    private List<ProgramSkemaDTO> skemas = new ArrayList<>();
    private List<ProgramPaguAnggaranDTO> paguAnggarans = new ArrayList<>();
    private List<ProgramJenisBibitDTO> jenisBibits = new ArrayList<>();

    public ProgramDTO(Program entity) {
        this.id = entity.getId();
        this.eselon2 = entity.getEselon2() != null ? new Eselon2DTO(entity.getEselon2()) : null;
        this.kategoriId = entity.getKategoriId() != null ? new LovDTO(entity.getKategoriId()) : null;
        this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
        this.nama = entity.getNama();
        this.tahunPelaksana = entity.getTahunPelaksana();
        this.totalAnggaran = entity.getTotalAnggaran();
        this.targetLuas = entity.getTargetLuas();
        
        if (entity.getSkemas() != null) {
            this.skemas = entity.getSkemas().stream()
                .map(ProgramSkemaDTO::new)
                .collect(Collectors.toList());
        }
        
        if (entity.getPaguAnggarans() != null) {
            this.paguAnggarans = entity.getPaguAnggarans().stream()
                .map(ProgramPaguAnggaranDTO::new)
                .collect(Collectors.toList());
        }
        
        if (entity.getJenisBibits() != null) {
            this.jenisBibits = entity.getJenisBibits().stream()
                .map(ProgramJenisBibitDTO::new)
                .collect(Collectors.toList());
        }
    }
}