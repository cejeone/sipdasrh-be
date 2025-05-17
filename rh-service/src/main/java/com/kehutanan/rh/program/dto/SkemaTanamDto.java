package com.kehutanan.rh.program.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class SkemaTanamDTO {
    private UUID id;
    private String pola;
    private Integer jumlahBtgHa;
    private Integer targetLuas;
    private String status;
    private String keterangan;
    private UUID programId;
}