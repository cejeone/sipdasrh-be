package com.kehutanan.rh.program.model.dto;

import com.kehutanan.rh.master.model.dto.LovDTO;
import com.kehutanan.rh.program.model.ProgramJenisBibit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramJenisBibitDTO {
    private Long id;
    private LovDTO kategoriId;
    private LovDTO namaBibitId;
    private LovDTO sumberBibitId;
    private LovDTO statusId;
    private Integer jumlah;
    private String keterangan;

    public ProgramJenisBibitDTO(ProgramJenisBibit entity) {
        this.id = entity.getId();
        this.kategoriId = entity.getKategoriId() != null ? new LovDTO(entity.getKategoriId()) : null;
        this.namaBibitId = entity.getNamaBibitId() != null ? new LovDTO(entity.getNamaBibitId()) : null;
        this.sumberBibitId = entity.getSumberBibitId() != null ? new LovDTO(entity.getSumberBibitId()) : null;
        this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
        this.jumlah = entity.getJumlah();
        this.keterangan = entity.getKeterangan();
    }
}