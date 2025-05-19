package com.kehutanan.rh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "rh_kegiatan_monev")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonev {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nomor;

    private LocalDate tanggal;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_id", nullable = false)
    @JsonBackReference
    private Kegiatan kegiatan;

    @OneToMany(mappedBy = "kegiatanMonev", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanMonevKriteriaEvaluasi> kriteriaEvaluasiList = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanMonevPdf> kegiatanMonevPdfs = new ArrayList<>();

}