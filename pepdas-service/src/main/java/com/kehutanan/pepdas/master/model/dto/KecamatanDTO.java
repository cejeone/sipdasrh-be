package com.kehutanan.pepdas.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.pepdas.master.model.Kecamatan;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KecamatanDTO {
    private Long id;
    private String kecamatan;
    private String kodeDepdagri;
    private KabupatenKotaDTO kabupatenKota;
    
    public KecamatanDTO(Kecamatan kecamatan) {
        if (kecamatan != null) {
            this.id = kecamatan.getId();
            this.kecamatan = kecamatan.getKecamatan();
            this.kodeDepdagri = kecamatan.getKodeDepdagri();
            this.kabupatenKota = kecamatan.getKabupatenKota() != null ? 
                new KabupatenKotaDTO(kecamatan.getKabupatenKota()) : null;
        }
    }
}