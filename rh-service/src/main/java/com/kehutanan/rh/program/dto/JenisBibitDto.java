package com.kehutanan.rh.program.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class JenisBibitDto {
    private String kategori;
    private String namaBibit;
    private String sumberBibit;
    private Integer jumlah;
    private String status;
    private String keterangan;
    private UUID programId;
}