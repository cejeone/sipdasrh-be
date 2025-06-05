package com.kehutanan.tktrh.bkta.kegiatan.model.dto;

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonevKriteria;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KegiatanMonevKriteriaDTO {

    private Long id;
    private String aktivitas;
    private String target;
    private String realisasi;
    private String catatan;

    public KegiatanMonevKriteriaDTO(KegiatanMonevKriteria entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.aktivitas = entity.getAktivitas();
            this.target = entity.getTarget();
            this.realisasi = entity.getRealisasi();
            this.catatan = entity.getCatatan();
        }
    }
}
