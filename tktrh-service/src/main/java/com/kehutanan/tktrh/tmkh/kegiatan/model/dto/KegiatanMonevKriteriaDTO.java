package com.kehutanan.tktrh.tmkh.kegiatan.model.dto;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonevKriteria;

import lombok.Data;

@Data
public class KegiatanMonevKriteriaDTO {
    
    private Long id;
    private LovDTO aktivitasId;
    private Double targetLuas;
    private Double realisasiLuas;
    private String catatan;
    
    public KegiatanMonevKriteriaDTO(KegiatanMonevKriteria entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.aktivitasId = entity.getAktivitasId() != null ? new LovDTO(entity.getAktivitasId()) : null;
            this.targetLuas = entity.getTargetLuas();
            this.realisasiLuas = entity.getRealisasiLuas();
            this.catatan = entity.getCatatan();
        }
    }
}
