package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.rh.master.model.dto.LovDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevKriteriaDTO {
    private Long id;
    private Long kegiatanMonevId;
    private LovDTO aktivitasId;
    private Double targetLuas;
    private Double realisasiLuas;
    private String catatan;
    
    public KegiatanMonevKriteriaDTO(KegiatanMonevKriteria entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatanMonev() != null) {
            this.kegiatanMonevId = entity.getKegiatanMonev().getId();
        }
        
        if (entity.getAktivitasId() != null) {
            this.aktivitasId = new LovDTO(entity.getAktivitasId());
        }
        
        this.targetLuas = entity.getTargetLuas();
        this.realisasiLuas = entity.getRealisasiLuas();
        this.catatan = entity.getCatatan();
    }
}