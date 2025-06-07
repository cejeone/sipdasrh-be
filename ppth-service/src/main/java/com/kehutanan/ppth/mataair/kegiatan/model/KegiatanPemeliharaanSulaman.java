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
@Entity(name = "mataairKegiatanPemeliharaanSulaman")
@Table(name = "trx_ppth_kegiatan_mata_airsulaman")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanSulaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    @JsonBackReference
    private Kegiatan kegiatan;

    @ManyToOne
    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Lov kategoriId;

    @Column(name = "waktu_penyulaman")
    private LocalDate waktuPenyulaman;

    @ManyToOne
    @JoinColumn(name = "nama_bibit_id", referencedColumnName = "id")
    private Lov namaBibitId;

    @ManyToOne
    @JoinColumn(name = "sumber_bibit_id", referencedColumnName = "id")
    private Lov sumberBibitId;

    
    @Column(name = "jumlah_bibit")
    private Integer jumlahBibit;

    @ManyToOne
    @JoinColumn(name = "kondisi_tanaman_id", referencedColumnName = "id")
    private Lov kondisiTanamanId;

        @Column(name = "jumlah_tanaman_hidup")
    private Integer jumlahTanamanHidup;

    @Column(name = "jumlah_hok_perempuan")
    private Integer jumlahHokPerempuan;

    @Column(name = "jumlah_hok_laki_laki")
    private Integer jumlahHokLakiLaki;


    @Column(columnDefinition = "TEXT")
    private String keterangan;

    
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;
}