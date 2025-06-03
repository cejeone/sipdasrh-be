package com.kehutanan.tktrh.konten.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.tktrh.master.model.Lov;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "trx_tktrh_konten")
@NoArgsConstructor
@AllArgsConstructor
public class Konten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipe_id", referencedColumnName = "id")
    private Lov tipe;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @Column(name = "judul")
    private String judul;

    @Column(name = "konten", columnDefinition = "TEXT")
    private String konten;

    @ElementCollection
    @CollectionTable(
        name = "trx_rh_konten_kata_kunci",
        joinColumns = @JoinColumn(name = "rh_konten_id")
    )
    @Column(name = "kata_kunci")
    private List<String> kataKunci;


    @Column(name = "waktu_awal_tayang")
    private LocalDateTime waktuAwalTayang;

    @Column(name = "waktu_akhir_tayang")
    private LocalDateTime waktuAkhirTayang;

    @OneToMany(mappedBy = "konten", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KontenGambar> kontenGambars = new ArrayList<>();

    
    @OneToMany(mappedBy = "konten", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KontenGambarUtama> kontenGambarUtamas = new ArrayList<>();
}