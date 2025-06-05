package com.kehutanan.tktrh.tmkh.kegiatan.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRiwayatSk;

import lombok.Data;

@Data
public class KegiatanRiwayatSkDTO {
    
    private Long id;
    private LovDTO jenisPerubahan;
    private String nomorSk;
    private String tanggalSk;
    private String tanggalBerakhirSk;
    private Double luasSkHa;
    private List<KegiatanRiwayatSkShpDTO> kegiatanRiwayatSkShps = new ArrayList<>();
    private String keterangan;
    private LovDTO status;
    
    public KegiatanRiwayatSkDTO(KegiatanRiwayatSk entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.jenisPerubahan = entity.getJenisPerubahan() != null ? 
                new LovDTO(entity.getJenisPerubahan()) : null;
            this.nomorSk = entity.getNomorSk();
            this.tanggalSk = entity.getTanggalSk() != null ? entity.getTanggalSk().toString() : null;
            this.tanggalBerakhirSk = entity.getTanggalBerakhirSk() != null ? 
                entity.getTanggalBerakhirSk().toString() : null;
            this.luasSkHa = entity.getLuasSkHa();
            this.keterangan = entity.getKeterangan();
            this.status = entity.getStatus() != null ? new LovDTO(entity.getStatus()) : null;
            
            if (entity.getKegiatanRiwayatSkShps() != null) {
                this.kegiatanRiwayatSkShps = entity.getKegiatanRiwayatSkShps().stream()
                    .map(KegiatanRiwayatSkShpDTO::new)
                    .collect(Collectors.toList());
            }
        }
    }
}
