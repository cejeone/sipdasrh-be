package com.kehutanan.pepdas.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.pepdas.master.model.Provinsi;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinsiDTO {
    private Long id;
    private String namaProvinsi;
    private String kodeDepdagri;
    
    public ProvinsiDTO(Provinsi provinsi) {
        if (provinsi != null) {
            this.id = provinsi.getId();
            this.namaProvinsi = provinsi.getNamaProvinsi();
            this.kodeDepdagri = provinsi.getKodeDepdagri();
        }
    }
}