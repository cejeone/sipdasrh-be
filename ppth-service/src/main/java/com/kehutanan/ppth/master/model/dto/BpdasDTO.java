package com.kehutanan.ppth.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.ppth.master.model.Bpdas;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BpdasDTO {
    private Long id;
    private String namaBpdas;
    private String alamat;
    private String telepon;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private KelurahanDesaDTO kelurahanDesa;
    
    public BpdasDTO(Bpdas bpdas) {
        if (bpdas != null) {
            this.id = bpdas.getId();
            this.namaBpdas = bpdas.getNamaBpdas();
            this.alamat = bpdas.getAlamat();
            this.telepon = bpdas.getTelepon();
            this.provinsi = bpdas.getProvinsi() != null ? 
                new ProvinsiDTO(bpdas.getProvinsi()) : null;
            this.kabupatenKota = bpdas.getKabupatenKota() != null ? 
                new KabupatenKotaDTO(bpdas.getKabupatenKota()) : null;
            this.kecamatan = bpdas.getKecamatan() != null ? 
                new KecamatanDTO(bpdas.getKecamatan()) : null;
            this.kelurahanDesa = bpdas.getKelurahanDesa() != null ? 
                new KelurahanDesaDTO(bpdas.getKelurahanDesa()) : null;
        }
    }
}