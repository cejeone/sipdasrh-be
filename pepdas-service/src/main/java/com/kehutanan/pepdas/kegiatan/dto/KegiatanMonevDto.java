package com.kehutanan.pepdas.kegiatan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevDto {
    private String nomor;
    private LocalDate tanggal;
    private String deskripsi;
    private String status;
    private UUID kegiatanId;
}