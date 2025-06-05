package com.kehutanan.rm.program.model.dto;

import com.kehutanan.rm.master.model.dto.LovDTO;
import com.kehutanan.rm.program.model.ProgramSkema;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramSkemaDTO {
    private Long id;
    private LovDTO kategoriId;
    private Double skemaBatangHa;
    private Double targetLuas;
    private LovDTO statusId;
    private String keterangan;

    public ProgramSkemaDTO(ProgramSkema programSkema) {
        this.id = programSkema.getId();
        this.kategoriId = programSkema.getKategoriId() != null ? new LovDTO(programSkema.getKategoriId()) : null;
        this.skemaBatangHa = programSkema.getSkemaBatangHa();
        this.targetLuas = programSkema.getTargetLuas();
        this.statusId = programSkema.getStatusId() != null ? new LovDTO(programSkema.getStatusId()) : null;
        this.keterangan = programSkema.getKeterangan();
    }
}