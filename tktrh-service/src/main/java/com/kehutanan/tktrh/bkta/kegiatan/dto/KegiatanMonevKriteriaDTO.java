package com.kehutanan.tktrh.bkta.kegiatan.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonevKriteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevKriteriaDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long monitoringEvaluasiId;
    private String aktivitas;
    private String target;
    private String realisasi;
    private String catatan;
    
    // Constructor to convert from Entity
    public KegiatanMonevKriteriaDTO(KegiatanMonevKriteria entity) {
        this.id = entity.getId();
        if (entity.getMonitoringEvaluasi() != null) {
            this.monitoringEvaluasiId = entity.getMonitoringEvaluasi().getId();
        }
        this.aktivitas = entity.getAktivitas();
        this.target = entity.getTarget();
        this.realisasi = entity.getRealisasi();
        this.catatan = entity.getCatatan();
    }
}