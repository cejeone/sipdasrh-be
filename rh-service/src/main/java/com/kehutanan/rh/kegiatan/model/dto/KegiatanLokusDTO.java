package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.KegiatanLokus;
import com.kehutanan.rh.master.model.dto.KabupatenKotaDTO;
import com.kehutanan.rh.master.model.dto.KecamatanDTO;
import com.kehutanan.rh.master.model.dto.KelurahanDesaDTO;
import com.kehutanan.rh.master.model.dto.ProvinsiDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokusDTO {
    private Long id;
    private Long kegiatanId;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private KelurahanDesaDTO kelurahanDesa;
    private String alamat;
    private List<KegiatanLokusShpDTO> kegiatanLokusShps = new ArrayList<>();
    
    public KegiatanLokusDTO(KegiatanLokus entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        if (entity.getProvinsi() != null) {
            this.provinsi = new ProvinsiDTO(entity.getProvinsi());
        }
        
        if (entity.getKabupatenKota() != null) {
            this.kabupatenKota = new KabupatenKotaDTO(entity.getKabupatenKota());
        }
        
        if (entity.getKecamatan() != null) {
            this.kecamatan = new KecamatanDTO(entity.getKecamatan());
        }
        
        if (entity.getKelurahanDesa() != null) {
            this.kelurahanDesa = new KelurahanDesaDTO(entity.getKelurahanDesa());
        }
        
        this.alamat = entity.getAlamat();
        
        if (entity.getKegiatanLokusShps() != null && !entity.getKegiatanLokusShps().isEmpty()) {
            this.kegiatanLokusShps = entity.getKegiatanLokusShps().stream()
                .map(KegiatanLokusShpDTO::new)
                .collect(Collectors.toList());
        }
    }
}