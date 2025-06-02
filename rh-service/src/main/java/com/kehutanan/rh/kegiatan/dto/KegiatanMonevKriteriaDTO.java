package com.kehutanan.rh.kegiatan.dto;

import java.io.Serializable;

import com.kehutanan.rh.kegiatan.model.KegiatanMonev;
import com.kehutanan.rh.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.rh.master.model.Lov;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevKriteriaDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanMonevId;
    private Long aktivitasId;
    private String aktivitasNama;
    private Double targetLuas;
    private Double realisasiLuas;
    private String catatan;
    
    // Constructor to convert from Entity
    public KegiatanMonevKriteriaDTO(KegiatanMonevKriteria entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatanMonev() != null) {
            this.kegiatanMonevId = entity.getKegiatanMonev().getId();
        }
        
        if (entity.getAktivitasId() != null) {
            this.aktivitasId = entity.getAktivitasId().getId();
            this.aktivitasNama = entity.getAktivitasId().getNilai();
        }
        
        this.targetLuas = entity.getTargetLuas();
        this.realisasiLuas = entity.getRealisasiLuas();
        this.catatan = entity.getCatatan();
    }
}