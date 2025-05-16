package com.kehutanan.rh.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaguAnggaranDto {
    @NotBlank(message = "Kategori is required")
    private String kategori;
    
    @NotBlank(message = "Sumber anggaran is required")
    private String sumberAnggaran;
    
    @NotNull(message = "Tahun anggaran is required")
    private Integer tahunAnggaran;
    
    private BigDecimal pagu;
    
    @NotBlank(message = "Status is required")
    private String status;
    
    private String keterangan;
    
    @NotNull(message = "Program ID is required")
    private UUID programId;
}