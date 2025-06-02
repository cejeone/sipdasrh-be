package com.kehutanan.pepdas.program.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kehutanan.pepdas.master.model.Lov;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "trx_pepdas_program_pagu")
@Data
@JsonIgnoreProperties({"program"})
@NoArgsConstructor
public class ProgramPagu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Lov kategori;

    @ManyToOne
    @JoinColumn(name = "sumber_anggaran_id", referencedColumnName = "id")
    private Lov sumberAnggaran;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @Column(name = "tahun_anggaran")
    private Integer tahunAnggaran;

    @Column(name = "pagu")
    private Double pagu;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
}