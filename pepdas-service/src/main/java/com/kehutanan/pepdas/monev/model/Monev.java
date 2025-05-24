package com.kehutanan.pepdas.monev.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;

@Data
@Entity
@Table(name = "pepdas_monev")
@NoArgsConstructor
@AllArgsConstructor
public class Monev {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kegiatan_id", nullable = false)
    private Kegiatan kegiatan;

    @Column(name = "nomor_monev", nullable = false)
    private String nomorMonev;

    @Column(name = "kontrak", nullable = false)
    private String kontrak;

    @Column(name = "rantek", nullable = false)
    private String rantek;

    @Column(name = "pelaksana", nullable = false)
    private String pelaksana;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @OneToMany(mappedBy = "monev", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MonevPdf> monevPdfs = new ArrayList<>();

    private String status;

}