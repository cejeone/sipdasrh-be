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
@Table(name = "trx_rm_kegiatan_p12_sulaman")
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