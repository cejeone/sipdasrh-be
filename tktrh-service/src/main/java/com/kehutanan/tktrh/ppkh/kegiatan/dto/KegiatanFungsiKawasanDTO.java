package com.kehutanan.tktrh.ppkh.kegiatan.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanFungsiKawasan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanFungsiKawasanDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private Long fungsiKawasanId;
    private String fungsiKawasanNama;
    private Double targetLuas;
    private Double realisasiLuas;
    private Integer tahunId;
    private String keterangan;
    private Long statusId;
    private String statusNama;
    
    // Constructor to convert from Entity
    public KegiatanFungsiKawasanDTO(KegiatanFungsiKawasan entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        if (entity.getFungsiKawasanId() != null) {
            this.fungsiKawasanId = entity.getFungsiKawasanId().getId();
            this.fungsiKawasanNama = entity.getFungsiKawasanId().getNama();
        }
        
        this.targetLuas = entity.getTargetLuas();
        this.realisasiLuas = entity.getRealisasiLuas();
        this.tahunId = entity.getTahunId();
        this.keterangan = entity.getKeterangan();
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNama();
        }
    }
}
