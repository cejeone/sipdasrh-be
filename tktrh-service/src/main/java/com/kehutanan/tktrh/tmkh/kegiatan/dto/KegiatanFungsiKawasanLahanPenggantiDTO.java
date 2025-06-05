package com.kehutanan.tktrh.tmkh.kegiatan.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanLahanPengganti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanFungsiKawasanLahanPenggantiDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    
    // Fungsi Kawasan
    private Long fungsiKawasanId;
    private String fungsiKawasanNama;
    
    private Double luas;
    
    // Tumpang Tindih
    private Long tumpangTindihId;
    private String tumpangTindihNama;
    
    private Double rasioTumpangTindih;
    private String keterangan;
    
    // Status
    private Long statusId;
    private String statusNama;
    
    // Constructor to convert from Entity
    public KegiatanFungsiKawasanLahanPenggantiDTO(KegiatanFungsiKawasanLahanPengganti entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        if (entity.getFungsiKawasanId() != null) {
            this.fungsiKawasanId = entity.getFungsiKawasanId().getId();
            this.fungsiKawasanNama = entity.getFungsiKawasanId().getNama();
        }
        
        this.luas = entity.getLuas();
        
        if (entity.getTumpangTindih() != null) {
            this.tumpangTindihId = entity.getTumpangTindih().getId();
            this.tumpangTindihNama = entity.getTumpangTindih().getNama();
        }
        
        this.rasioTumpangTindih = entity.getRasioTumpangTindih();
        this.keterangan = entity.getKeterangan();
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNama();
        }
    }
}
