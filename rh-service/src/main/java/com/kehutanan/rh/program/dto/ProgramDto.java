package com.kehutanan.rh.program.dto;

import com.kehutanan.rh.program.model.JenisBibit;
import com.kehutanan.rh.program.model.PaguAnggaran;
import com.kehutanan.rh.program.model.SkemaTanam;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class ProgramDto {
    private UUID id;
    private String direktorat;
    private String kategori;
    private String nama;
    private Integer tahunPelaksanaan;
    private BigDecimal totalAnggaran;
    private Integer targetLuas;
    private String status;
    private List<JenisBibit> jenisBibit;
    private List<PaguAnggaran> paguAnggaran;
    private List<SkemaTanam> skemaTanam;
}