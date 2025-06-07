package com.kehutanan.ppth.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.ppth.master.model.KabupatenKota;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KabupatenKotaDTO {
    private Long id;
    private String kabupatenKota;
    private String kodeDepdagri;
    private ProvinsiDTO provinsi;
    
    public KabupatenKotaDTO(KabupatenKota kabupatenKota) {
        if (kabupatenKota != null) {
            this.id = kabupatenKota.getId();
            this.kabupatenKota = kabupatenKota.getKabupatenKota();
            this.kodeDepdagri = kabupatenKota.getKodeDepdagri();
            this.provinsi = kabupatenKota.getProvinsi() != null ? 
                new ProvinsiDTO(kabupatenKota.getProvinsi()) : null;
        }
    }
}