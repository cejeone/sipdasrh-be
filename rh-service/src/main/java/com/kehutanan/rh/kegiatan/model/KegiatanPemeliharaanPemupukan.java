package com.kehutanan.rh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.rh.master.model.Lov;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_rh_kegiatan_p12_pemupukan")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanPemupukan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    private Kegiatan kegiatan;

    @ManyToOne
    @JoinColumn(name = "jenis_id", referencedColumnName = "id")
    private Lov jenisId;

    @ManyToOne
    @JoinColumn(name = "satuan_id", referencedColumnName = "id")
    private Lov satuanId;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

    @Column(name = "waktu_pemupukan")
    private LocalDate waktuPemupukan;

    @Column(name = "jumlah_pupuk")
    private String jumlahPupuk;

    @Column(columnDefinition = "TEXT")
    private String keterangan;
}