package com.kehutanan.pepdas.program.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Data
public class ProgramDto {
    private String direktorat;
    private String kategori;
    private String nama;
    private Integer tahunRencana;
    private BigDecimal totalAnggaran;
    private String status;
}