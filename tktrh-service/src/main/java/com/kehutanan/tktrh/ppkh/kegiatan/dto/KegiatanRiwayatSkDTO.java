package com.kehutanan.tktrh.ppkh.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRiwayatSk;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRiwayatSkShp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRiwayatSkDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private String kegiatanNama;
    
    private Long jenisPerubahanId;
    private String jenisPerubahanKode;
    private String jenisPerubahanNama;
    
    private String nomorSk;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalSk;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalBerakhirSk;
    
    private Double luasSkHa;
    private String keterangan;
    
    private Long statusId;
    private String statusKode;
    private String statusNama;
    
    // Lists for file references
    private List<KegiatanRiwayatSkFileDTO> kegiatanRiwayatSkShpList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanRiwayatSkDTO(KegiatanRiwayatSk entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            // Assuming Kegiatan has a name field, adjust accordingly
            // this.kegiatanNama = entity.getKegiatan().getNama();
        }
        
        if (entity.getJenisPerubahan() != null) {
            this.jenisPerubahanId = entity.getJenisPerubahan().getId();
            this.jenisPerubahanKode = entity.getJenisPerubahan().getKode();
            this.jenisPerubahanNama = entity.getJenisPerubahan().getNama();
        }
        
        this.nomorSk = entity.getNomorSk();
        this.tanggalSk = entity.getTanggalSk();
        this.tanggalBerakhirSk = entity.getTanggalBerakhirSk();
        this.luasSkHa = entity.getLuasSkHa();
        this.keterangan = entity.getKeterangan();
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusKode = entity.getStatus().getKode();
            this.statusNama = entity.getStatus().getNama();
        }
        
        // Convert file lists
        if (entity.getKegiatanRiwayatSkShps() != null) {
            for (KegiatanRiwayatSkShp shp : entity.getKegiatanRiwayatSkShps()) {
                this.kegiatanRiwayatSkShpList.add(new KegiatanRiwayatSkFileDTO(shp));
            }
        }
    }
}