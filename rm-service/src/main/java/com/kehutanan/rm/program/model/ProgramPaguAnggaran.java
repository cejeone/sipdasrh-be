package com.kehutanan.rm.program.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kehutanan.rm.master.model.Lov;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "trx_rm_program_pagu_anggaran")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguAnggaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    @JsonBackReference
    private Program program;

    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Lov kategoriId;

    @JoinColumn(name = "sumber_anggaran_id", referencedColumnName = "id")
    private Lov sumberAnggaranId;

    @Column(name = "tahun_anggaran")
    private Integer tahunAnggaran;

    private Double pagu;

    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
}
