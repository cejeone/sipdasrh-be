package com.kehutanan.ppth.mataair.monev.model;

import com.kehutanan.ppth.master.model.Bpdas;
import com.kehutanan.ppth.mataair.program.model.Program;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Entity(name = "mataairMonevPusat")
@Table(name = "trx_ppth_monev_mata_air")
@NoArgsConstructor
@AllArgsConstructor
public class MonevPusat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id",referencedColumnName = "id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "bpdas_id",referencedColumnName = "id")
    private Bpdas bpdasId;

    @Column(name = "luas_total_target")
    private Double luasTotalTarget;

    @Column(name = "luas_total_realisasi")
    private Double luasTotalRealisasi;

    @Column(name = "target_t1")
    private Double targetT1;

    @Column(name = "realisasi_t1")
    private Double realisasiT1;

    @Column(name = "target_p0")
    private Double targetP0;

    @Column(name = "realisasi_p0")
    private Double realisasiP0;

    @Column(name = "target_p1")
    private Double targetP1;

    @Column(name = "realisasi_p1")
    private Double realisasiP1;

    @Column(name = "target_p2")
    private Double targetP2;

    @Column(name = "realisasi_p2")
    private Double realisasiP2;

    @Column(name = "target_bast")
    private Double targetBast;

    @Column(name = "realisasi_bast")
    private Double realisasiBast;

    private String keterangan;
}
