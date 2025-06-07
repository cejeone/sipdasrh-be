package com.kehutanan.ppth.mataair.serahterima.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.ppth.master.model.Lov;
import com.kehutanan.ppth.mataair.kegiatan.model.Kegiatan;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity(name = "mataairBast")
@Table(name = "trx_ppth_bast_mata_air")
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