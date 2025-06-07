package com.kehutanan.ppth.penghijauan.serahterima.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.ppth.penghijauan.kegiatan.model.Kegiatan;
import com.kehutanan.ppth.master.model.Lov;

@Data
@Entity(name = "pehijauanBast")
@Table(name = "trx_ppth_bast_penghijauan")
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