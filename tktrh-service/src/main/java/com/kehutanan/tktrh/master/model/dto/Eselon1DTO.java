package com.kehutanan.tktrh.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.tktrh.master.model.Eselon1;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Eselon1DTO {
    private Long id;
    private String nama;
    private String pejabat;
    private String tugasDanFungsi;
    private String keterangan;
    
    public Eselon1DTO(Eselon1 eselon1) {
        if (eselon1 != null) {
            this.id = eselon1.getId();
            this.nama = eselon1.getNama();
            this.pejabat = eselon1.getPejabat();
            this.tugasDanFungsi = eselon1.getTugasDanFungsi();
            this.keterangan = eselon1.getKeterangan();
        }
    }
}