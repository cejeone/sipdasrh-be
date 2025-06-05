package com.kehutanan.tktrh.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.tktrh.master.model.PelakuUsaha;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PelakuUsahaDTO {
    private Long id;
    private String namaBadanUsaha;
    private String nomorIndukBerusahaNib;
    private String nomorSertifikatStandar;
    private String ruangLingkupUsaha;
    private String namaDirektur;
    private String nomorHpDirektur;
    private String alamat;
    private LovDTO kategoriPelakuUsaha;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private KelurahanDesaDTO kelurahanDesa;
    
    public PelakuUsahaDTO(PelakuUsaha pelakuUsaha) {
        if (pelakuUsaha != null) {
            this.id = pelakuUsaha.getId();
            this.namaBadanUsaha = pelakuUsaha.getNamaBadanUsaha();
            this.nomorIndukBerusahaNib = pelakuUsaha.getNomorIndukBerusahaNib();
            this.nomorSertifikatStandar = pelakuUsaha.getNomorSertifikatStandar();
            this.ruangLingkupUsaha = pelakuUsaha.getRuangLingkupUsaha();
            this.namaDirektur = pelakuUsaha.getNamaDirektur();
            this.nomorHpDirektur = pelakuUsaha.getNomorHpDirektur();
            this.alamat = pelakuUsaha.getAlamat();
            
            this.kategoriPelakuUsaha = pelakuUsaha.getKategoriPelakuUsaha() != null ? 
                new LovDTO(pelakuUsaha.getKategoriPelakuUsaha()) : null;
                
            this.provinsi = pelakuUsaha.getProvinsi() != null ? 
                new ProvinsiDTO(pelakuUsaha.getProvinsi()) : null;
            this.kabupatenKota = pelakuUsaha.getKabupatenKota() != null ? 
                new KabupatenKotaDTO(pelakuUsaha.getKabupatenKota()) : null;
            this.kecamatan = pelakuUsaha.getKecamatan() != null ? 
                new KecamatanDTO(pelakuUsaha.getKecamatan()) : null;
            this.kelurahanDesa = pelakuUsaha.getKelurahanDesa() != null ? 
                new KelurahanDesaDTO(pelakuUsaha.getKelurahanDesa()) : null;
        }
    }
}