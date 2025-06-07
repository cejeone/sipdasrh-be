package com.kehutanan.ppth.penghijauan.kegiatan.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.ppth.master.model.Lov;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "penghijauanKegiatanMonev")
@Table(name = "trx_ppth_kegiatan_penghijauan_monev")
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

    @Column(name = "nomor")
    private String nomor;

    
    @Column(name = "tanggal")
    private LocalDate tanggal;

    
    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;
    

    @OneToMany(mappedBy = "kegiatanMonev", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanMonevKriteria> kriterias = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatanMonev", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanMonevPdf> kegiatanMonevPdfs = new ArrayList<>();

}