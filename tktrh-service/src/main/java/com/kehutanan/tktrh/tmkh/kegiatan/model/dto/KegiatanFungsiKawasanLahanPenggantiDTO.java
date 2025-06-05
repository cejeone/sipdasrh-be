package com.kehutanan.tktrh.tmkh.kegiatan.model.dto;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanLahanPengganti;

import lombok.Data;

@Data
public class KegiatanFungsiKawasanLahanPenggantiDTO {
    
    private Long id;
    private LovDTO fungsiKawasanId;
    private Double luas;
    private LovDTO tumpangTindih;
    private Double rasioTumpangTindih;
    private String keterangan;
    private LovDTO statusId;
    
    public KegiatanFungsiKawasanLahanPenggantiDTO(KegiatanFungsiKawasanLahanPengganti entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.fungsiKawasanId = entity.getFungsiKawasanId() != null ? new LovDTO(entity.getFungsiKawasanId()) : null;
            this.luas = entity.getLuas();
            this.tumpangTindih = entity.getTumpangTindih() != null ? new LovDTO(entity.getTumpangTindih()) : null;
            this.rasioTumpangTindih = entity.getRasioTumpangTindih();
            this.keterangan = entity.getKeterangan();
            this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
        }
    }
}
