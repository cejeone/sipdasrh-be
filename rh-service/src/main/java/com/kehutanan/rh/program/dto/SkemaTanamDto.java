package com.kehutanan.rh.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SkemaTanamDto {
    @NotBlank(message = "Kategori is required")
    private String kategori;
    
    private BigDecimal skemaBtgHa;
    
    private BigDecimal targetLuas;
    
    @NotBlank(message = "Status is required")
    private String status;
    
    private String keterangan;
    
    @NotNull(message = "Program ID is required")
    private UUID programId;
}