package com.kehutanan.ppth.mataair.kegiatan.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.ppth.master.model.Lov;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "mataairKegiatanPemeliharaanPemupukan")
@Table(name = "trx_ppth_kegiatan_mata_air_pemupukan")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanPemupukan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    @JsonBackReference
    private Kegiatan kegiatan;

    @ManyToOne
    @JoinColumn(name = "jenis_id", referencedColumnName = "id")
    private Lov jenisId;

        @Column(name = "waktu_pemupukan")
    private LocalDate waktuPemupukan;

    
    @Column(name = "jumlah_pupuk")
    private String jumlahPupuk;

    @ManyToOne
    @JoinColumn(name = "satuan_id", referencedColumnName = "id")
    private Lov satuanId;


    @Column(columnDefinition = "TEXT")
    private String keterangan;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;




}