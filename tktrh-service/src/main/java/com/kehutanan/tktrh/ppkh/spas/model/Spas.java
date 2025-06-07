package com.kehutanan.tktrh.ppkh.spas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.Das;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.ppkh.program.model.Program;

@Data
@Entity(name = "ppkhSpas")
@Table(name = "trx_ppkh_spas")
@NoArgsConstructor
@AllArgsConstructor
public class Spas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdas;

    @ManyToOne
    @JoinColumn(name = "das_id", referencedColumnName = "id")
    private Das das;

    @ManyToOne
    @JoinColumn(name = "spas_id", referencedColumnName = "id")
    private Lov spasId;

    @Column(name = "tanggal")
    private LocalDateTime tanggal;

    @Column(name = "nilai_tma")
    private Double nilaiTma;

    @Column(name = "nilai_curah_hujan")
    private Double nilaiCurahHujan;

    @Column(name = "tegangan_baterai")
    private Double teganganBaterai;

}