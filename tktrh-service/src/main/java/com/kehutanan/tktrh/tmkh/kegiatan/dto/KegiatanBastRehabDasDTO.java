package com.kehutanan.tktrh.tmkh.kegiatan.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.master.dto.LovDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastRehabDas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanBastRehabDasDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private Integer tahunId;
    private Double targetLuas;
    private String keterangan;
    
    // Status serah terima data
    private Long statusSerahTerimaId;
    private String statusSerahTerimaKode;
    private String statusSerahTerimaNama;
    
    // Constructor to convert from Entity
    public KegiatanBastRehabDasDTO(KegiatanBastRehabDas entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        this.tahunId = entity.getTahunId();
        this.targetLuas = entity.getTargetLuas();
        this.keterangan = entity.getKeterangan();
        
        if (entity.getStatusSerahTerima() != null) {
            this.statusSerahTerimaId = entity.getStatusSerahTerima().getId();
            this.statusSerahTerimaKode = entity.getStatusSerahTerima().getKode();
            this.statusSerahTerimaNama = entity.getStatusSerahTerima().getNama();
        }
    }
}
