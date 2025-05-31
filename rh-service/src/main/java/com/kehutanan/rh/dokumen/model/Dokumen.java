package com.kehutanan.rh.dokumen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.kehutanan.rh.master.model.Lov;

import jakarta.persistence.*;

/**
 * Entity class representing the trx_rh_dokumen table
 * This table stores document information in the forestry management system
 */
@Data
@Entity
@Table(name = "trx_rh_dokumen")
@NoArgsConstructor
@AllArgsConstructor
public class Dokumen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipe_id", referencedColumnName = "id")
    private Lov tipe;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @Column(name = "nama_dokumen", length = 255)
    private String namaDokumen;

    @OneToMany(mappedBy = "dokumen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DokumenFile> dokumenFiles = new ArrayList<>();

    @Column(name = "ukuran_dokumen")
    private Double ukuranDokumen;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
}