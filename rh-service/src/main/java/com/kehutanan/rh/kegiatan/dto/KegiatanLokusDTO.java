package com.kehutanan.rh.kegiatan.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.rh.kegiatan.model.KegiatanLokus;
import com.kehutanan.rh.kegiatan.model.KegiatanLokusShp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokusDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private String alamat;
    
    // Related entity IDs instead of full entities
    private Long provinsiId;
    private String provinsiNama;
    private Long kabupatenKotaId;
    private String kabupatenKotaNama;
    private Long kecamatanId;
    private String kecamatanNama;
    private Long kelurahanDesaId;
    private String kelurahanDesaNama;
    
    // Lists for file references
    private List<KegiatanLokusFileDTO> kegiatanLokusShpList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanLokusDTO(KegiatanLokus entity) {
        this.id = entity.getId();
        this.alamat = entity.getAlamat();
        
        // Set kegiatan ID
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        // Set related entity IDs and names
        if (entity.getProvinsi() != null) {
            this.provinsiId = entity.getProvinsi().getId();
            this.provinsiNama = entity.getProvinsi().getNamaProvinsi();
        }
        
        if (entity.getKabupatenKota() != null) {
            this.kabupatenKotaId = entity.getKabupatenKota().getId();
            this.kabupatenKotaNama = entity.getKabupatenKota().getKabupatenKota();
        }
        
        if (entity.getKecamatan() != null) {
            this.kecamatanId = entity.getKecamatan().getId();
            this.kecamatanNama = entity.getKecamatan().getKecamatan();
        }
        
        if (entity.getKelurahanDesa() != null) {
            this.kelurahanDesaId = entity.getKelurahanDesa().getId();
            this.kelurahanDesaNama = entity.getKelurahanDesa().getKelurahan();
        }
        
        // Convert file lists
        if (entity.getKegiatanLokusShps() != null) {
            for (KegiatanLokusShp shp : entity.getKegiatanLokusShps()) {
                this.kegiatanLokusShpList.add(new KegiatanLokusFileDTO(shp));
            }
        }
    }
}