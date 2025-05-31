package com.kehutanan.superadmin.master.dto;

import java.io.Serializable;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.superadmin.master.model.KelompokMasyarakat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KelompokMasyarakatDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String namaKelompokMasyarakat;
    private String nomorSkPenetapan;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String tanggalSkPenetapan;
    
    private String alamat;
    private String pic;
    private String telepon;
    
    // Related entity IDs instead of full entities
    private Long provinsiId;
    private String provinsiNama;
    private Long kabupatenKotaId;
    private String kabupatenKotaNama;
    private Long kecamatanId;
    private String kecamatanNama;
    private Long kelurahanDesaId;
    private String kelurahanDesaNama;
    
    // Constructor to convert from Entity
    public KelompokMasyarakatDTO(KelompokMasyarakat entity) {
        this.id = entity.getId();
        this.namaKelompokMasyarakat = entity.getNamaKelompokMasyarakat();
        this.nomorSkPenetapan = entity.getNomorSkPenetapan();
        
        // Convert LocalDate to String
        if (entity.getTanggalSkPenetapan() != null) {
            this.tanggalSkPenetapan = entity.getTanggalSkPenetapan().toString();
        }
        
        this.alamat = entity.getAlamat();
        this.pic = entity.getPic();
        this.telepon = entity.getTelepon();
        
        // Set related entity IDs and names
        if (entity.getProvinsi() != null) {
            this.provinsiId = entity.getProvinsi().getId();
            this.provinsiNama = entity.getProvinsi().getNamaProvinsi();
        }
        
        if (entity.getKabupatenKota() != null) {
            this.kabupatenKotaId = entity.getKabupatenKota().getId();
            this.kabupatenKotaNama = entity.getKabupatenKota().getKabupatenKota();
        }
        
        if (entity.getKecamatan() != null) {
            this.kecamatanId = entity.getKecamatan().getId();
            this.kecamatanNama = entity.getKecamatan().getKecamatan();
        }
        
        if (entity.getKelurahanDesa() != null) {
            this.kelurahanDesaId = entity.getKelurahanDesa().getId();
            this.kelurahanDesaNama = entity.getKelurahanDesa().getKelurahan();
        }
    }
}