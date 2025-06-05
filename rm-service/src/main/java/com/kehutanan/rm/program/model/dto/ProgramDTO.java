package com.kehutanan.rm.program.model.dto;

import com.kehutanan.rm.master.model.dto.Eselon2DTO;
import com.kehutanan.rm.master.model.dto.LovDTO;
import com.kehutanan.rm.program.model.Program;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
    private String nama;
    private LovDTO fungsiKawasan;
    private String tahunRencana;
    private Double totalAnggaran;
    private Double totalBibit;
    private Double targetLuas;
    private LovDTO statusId;
    private List<ProgramSkemaDTO> skemas = new ArrayList<>();
    private List<ProgramPaguAnggaranDTO> paguAnggarans = new ArrayList<>();
    private List<ProgramJenisBibitDTO> jenisBibits = new ArrayList<>();

    public ProgramDTO(Program program) {
        this.id = program.getId();
        this.eselon2 = program.getEselon2() != null ? new Eselon2DTO(program.getEselon2()) : null;
        this.kategoriId = program.getKategoriId() != null ? new LovDTO(program.getKategoriId()) : null;
        this.nama = program.getNama();
        this.fungsiKawasan = program.getFungsiKawasan() != null ? new LovDTO(program.getFungsiKawasan()) : null;
        this.tahunRencana = program.getTahunRencana();
        this.totalAnggaran = program.getTotalAnggaran();
        this.totalBibit = program.getTotalBibit();
        this.targetLuas = program.getTargetLuas();
        this.statusId = program.getStatusId() != null ? new LovDTO(program.getStatusId()) : null;
        
        if (program.getSkemas() != null) {
            this.skemas = program.getSkemas().stream()
                .map(ProgramSkemaDTO::new)
                .collect(Collectors.toList());
        }
        
        if (program.getPaguAnggarans() != null) {
            this.paguAnggarans = program.getPaguAnggarans().stream()
                .map(ProgramPaguAnggaranDTO::new)
                .collect(Collectors.toList());
        }
        
        if (program.getJenisBibits() != null) {
            this.jenisBibits = program.getJenisBibits().stream()
                .map(ProgramJenisBibitDTO::new)
                .collect(Collectors.toList());
        }
    }
}