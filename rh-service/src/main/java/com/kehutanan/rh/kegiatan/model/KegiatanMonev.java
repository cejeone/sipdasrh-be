package com.kehutanan.rh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.rh.master.model.Lov;

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
@Table(name = "trx_rh_kegiatan_monev")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    @JsonBackReference
    private Kegiatan kegiatan;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

    @Column(name = "nomor")
    private String nomor;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @OneToMany(mappedBy = "kegiatanMonev", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanMonevKriteria> kriterias = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatanMonev", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanMonevPdf> kegiatanMonevPdfs = new ArrayList<>();

}