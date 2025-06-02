package com.kehutanan.rh.kegiatan.dto;

import java.io.Serializable;

import com.kehutanan.rh.kegiatan.model.KegiatanFungsiKawasan;

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
    private String kegiatanNama;
    private Long fungsiKawasanId;
    private String fungsiKawasanNama;
    private Long statusId;
    private String statusNama;
    private Double targetLuas;
    private Double realisasiLuas;
    private Integer tahunId;
    private String keterangan;
    
    // Constructor to convert from Entity
    public KegiatanFungsiKawasanDTO(KegiatanFungsiKawasan entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            // Assuming Kegiatan has a name field
            this.kegiatanNama = entity.getKegiatan().getNamaKegiatan();
        }
        
        if (entity.getFungsiKawasanId() != null) {
            this.fungsiKawasanId = entity.getFungsiKawasanId().getId();
            this.fungsiKawasanNama = entity.getFungsiKawasanId().getNilai();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        this.targetLuas = entity.getTargetLuas();
        this.realisasiLuas = entity.getRealisasiLuas();
        this.tahunId = entity.getTahunId();
        this.keterangan = entity.getKeterangan();
    }
}