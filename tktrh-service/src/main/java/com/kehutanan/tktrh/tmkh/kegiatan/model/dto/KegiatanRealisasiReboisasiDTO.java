package com.kehutanan.tktrh.tmkh.kegiatan.model.dto;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRealisasiReboisasi;

import lombok.Data;

@Data
public class KegiatanRealisasiReboisasiDTO {
    
    private Long id;
    private LovDTO fungsiKawasanId;
    private Double targetLuas;
    private Double realisasiLuas;
    private Integer tahunId;
    private String keterangan;
    private LovDTO statusId;
    
    public KegiatanRealisasiReboisasiDTO(KegiatanRealisasiReboisasi entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.fungsiKawasanId = entity.getFungsiKawasanId() != null ? new LovDTO(entity.getFungsiKawasanId()) : null;
            this.targetLuas = entity.getTargetLuas();
            this.realisasiLuas = entity.getRealisasiLuas();
            this.tahunId = entity.getTahunId();
            this.keterangan = entity.getKeterangan();
            this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
        }
    }
}
