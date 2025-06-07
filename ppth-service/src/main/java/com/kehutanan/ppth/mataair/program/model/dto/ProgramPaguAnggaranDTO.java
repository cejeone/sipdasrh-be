package com.kehutanan.ppth.mataair.program.model.dto;

import com.kehutanan.ppth.master.model.dto.LovDTO;
import com.kehutanan.ppth.mataair.program.model.ProgramPaguAnggaran;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProgramPaguAnggaranDTO {
    private Long id;
    private LovDTO kategoriId;
    private LovDTO sumberAnggaranId;
    private String tahunAnggaran;
    private Double pagu;
    private LovDTO statusId;
    private String keterangan;

    public ProgramPaguAnggaranDTO(ProgramPaguAnggaran entity) {
        if (entity == null) return;
        
        this.id = entity.getId();
        this.pagu = entity.getPagu();
        this.keterangan = entity.getKeterangan();
        
        if (entity.getTahunAnggaran() != null) {
            this.tahunAnggaran = entity.getTahunAnggaran().toString();
        }
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = new LovDTO(entity.getKategoriId());
        }
        
        if (entity.getSumberAnggaranId() != null) {
            this.sumberAnggaranId = new LovDTO(entity.getSumberAnggaranId());
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
    }
}