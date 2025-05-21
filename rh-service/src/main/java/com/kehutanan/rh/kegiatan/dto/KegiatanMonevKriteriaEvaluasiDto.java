package com.kehutanan.rh.kegiatan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevKriteriaEvaluasiDto {
    private String aktivitas;
    private Integer target;
    private Integer realisasi;
    private String catatan;
    private UUID kegiatanMonevId;
}