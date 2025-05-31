package com.kehutanan.rh.konten.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.rh.master.model.Lov;

/**
 * Entity class representing the trx_rh_konten table
 * This table stores content information in the forestry management system
 */
@Data
@Entity
@Table(name = "trx_rh_konten")
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