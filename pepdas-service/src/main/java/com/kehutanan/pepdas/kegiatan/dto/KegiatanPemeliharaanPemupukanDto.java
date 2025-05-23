package com.kehutanan.pepdas.kegiatan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanPemupukanDto {
    private String jenis;
    private LocalDate waktuPemupukan;
    private Integer jumlahPupuk;
    private String satuan;
    private Integer jumlahHokPerempuan;
    private Integer jumlahHokLakiLaki;
    private String keterangan;
    private String status;
    private UUID kegiatanId;
}