package com.kehutanan.rh.program.model.dto;

import com.kehutanan.rh.master.model.dto.LovDTO;
import com.kehutanan.rh.program.model.ProgramSkema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramSkemaDTO {
    private Long id;
    private LovDTO kategoriId;
    private LovDTO statusId;
    private Integer skema;
    private Double targetLuas;
    private String keterangan;

    public ProgramSkemaDTO(ProgramSkema entity) {
        this.id = entity.getId();
        this.kategoriId = entity.getKategoriId() != null ? new LovDTO(entity.getKategoriId()) : null;
        this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
        this.skema = entity.getSkema();
        this.targetLuas = entity.getTargetLuas();
        this.keterangan = entity.getKeterangan();
    }
}