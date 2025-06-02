package com.kehutanan.pepdas.pemantauandas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

import com.kehutanan.pepdas.master.model.Bpdas;
import com.kehutanan.pepdas.master.model.Das;
import com.kehutanan.pepdas.master.model.Spas;

@Data
@Entity
@Table(name =  "trx_pedpas_pemantauan_das")
@NoArgsConstructor
@AllArgsConstructor
public class PemantauanDas {
    
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
    private Spas spas;

    @Column(name = "tanggal_dan_waktu")
    private LocalDateTime tanggalDanWaktu;

    @Column(name = "nilai_tma")
    private Integer nilaiTma;

    @Column(name = "nilai_curah_hujan")
    private Integer nilaiCurahHujan;

    @Column(name = "tegangan_baterai")
    private Integer teganganBaterai;
}