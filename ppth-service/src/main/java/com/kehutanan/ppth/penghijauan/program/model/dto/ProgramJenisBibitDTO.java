package com.kehutanan.ppth.penghijauan.program.model.dto;

import com.kehutanan.ppth.master.model.dto.LovDTO;
import com.kehutanan.ppth.penghijauan.program.model.ProgramJenisBibit;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProgramJenisBibitDTO {
    private Long id;
    private LovDTO namaBibitId;
    private LovDTO sumberBibitId;
    private Integer jumlah;
    private LovDTO statusId;
    private String keterangan;

    public ProgramJenisBibitDTO(ProgramJenisBibit entity) {
        if (entity == null) return;
        
        this.id = entity.getId();
        this.jumlah = entity.getJumlah();
        this.keterangan = entity.getKeterangan();
        
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