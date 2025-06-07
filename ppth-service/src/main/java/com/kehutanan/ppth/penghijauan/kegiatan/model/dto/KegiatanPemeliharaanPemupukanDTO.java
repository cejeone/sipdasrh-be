package com.kehutanan.ppth.penghijauan.kegiatan.model.dto;

import com.kehutanan.ppth.penghijauan.kegiatan.model.KegiatanPemeliharaanPemupukan;
import com.kehutanan.ppth.master.model.dto.LovDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KegiatanPemeliharaanPemupukanDTO {
    
    private Long id;
    private LovDTO jenisId;
    private String waktuPemupukan;
    private String jumlahPupuk;
    private LovDTO satuanId;
    private String keterangan;
    private LovDTO statusId;
    
    public KegiatanPemeliharaanPemupukanDTO(KegiatanPemeliharaanPemupukan entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.jenisId = entity.getJenisId() != null ? new LovDTO(entity.getJenisId()) : null;
            this.waktuPemupukan = entity.getWaktuPemupukan() != null ? entity.getWaktuPemupukan().toString() : null;
            this.jumlahPupuk = entity.getJumlahPupuk();
            this.satuanId = entity.getSatuanId() != null ? new LovDTO(entity.getSatuanId()) : null;
            this.keterangan = entity.getKeterangan();
            this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
        }
    }
}
