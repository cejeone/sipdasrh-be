package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanPemupukan;
import com.kehutanan.rh.master.model.dto.LovDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanPemupukanDTO {
    private Long id;
    private Long kegiatanId;
    private LovDTO jenisId;
    private LovDTO satuanId;
    private LovDTO statusId;
    private String waktuPemupukan;
    private String jumlahPupuk;
    private String keterangan;
    
    public KegiatanPemeliharaanPemupukanDTO(KegiatanPemeliharaanPemupukan entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        if (entity.getJenisId() != null) {
            this.jenisId = new LovDTO(entity.getJenisId());
        }
        
        if (entity.getSatuanId() != null) {
            this.satuanId = new LovDTO(entity.getSatuanId());
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
        
        this.waktuPemupukan = entity.getWaktuPemupukan() != null ? entity.getWaktuPemupukan().toString() : null;
        this.jumlahPupuk = entity.getJumlahPupuk();
        this.keterangan = entity.getKeterangan();
    }
}