package com.kehutanan.rh.kegiatan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanFungsiKawasanDto {
    

    private UUID id;

    private String fungsiKawasan;

    private Integer targetLuasHa;

    private Integer realisasiLuas;
    
    private Integer tahun;
    
    private String keterangan;
    
    private String status;

    private UUID kegiatanId;
}