package com.kehutanan.pepdas.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_kegiatan_pemeliharaan_sulaman")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanSulaman {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String kategori;
    
    @Column(name = "waktu_penyulaman")
    private LocalDate waktuPenyulaman;
    
    @Column(name = "nama_bibit")
    private String namaBibit;
    
    @Column(name = "jumlah_bibit")
    private Integer jumlahBibit;
    
    @Column(name = "kondisi_tanaman")
    private String kondisiTanaman;
    
    @Column(name = "jumlah_tanaman_hidup")
    private Integer jumlahTanamanHidup;
    
    @Column(name = "jumlah_hok_perempuan")
    private Integer jumlahHokPerempuan;
    
    @Column(name = "jumlah_hok_laki_laki")
    private Integer jumlahHokLakiLaki;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    private String status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_id", nullable = false)
    @JsonBackReference
    private Kegiatan kegiatan;
}