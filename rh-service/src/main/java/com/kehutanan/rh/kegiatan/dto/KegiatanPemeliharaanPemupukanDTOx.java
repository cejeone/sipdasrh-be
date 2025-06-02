package com.kehutanan.rh.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanPemupukan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanPemupukanDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    // Related entity IDs instead of full entities
    private Long kegiatanId;
    private String kegiatanNama;
    
    private Long jenisId;
    private String jenisNama;
    
    private Long satuanId;
    private String satuanNama;
    
    private Long statusId;
    private String statusNama;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate waktuPemupukan;
    
    private String jumlahPupuk;
    private String keterangan;
    
    // Constructor to convert from Entity
    public KegiatanPemeliharaanPemupukanDTOx(KegiatanPemeliharaanPemupukan entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            // Assuming Kegiatan has a name field, adjust if different
            // this.kegiatanNama = entity.getKegiatan().getNama();
        }
        
        if (entity.getJenisId() != null) {
            this.jenisId = entity.getJenisId().getId();
            this.jenisNama = entity.getJenisId().getNilai();
        }
        
        if (entity.getSatuanId() != null) {
            this.satuanId = entity.getSatuanId().getId();
            this.satuanNama = entity.getSatuanId().getNilai();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        this.waktuPemupukan = entity.getWaktuPemupukan();
        this.jumlahPupuk = entity.getJumlahPupuk();
        this.keterangan = entity.getKeterangan();
    }
}