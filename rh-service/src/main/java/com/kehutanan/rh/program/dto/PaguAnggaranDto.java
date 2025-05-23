package com.kehutanan.rh.program.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaguAnggaranDto {
    private String sumberAnggaran;
    private Integer tahunAnggaran;
    private BigDecimal pagu;
    private String status;
    private String keterangan;
    private UUID programId;
}