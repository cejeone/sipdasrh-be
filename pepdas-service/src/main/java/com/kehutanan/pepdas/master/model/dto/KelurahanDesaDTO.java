package com.kehutanan.pepdas.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.pepdas.master.model.KelurahanDesa;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KelurahanDesaDTO {
    private Long id;
    private String kelurahan;
    private KecamatanDTO kecamatan;
    
    public KelurahanDesaDTO(KelurahanDesa kelurahanDesa) {
        if (kelurahanDesa != null) {
            this.id = kelurahanDesa.getId();
            this.kelurahan = kelurahanDesa.getKelurahan();
            this.kecamatan = kelurahanDesa.getKecamatan() != null ? 
                new KecamatanDTO(kelurahanDesa.getKecamatan()) : null;
        }
    }
}