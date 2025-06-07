package com.kehutanan.ppth.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.ppth.master.model.Bpth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BpthDTO {
    private Long id;
    private String namaBpth;
    private String alamat;
    private String telepon;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private KelurahanDesaDTO kelurahanDesa;
    
    public BpthDTO(Bpth bpth) {
        if (bpth != null) {
            this.id = bpth.getId();
            this.namaBpth = bpth.getNamaBpth();
            this.alamat = bpth.getAlamat();
            this.telepon = bpth.getTelepon();
            this.provinsi = bpth.getProvinsi() != null ? 
                new ProvinsiDTO(bpth.getProvinsi()) : null;
            this.kabupatenKota = bpth.getKabupatenKota() != null ? 
                new KabupatenKotaDTO(bpth.getKabupatenKota()) : null;
            this.kecamatan = bpth.getKecamatan() != null ? 
                new KecamatanDTO(bpth.getKecamatan()) : null;
            this.kelurahanDesa = bpth.getKelurahanDesa() != null ? 
                new KelurahanDesaDTO(bpth.getKelurahanDesa()) : null;
        }
    }
}