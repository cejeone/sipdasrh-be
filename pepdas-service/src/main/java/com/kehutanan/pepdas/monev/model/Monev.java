package com.kehutanan.pepdas.monev.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.pepdas.master.model.Lov;

@Data
@Entity
@Table(name = "trx_pepdas_monev")
@NoArgsConstructor
@AllArgsConstructor
public class Monev implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    private Kegiatan kegiatan;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @Column(name = "nomor")
    private String nomor;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @OneToMany(mappedBy = "monev", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MonevPdf> monevPdfs = new ArrayList<>();
}