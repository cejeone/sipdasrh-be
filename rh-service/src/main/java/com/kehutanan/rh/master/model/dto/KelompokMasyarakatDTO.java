package com.kehutanan.rh.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rh.master.model.KelompokMasyarakat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KelompokMasyarakatDTO {
    private Long id;
    private String namaKelompokMasyarakat;
    private String nomorSkPenetapan;
    private LocalDate tanggalSkPenetapan;
    private String alamat;
    private String pic;
    private String telepon;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private KelurahanDesaDTO kelurahanDesa;
    
    public KelompokMasyarakatDTO(KelompokMasyarakat kelompokMasyarakat) {
        if (kelompokMasyarakat != null) {
            this.id = kelompokMasyarakat.getId();
            this.namaKelompokMasyarakat = kelompokMasyarakat.getNamaKelompokMasyarakat();
            this.nomorSkPenetapan = kelompokMasyarakat.getNomorSkPenetapan();
            this.tanggalSkPenetapan = kelompokMasyarakat.getTanggalSkPenetapan();
            this.alamat = kelompokMasyarakat.getAlamat();
            this.pic = kelompokMasyarakat.getPic();
            this.telepon = kelompokMasyarakat.getTelepon();
            
            this.provinsi = kelompokMasyarakat.getProvinsi() != null ? 
                new ProvinsiDTO(kelompokMasyarakat.getProvinsi()) : null;
            this.kabupatenKota = kelompokMasyarakat.getKabupatenKota() != null ? 
                new KabupatenKotaDTO(kelompokMasyarakat.getKabupatenKota()) : null;
            this.kecamatan = kelompokMasyarakat.getKecamatan() != null ? 
                new KecamatanDTO(kelompokMasyarakat.getKecamatan()) : null;
            this.kelurahanDesa = kelompokMasyarakat.getKelurahanDesa() != null ? 
                new KelurahanDesaDTO(kelompokMasyarakat.getKelurahanDesa()) : null;
        }
    }
}