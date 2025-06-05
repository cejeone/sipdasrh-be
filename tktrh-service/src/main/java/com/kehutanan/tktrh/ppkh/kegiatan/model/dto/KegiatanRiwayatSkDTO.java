package com.kehutanan.tktrh.ppkh.kegiatan.model.dto;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRiwayatSk;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRiwayatSkShp;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRiwayatSkDTO {
    private Long id;
    private LovDTO jenisPerubahan;
    private String nomorSk;
    private String tanggalSk;
    private String tanggalBerakhirSk;
    private Double luasSkHa;
    private List<KegiatanRiwayatSkShpDTO> kegiatanRiwayatSkShps;
    private String keterangan;
    private LovDTO status;

    public KegiatanRiwayatSkDTO(KegiatanRiwayatSk entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.jenisPerubahan = entity.getJenisPerubahan() != null ? new LovDTO(entity.getJenisPerubahan()) : null;
            this.nomorSk = entity.getNomorSk();
            this.tanggalSk = entity.getTanggalSk() != null ? 
                entity.getTanggalSk().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
            this.tanggalBerakhirSk = entity.getTanggalBerakhirSk() != null ? 
                entity.getTanggalBerakhirSk().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
            this.luasSkHa = entity.getLuasSkHa();
            
            if (entity.getKegiatanRiwayatSkShps() != null) {
                this.kegiatanRiwayatSkShps = entity.getKegiatanRiwayatSkShps().stream()
                    .map(KegiatanRiwayatSkShpDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.kegiatanRiwayatSkShps = new ArrayList<>();
            }
            
            this.keterangan = entity.getKeterangan();
            this.status = entity.getStatus() != null ? new LovDTO(entity.getStatus()) : null;
        }
    }
}
