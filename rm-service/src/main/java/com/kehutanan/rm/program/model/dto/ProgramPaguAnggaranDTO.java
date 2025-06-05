package com.kehutanan.rm.program.model.dto;

import com.kehutanan.rm.master.model.dto.LovDTO;
import com.kehutanan.rm.program.model.ProgramPaguAnggaran;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguAnggaranDTO {
    private Long id;
    private LovDTO kategoriId;
    private LovDTO sumberAnggaranId;
    private Integer tahunAnggaran;
    private Double pagu;
    private LovDTO statusId;
    private String keterangan;

    public ProgramPaguAnggaranDTO(ProgramPaguAnggaran programPaguAnggaran) {
        this.id = programPaguAnggaran.getId();
        this.kategoriId = programPaguAnggaran.getKategoriId() != null ? new LovDTO(programPaguAnggaran.getKategoriId()) : null;
        this.sumberAnggaranId = programPaguAnggaran.getSumberAnggaranId() != null ? new LovDTO(programPaguAnggaran.getSumberAnggaranId()) : null;
        this.tahunAnggaran = programPaguAnggaran.getTahunAnggaran();
        this.pagu = programPaguAnggaran.getPagu();
        this.statusId = programPaguAnggaran.getStatusId() != null ? new LovDTO(programPaguAnggaran.getStatusId()) : null;
        this.keterangan = programPaguAnggaran.getKeterangan();
    }
}