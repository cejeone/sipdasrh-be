package com.kehutanan.tktrh.ppkh.kegiatan.model.dto;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRencanaRealisasi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRencanaRealisasiDTO {
    private Long id;
    private LovDTO fungsiKawasanId;
    private Double targetLuas;
    private Double realisasiLuas;
    private Integer tahunId;
    private String keterangan;
    private LovDTO statusId;

    public KegiatanRencanaRealisasiDTO(KegiatanRencanaRealisasi entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.fungsiKawasanId = entity.getFungsiKawasanId() != null ? new LovDTO(entity.getFungsiKawasanId()) : null;
            this.targetLuas = entity.getTargetLuas();
            this.realisasiLuas = entity.getRealisasiLuas();
            this.tahunId = entity.getTahunId();
            this.keterangan = entity.getKeterangan();
            this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
        }
    }
}
