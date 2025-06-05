package com.kehutanan.rh.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rh.master.model.Spas;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpasDTO {
    private Long id;
    private String spas;
    private String keterangan;
    private String alamat;
    private BigDecimal lintang;
    private BigDecimal bujur;
    private BpdasDTO bpdas;
    private DasDTO das;
    private LovDTO tipeSpas;
    private LovDTO frekuensiPengirimanData;
    private LovDTO kanalData;
    private LovDTO status;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private KelurahanDesaDTO kelurahanDesa;
    
    public SpasDTO(Spas spas) {
        if (spas != null) {
            this.id = spas.getId();
            this.spas = spas.getSpas();
            this.keterangan = spas.getKeterangan();
            this.alamat = spas.getAlamat();
            this.lintang = spas.getLintang();
            this.bujur = spas.getBujur();
            
            this.bpdas = spas.getBpdas() != null ? 
                new BpdasDTO(spas.getBpdas()) : null;
            this.das = spas.getDas() != null ? 
                new DasDTO(spas.getDas()) : null;
            this.tipeSpas = spas.getTipeSpas() != null ? 
                new LovDTO(spas.getTipeSpas()) : null;
            this.frekuensiPengirimanData = spas.getFrekuensiPengirimanData() != null ? 
                new LovDTO(spas.getFrekuensiPengirimanData()) : null;
            this.kanalData = spas.getKanalData() != null ? 
                new LovDTO(spas.getKanalData()) : null;
            this.status = spas.getStatus() != null ? 
                new LovDTO(spas.getStatus()) : null;
                
            this.provinsi = spas.getProvinsi() != null ? 
                new ProvinsiDTO(spas.getProvinsi()) : null;
            this.kabupatenKota = spas.getKabupatenKota() != null ? 
                new KabupatenKotaDTO(spas.getKabupatenKota()) : null;
            this.kecamatan = spas.getKecamatan() != null ? 
                new KecamatanDTO(spas.getKecamatan()) : null;
            this.kelurahanDesa = spas.getKelurahanDesa() != null ? 
                new KelurahanDesaDTO(spas.getKelurahanDesa()) : null;
        }
    }
}