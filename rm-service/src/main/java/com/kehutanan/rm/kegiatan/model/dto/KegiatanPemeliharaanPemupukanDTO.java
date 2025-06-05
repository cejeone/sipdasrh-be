package com.kehutanan.rm.kegiatan.model.dto;

import com.kehutanan.rm.kegiatan.model.KegiatanPemeliharaanPemupukan;
import com.kehutanan.rm.master.model.dto.LovDTO;
import lombok.Data;

@Data
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