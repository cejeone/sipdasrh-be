package com.kehutanan.rm.program.model.dto;

import com.kehutanan.rm.master.model.dto.LovDTO;
import com.kehutanan.rm.program.model.ProgramJenisBibit;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramJenisBibitDTO {
    private Long id;
    private LovDTO kategoriId;
    private LovDTO namaBibitId;
    private LovDTO sumberBibitId;
    private Integer jumlahBibit;
    private LovDTO statusId;
    private String keterangan;

    public ProgramJenisBibitDTO(ProgramJenisBibit programJenisBibit) {
        this.id = programJenisBibit.getId();
        this.kategoriId = programJenisBibit.getKategoriId() != null ? new LovDTO(programJenisBibit.getKategoriId()) : null;
        this.namaBibitId = programJenisBibit.getNamaBibitId() != null ? new LovDTO(programJenisBibit.getNamaBibitId()) : null;
        this.sumberBibitId = programJenisBibit.getSumberBibitId() != null ? new LovDTO(programJenisBibit.getSumberBibitId()) : null;
        this.jumlahBibit = programJenisBibit.getJumlahBibit();
        this.statusId = programJenisBibit.getStatusId() != null ? new LovDTO(programJenisBibit.getStatusId()) : null;
        this.keterangan = programJenisBibit.getKeterangan();
    }
}