package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.rh.master.model.dto.LovDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanFungsiKawasanDTO {
    private Long id;
    private Long kegiatanId;
    private LovDTO fungsiKawasanId;
    private LovDTO statusId;
    private Double targetLuas;
    private Double realisasiLuas;
    private Integer tahunId;
    private String keterangan;
    
    public KegiatanFungsiKawasanDTO(KegiatanFungsiKawasan entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        if (entity.getFungsiKawasanId() != null) {
            this.fungsiKawasanId = new LovDTO(entity.getFungsiKawasanId());
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
        
        this.targetLuas = entity.getTargetLuas();
        this.realisasiLuas = entity.getRealisasiLuas();
        this.tahunId = entity.getTahunId();
        this.keterangan = entity.getKeterangan();
    }
}