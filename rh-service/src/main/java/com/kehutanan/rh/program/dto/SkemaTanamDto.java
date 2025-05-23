package com.kehutanan.rh.program.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class SkemaTanamDto {
    private String pola;
    private Integer jumlahBtgHa;
    private Integer targetLuas;
    private String status;
    private String keterangan;
    private UUID programId;
}