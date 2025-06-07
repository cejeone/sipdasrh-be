package com.kehutanan.ppth.penghijauan.program.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.kehutanan.ppth.master.model.dto.LovDTO;
import com.kehutanan.ppth.penghijauan.program.model.Program;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProgramDTO {
    private Long id;
    private LovDTO kategoriId;
    private String nama;
    private LovDTO fungsiKawasan;
    private String tahunRencana;
    private Double totalAnggaran;
    private Integer totalBibit;
    private Double targetLuas;
    private LovDTO statusId;
    private List<ProgramSkemaDTO> skemas = new ArrayList<>();
    private List<ProgramPaguAnggaranDTO> paguAnggarans = new ArrayList<>();
    private List<ProgramJenisBibitDTO> jenisBibits = new ArrayList<>();

    public ProgramDTO(Program entity) {
        if (entity == null) return;
        
        this.id = entity.getId();
        this.nama = entity.getNama();
        this.totalAnggaran = entity.getTotalAnggaran();
        this.totalBibit = entity.getTotalBibit();
        this.targetLuas = entity.getTargetLuas();
        
        if (entity.getTahunRencana() != null) {
            this.tahunRencana = entity.getTahunRencana().toString();
        }
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = new LovDTO(entity.getKategoriId());
        }
        
        if (entity.getFungsiKawasan() != null) {
            this.fungsiKawasan = new LovDTO(entity.getFungsiKawasan());
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
        
        if (entity.getSkemas() != null) {
            entity.getSkemas().forEach(skema -> this.skemas.add(new ProgramSkemaDTO(skema)));
        }
        
        if (entity.getPaguAnggarans() != null) {
            entity.getPaguAnggarans().forEach(pagu -> this.paguAnggarans.add(new ProgramPaguAnggaranDTO(pagu)));
        }
        
        if (entity.getJenisBibits() != null) {
            entity.getJenisBibits().forEach(bibit -> this.jenisBibits.add(new ProgramJenisBibitDTO(bibit)));
        }
    }
}