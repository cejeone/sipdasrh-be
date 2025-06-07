package com.kehutanan.ppth.penghijauan.kegiatan.model.dto;

import com.kehutanan.ppth.penghijauan.kegiatan.model.KegiatanPemeliharaanSulaman;
import com.kehutanan.ppth.master.model.dto.LovDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KegiatanPemeliharaanSulamanDTO {
    
    private Long id;
    private LovDTO kategoriId;
    private String waktuPenyulaman;
    private LovDTO namaBibitId;
    private LovDTO sumberBibitId;
    private Integer jumlahBibit;
    private LovDTO kondisiTanamanId;
    private Integer jumlahTanamanHidup;
    private Integer jumlahHokPerempuan;
    private Integer jumlahHokLakiLaki;
    private String keterangan;
    private LovDTO statusId;
    
    public KegiatanPemeliharaanSulamanDTO(KegiatanPemeliharaanSulaman entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.kategoriId = entity.getKategoriId() != null ? new LovDTO(entity.getKategoriId()) : null;
            this.waktuPenyulaman = entity.getWaktuPenyulaman() != null ? entity.getWaktuPenyulaman().toString() : null;
            this.namaBibitId = entity.getNamaBibitId() != null ? new LovDTO(entity.getNamaBibitId()) : null;
            this.sumberBibitId = entity.getSumberBibitId() != null ? new LovDTO(entity.getSumberBibitId()) : null;
            this.jumlahBibit = entity.getJumlahBibit();
            this.kondisiTanamanId = entity.getKondisiTanamanId() != null ? new LovDTO(entity.getKondisiTanamanId()) : null;
            this.jumlahTanamanHidup = entity.getJumlahTanamanHidup();
            this.jumlahHokPerempuan = entity.getJumlahHokPerempuan();
            this.jumlahHokLakiLaki = entity.getJumlahHokLakiLaki();
            this.keterangan = entity.getKeterangan();
            this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
        }
    }
}
