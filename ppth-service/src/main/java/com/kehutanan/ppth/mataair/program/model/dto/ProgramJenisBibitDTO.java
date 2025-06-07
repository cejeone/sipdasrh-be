package com.kehutanan.ppth.mataair.program.model.dto;

import com.kehutanan.ppth.master.model.dto.LovDTO;
import com.kehutanan.ppth.mataair.program.model.ProgramJenisBibit;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProgramJenisBibitDTO {
    private Long id;
    private LovDTO kategoriId;
    private LovDTO namaBibitId;
    private LovDTO sumberBibitId;
    private LovDTO statusId;
    private Integer jumlah;
    private String keterangan;

    public ProgramJenisBibitDTO(ProgramJenisBibit entity) {
        if (entity == null) return;
        
        this.id = entity.getId();
        this.jumlah = entity.getJumlah();
        this.keterangan = entity.getKeterangan();
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = new LovDTO(entity.getKategoriId());
        }
        
        if (entity.getNamaBibitId() != null) {
            this.namaBibitId = new LovDTO(entity.getNamaBibitId());
        }
        
        if (entity.getSumberBibitId() != null) {
            this.sumberBibitId = new LovDTO(entity.getSumberBibitId());
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
    }
}