package com.kehutanan.rh.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rh.master.model.Institusi;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitusiDTO {
    private Long id;
    private String nama;
    private String email;
    private String nomorTelepon;
    private String website;
    private String alamat;
    private String kodePos;
    private LovDTO tipeInstitusi;
    private LovDTO tipeAkreditasi;
    private LovDTO status;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    
    public InstitusiDTO(Institusi institusi) {
        if (institusi != null) {
            this.id = institusi.getId();
            this.nama = institusi.getNama();
            this.email = institusi.getEmail();
            this.nomorTelepon = institusi.getNomorTelepon();
            this.website = institusi.getWebsite();
            this.alamat = institusi.getAlamat();
            this.kodePos = institusi.getKodePos();
            
            this.tipeInstitusi = institusi.getTipeInstitusi() != null ? 
                new LovDTO(institusi.getTipeInstitusi()) : null;
            this.tipeAkreditasi = institusi.getTipeAkreditasi() != null ? 
                new LovDTO(institusi.getTipeAkreditasi()) : null;
            this.status = institusi.getStatus() != null ? 
                new LovDTO(institusi.getStatus()) : null;
            
            this.provinsi = institusi.getProvinsi() != null ? 
                new ProvinsiDTO(institusi.getProvinsi()) : null;
            this.kabupatenKota = institusi.getKabupatenKota() != null ? 
                new KabupatenKotaDTO(institusi.getKabupatenKota()) : null;
            this.kecamatan = institusi.getKecamatan() != null ? 
                new KecamatanDTO(institusi.getKecamatan()) : null;
        }
    }
}