package com.kehutanan.pepdas.kegiatan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanSulamanDto {
    private String kategori;
    private LocalDate waktuPenyulaman;
    private String namaBibit;
    private Integer jumlahBibit;
    private String kondisiTanaman;
    private Integer jumlahTanamanHidup;
    private Integer jumlahHokPerempuan;
    private Integer jumlahHokLakiLaki;
    private String keterangan;
    private String status;
    private UUID kegiatanId;
}