package com.kehutanan.rh.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class JenisBibitDto {
    @NotBlank(message = "Kategori is required")
    private String kategori;
    
    @NotBlank(message = "Nama bibit is required")
    private String namaBibit;
    
    private String sumberBibit;
    
    private Integer jumlah;
    
    @NotBlank(message = "Status is required")
    private String status;
    
    private String keterangan;
    
    @NotNull(message = "Program ID is required")
    private UUID programId;
}