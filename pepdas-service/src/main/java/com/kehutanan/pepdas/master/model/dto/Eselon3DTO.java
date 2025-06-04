package com.kehutanan.pepdas.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.pepdas.master.model.Eselon3;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Eselon3DTO {
    private Long id;
    private String nama;
    private String pejabat;
    private String tugasDanFungsi;
    private String keterangan;
    private Eselon2DTO eselon2;
    
    public Eselon3DTO(Eselon3 eselon3) {
        if (eselon3 != null) {
            this.id = eselon3.getId();
            this.nama = eselon3.getNama();
            this.pejabat = eselon3.getPejabat();
            this.tugasDanFungsi = eselon3.getTugasDanFungsi();
            this.keterangan = eselon3.getKeterangan();
            this.eselon2 = eselon3.getEselon2() != null ? 
                new Eselon2DTO(eselon3.getEselon2()) : null;
        }
    }
}