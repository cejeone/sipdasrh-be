package com.kehutanan.tktrh.tmkh.kegiatan.model.dto;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastRehabDas;

import lombok.Data;

@Data
public class KegiatanBastRehabDasDTO {
    
    private Long id;
    private Integer tahunId;
    private Double targetLuas;
    private String keterangan;
    private LovDTO statusSerahTerima;
    
    public KegiatanBastRehabDasDTO(KegiatanBastRehabDas entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.tahunId = entity.getTahunId();
            this.targetLuas = entity.getTargetLuas();
            this.keterangan = entity.getKeterangan();
            this.statusSerahTerima = entity.getStatusSerahTerima() != null 
                ? new LovDTO(entity.getStatusSerahTerima()) : null;
        }
    }
}
