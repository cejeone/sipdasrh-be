package com.kehutanan.rm.kegiatan.model.dto;

import com.kehutanan.rm.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.rm.master.model.dto.LovDTO;
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