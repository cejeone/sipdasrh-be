package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanSulaman;
import com.kehutanan.rh.master.model.dto.LovDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanSulamanDTO {
    private Long id;
    private Long kegiatanId;
    private LovDTO kategoriId;
    private LovDTO namaBibitId;
    private LovDTO sumberBibitId;
    private LovDTO kondisiTanamanId;
    private LovDTO statusId;
    private String waktuPenyulaman;
    private Integer jumlahBibit;
    private Integer jumlahTanamanHidup;
    private Integer jumlahHokPerempuan;
    private Integer jumlahHokLakiLaki;
    private String keterangan;
    
    public KegiatanPemeliharaanSulamanDTO(KegiatanPemeliharaanSulaman entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        if (entity.getKategoriId() != null) {
            this.kategoriId = new LovDTO(entity.getKategoriId());
        }
        
        if (entity.getNamaBibitId() != null) {
            this.namaBibitId = new LovDTO(entity.getNamaBibitId());
        }
        
        if (entity.getSumberBibitId() != null) {
            this.sumberBibitId = new LovDTO(entity.getSumberBibitId());
        }
        
        if (entity.getKondisiTanamanId() != null) {
            this.kondisiTanamanId = new LovDTO(entity.getKondisiTanamanId());
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
        
        this.waktuPenyulaman = entity.getWaktuPenyulaman() != null ? entity.getWaktuPenyulaman().toString() : null;
        this.jumlahBibit = entity.getJumlahBibit();
        this.jumlahTanamanHidup = entity.getJumlahTanamanHidup();
        this.jumlahHokPerempuan = entity.getJumlahHokPerempuan();
        this.jumlahHokLakiLaki = entity.getJumlahHokLakiLaki();
        this.keterangan = entity.getKeterangan();
    }
}