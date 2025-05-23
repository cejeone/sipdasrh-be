
package com.kehutanan.pepdas.dokumen.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class DokumenDto {
    private String tipe;
    private String namaDokumen;
    private String status; 
    private String keterangan;
    private LocalDateTime uploadedAt;
}