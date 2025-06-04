package com.kehutanan.rh.program.model.dto;

import com.kehutanan.rh.master.model.dto.LovDTO;
import com.kehutanan.rh.program.model.ProgramPaguAnggaran;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguAnggaranDTO {
    private Long id;
    private LovDTO kategoriId;
    private LovDTO sumberAnggaranId;
    private LovDTO statusId;
    private Integer tahunAnggaran;
    private Double pagu;
    private String keterangan;

    public ProgramPaguAnggaranDTO(ProgramPaguAnggaran entity) {
        this.id = entity.getId();
        this.kategoriId = entity.getKategoriId() != null ? new LovDTO(entity.getKategoriId()) : null;
        this.sumberAnggaranId = entity.getSumberAnggaranId() != null ? new LovDTO(entity.getSumberAnggaranId()) : null;
        this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
        this.tahunAnggaran = entity.getTahunAnggaran();
        this.pagu = entity.getPagu();
        this.keterangan = entity.getKeterangan();
    }
}