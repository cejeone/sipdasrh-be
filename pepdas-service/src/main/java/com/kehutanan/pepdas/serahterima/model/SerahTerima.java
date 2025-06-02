package com.kehutanan.pepdas.serahterima.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.pepdas.monev.model.MonevPdf;

@Data
@Entity
@Table(name = "trx_pepdas_serah_terima")
public class SerahTerima {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nomor")
    private String nomor;

    @Column(name = "kegiatan")
    private String kegiatan;

    @Column(name = "kontrak")
    private String kontrak;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @OneToMany(mappedBy = "serahTerima", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SerahTerimaPdf> serahTerimaPdfs = new ArrayList<>();

    @Column(name = "status")
    private String status;

}