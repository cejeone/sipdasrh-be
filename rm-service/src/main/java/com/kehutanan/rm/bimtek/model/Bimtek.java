package com.kehutanan.rm.bimtek.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.rm.master.model.Bpdas;
import com.kehutanan.rm.master.model.UptdDokumentasiVideo;
import com.kehutanan.rm.program.model.Program;

@Data
@Entity
@Table(name = "trx_rm_bimtek")
@NoArgsConstructor
@AllArgsConstructor
public class Bimtek implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    @JsonBackReference
    private Program program;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdasId;

    @Column(name = "nama_bimtek")
    private String namaBimtek;

    @Column(name = "subjek", columnDefinition = "TEXT")
    private String subjek;

    @Column(name = "tempat", columnDefinition = "TEXT")
    private String tempat;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "audience", columnDefinition = "TEXT")
    private String audience;

    @OneToMany(mappedBy = "bimtek", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BimtekPdf> bimtekPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "bimtek", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BimtekVideo> bimtekVideos = new ArrayList<>();

    @OneToMany(mappedBy = "bimtek", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BimtekFoto> bimtekFotos = new ArrayList<>();

    @Column(name = "evaluasi", columnDefinition = "TEXT")
    private String evaluasi;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;
}