package com.kehutanan.rm.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.master.model.Lov;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LovDTO {
    private Long id;
    private String namaKategori;
    private String nilai;
    private String kelas;
    private String deskripsi;
    private String status;
    
    public LovDTO(Lov lov) {
        if (lov != null) {
            this.id = lov.getId();
            this.namaKategori = lov.getNamaKategori();
            this.nilai = lov.getNilai();
            this.kelas = lov.getKelas();
            this.deskripsi = lov.getDeskripsi();
            this.status = lov.getStatus();
        }
    }
}