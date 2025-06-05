package com.kehutanan.tktrh.ppkh.kegiatan.model.dto;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastReboRehab;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanBastReboRehabDTO {
    private Long id;
    private Integer tahunId;
    private Double targetLuas;
    private String keterangan;
    private LovDTO statusSerahTerima;

    public KegiatanBastReboRehabDTO(KegiatanBastReboRehab entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.tahunId = entity.getTahunId();
            this.targetLuas = entity.getTargetLuas();
            this.keterangan = entity.getKeterangan();
            this.statusSerahTerima = entity.getStatusSerahTerima() != null ? 
                new LovDTO(entity.getStatusSerahTerima()) : null;
        }
    }
}
