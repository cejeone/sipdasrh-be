package com.kehutanan.tktrh.ppkh.kegiatan.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastReboRehab;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanBastReboRehabDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private Integer tahunId;
    private Double targetLuas;
    private String keterangan;
    private Long statusSerahTerimaId;
    private String statusSerahTerimaNama;
    
    // Constructor to convert from Entity
    public KegiatanBastReboRehabDTO(KegiatanBastReboRehab entity) {
        this.id = entity.getId();
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        this.tahunId = entity.getTahunId();
        this.targetLuas = entity.getTargetLuas();
        this.keterangan = entity.getKeterangan();
        
        if (entity.getStatusSerahTerima() != null) {
            this.statusSerahTerimaId = entity.getStatusSerahTerima().getId();
            this.statusSerahTerimaNama = entity.getStatusSerahTerima().getNama();
        }
    }
}
