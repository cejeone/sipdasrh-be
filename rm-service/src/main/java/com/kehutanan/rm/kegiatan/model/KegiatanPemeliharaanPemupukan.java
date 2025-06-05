package com.kehutanan.rm.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.rm.master.model.Lov;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_rm_kegiatan_p12_pemupukan")
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