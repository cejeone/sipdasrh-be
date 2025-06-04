package com.kehutanan.tktrh.tmkh.kegiatan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.tktrh.master.model.Lov;

/**
 * Entity class representing the trx_bkta_riwayat_sk table
 * This table stores information about SK (Surat Keputusan) history for BKTA
 * activities
 */
@Data
@Entity
@Table(name = "trx_tmkh_riwayat_sk")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRiwayatSk implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    private Kegiatan kegiatan;

        @ManyToOne
    @JoinColumn(name = "jenis_perubahan", referencedColumnName = "id")
    private Lov jenisPerubahan;

    @Column(name = "nomor_sk", columnDefinition = "TEXT")
    private String nomorSk;

    @Column(name = "tanggal_sk")
    private LocalDate tanggalSk;

    @Column(name = "tanggal_berakhir_sk")
    private LocalDate tanggalBerakhirSk;

    @Column(name = "luas_sk_ha")
    private Double luasSkHa;

    @OneToMany(mappedBy = "kegiatan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanRiwayatSkShp> kegiatanRiwayatSkShps = new ArrayList<>();

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

}