package com.kehutanan.rm.program.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rm.program.model.Program;
import com.kehutanan.rm.program.model.ProgramJenisBibit;
import com.kehutanan.rm.program.model.ProgramPaguAnggaran;
import com.kehutanan.rm.program.model.ProgramSkema;

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
    private String tahunRencana; // ganti dari tahunPelaksana
    private Double totalAnggaran;
    private Double targetLuas;
    private Double totalBibit;
    private Long fungsiKawasanId;
    private String fungsiKawasanNama;
    
    // Related entity IDs instead of full entities
    private Long eselon2Id;
    private String eselon2Nama;
    
    private Long kategoriId;
    private String kategoriNama;
    
    private Long statusId;
    private String statusNama;
    
    // Lists for related data
    private List<ProgramSkemaDTO> skemas = new ArrayList<>();
    private List<ProgramPaguAnggaranDTO> paguAnggarans = new ArrayList<>();
    private List<ProgramJenisBibitDTO> jenisBibits = new ArrayList<>();
    
    // Constructor to convert from Entity
    public ProgramDTO(Program entity) {
        this.id = entity.getId();
        this.nama = entity.getNama();
        this.tahunRencana = entity.getTahunRencana();
        this.totalAnggaran = entity.getTotalAnggaran();
        this.targetLuas = entity.getTargetLuas();
        this.totalBibit = entity.getTotalBibit();
        if (entity.getFungsiKawasan() != null) {
            this.fungsiKawasanId = entity.getFungsiKawasan().getId();
            this.fungsiKawasanNama = entity.getFungsiKawasan().getNilai();
        }
        
        // Set related entity IDs and names
        if (entity.getEselon2() != null) {
            this.eselon2Id = entity.getEselon2().getId();
            this.eselon2Nama = entity.getEselon2().getNama();
        }
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = entity.getKategoriId().getId();
            this.kategoriNama = entity.getKategoriId().getNilai();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        // Convert related lists
        if (entity.getSkemas() != null) {
            for (ProgramSkema skema : entity.getSkemas()) {
                this.skemas.add(new ProgramSkemaDTO(skema));
            }
        }
        
        if (entity.getPaguAnggarans() != null) {
            for (ProgramPaguAnggaran paguAnggaran : entity.getPaguAnggarans()) {
                this.paguAnggarans.add(new ProgramPaguAnggaranDTO(paguAnggaran));
            }
        }
        
        if (entity.getJenisBibits() != null) {
            for (ProgramJenisBibit jenisBibit : entity.getJenisBibits()) {
                this.jenisBibits.add(new ProgramJenisBibitDTO(jenisBibit));
            }
        }
    }
}