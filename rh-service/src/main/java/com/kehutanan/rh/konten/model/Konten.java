package com.kehutanan.rh.konten.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(name = "rh_konten")
public class Konten {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tipe", nullable = false)
    private String tipe;

    @Column(name = "judul", nullable = false)
    private String judul;

    @Column(name = "konten", columnDefinition = "TEXT", nullable = false)
    private String konten;

    @ElementCollection
    @CollectionTable(
        name = "rh_konten_kata_kunci",
        joinColumns = @JoinColumn(name = "rh_konten_id")
    )
    @Column(name = "kata_kunci")
    private List<String> kataKunci;

    @Column(name = "waktu_awal_tayang", nullable = false)
    private LocalDateTime waktuAwalTayang;

    @Column(name = "waktu_akhir_tayang", nullable = false)
    private LocalDateTime waktuAkhirTayang;

    @Column(name = "status", nullable = false)
    private String status;

    @JsonManagedReference
    @OneToMany(mappedBy = "konten", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KontenGambar> kontenGambars = new ArrayList<>();

    
    @JsonManagedReference
    @OneToMany(mappedBy = "konten", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KontenGambarUtama> kontenGambarUtamas = new ArrayList<>();


}