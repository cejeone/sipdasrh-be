package com.kehutanan.ppth.mataair.program.model.dto;

import com.kehutanan.ppth.master.model.dto.LovDTO;
import com.kehutanan.ppth.mataair.program.model.ProgramSkema;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProgramSkemaDTO {
    private Long id;
    private LovDTO kategoriId;
    private Double skemaBatangHa;
    private Double targetLuas;
    private LovDTO statusId;
    private String keterangan;

    public ProgramSkemaDTO(ProgramSkema entity) {
        if (entity == null) return;
        
        this.id = entity.getId();
        this.skemaBatangHa = entity.getSkemaBatangHa();
        this.targetLuas = entity.getTargetLuas();
        this.keterangan = entity.getKeterangan();
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = new LovDTO(entity.getKategoriId());
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
    }
}