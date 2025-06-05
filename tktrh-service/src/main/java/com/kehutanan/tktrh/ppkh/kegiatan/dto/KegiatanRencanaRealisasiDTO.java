package com.kehutanan.tktrh.ppkh.kegiatan.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRencanaRealisasi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRencanaRealisasiDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private String kegiatanNama;
    private Long fungsiKawasanId;
    private String fungsiKawasanNama;
    private Double targetLuas;
    private Double realisasiLuas;
    private Integer tahunId;
    private String keterangan;
    private Long statusId;
    private String statusNama;
    
    // Constructor to convert from Entity
    public KegiatanRencanaRealisasiDTO(KegiatanRencanaRealisasi entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            // Assuming Kegiatan has a name property - replace with actual property if different
            this.kegiatanNama = entity.getKegiatan().getNama(); 
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