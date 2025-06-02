package com.kehutanan.pepdas.serahterima.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.konten.model.KontenGambar;
import com.kehutanan.pepdas.master.model.Lov;
import com.kehutanan.pepdas.monev.model.MonevPdf;

@Data
@Entity
@Table(name = "trx_pepdas_bast")
public class Bast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    private Kegiatan kegiatan;

    @Column(name = "nomor_bast")
    private String nomorBast;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @OneToMany(mappedBy = "bast", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BastPdf> bastPdfs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

}