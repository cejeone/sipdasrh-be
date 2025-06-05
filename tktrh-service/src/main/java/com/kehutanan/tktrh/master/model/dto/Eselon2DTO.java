package com.kehutanan.tktrh.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.tktrh.master.model.Eselon2;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Eselon2DTO {
    private Long id;
    private String nama;
    private String pejabat;
    private String tugasDanFungsi;
    private String keterangan;
    private Eselon1DTO eselon1;
    
    public Eselon2DTO(Eselon2 eselon2) {
        if (eselon2 != null) {
            this.id = eselon2.getId();
            this.nama = eselon2.getNama();
            this.pejabat = eselon2.getPejabat();
            this.tugasDanFungsi = eselon2.getTugasDanFungsi();
            this.keterangan = eselon2.getKeterangan();
            this.eselon1 = eselon2.getEselon1() != null ? 
                new Eselon1DTO(eselon2.getEselon1()) : null;
        }
    }
}